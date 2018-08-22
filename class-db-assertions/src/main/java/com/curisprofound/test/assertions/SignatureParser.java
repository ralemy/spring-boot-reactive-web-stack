package com.curisprofound.test.assertions;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;

import java.util.List;

public class SignatureParser {
    private final String signature;
    private String type;
    private TypeDef returnType;
    private List<TypeDef> argumentList;
    private String methodName;

    public SignatureParser(String signature) {
        this.signature = signature;
        try {
            if (isMethod(signature))
                setupAsMethod(signature);
            else
                this.argumentList = getTypeList(signature);
        } catch (Exception e) {
            type = "invalid signature: " + e.getMessage();
        }
    }

    public String getType(){
        return type;
    }
    private boolean isMethod(String signature) {
        return signature.contains("(");
    }


    private List<TypeDef> getTypeList(String signature) throws Exception {
        type = "argumentList";
        return TypeDef.parse(signature);
    }


    private void setupAsMethod(String signature) throws Exception {
        String[] parts = signature.split(":");
        this.returnType = getReturnType(parts.length > 1 ? parts[1].trim() : "");
        parts = signature.split("\\(");
        this.methodName = parts[0].trim();
        if(this.methodName.isEmpty())
            throw new Exception(" No method name before parenthesis");
        this.argumentList = getTypeList(parts[1].trim().split("\\)")[0].trim());
        this.type = "method";
    }

    private TypeDef getReturnType(String signature) throws Exception {
        if (signature.isEmpty())
            signature = "void";
        return getTypeList(signature).get(0);
    }

    public TypeDef getReturnType() {
        return returnType;
    }
    public String getSignature() {
        return signature;
    }

    public List<TypeDef> getArgumentList() {
        return argumentList;
    }

    public String getMethodName() {
        return methodName;
    }


}
