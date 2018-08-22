package com.curisprofound.test.cucumber;

import com.curisprofound.test.StepsBase;
import com.curisprofound.test.assertions.AssertOnClass;
import com.curisprofound.test.assertions.MethodAssertions;
import com.curisprofound.test.assertions.SignatureParser;
import com.curisprofound.test.assertions.TypeDef;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class ClassAssertionsStep extends StepsBase {

    @Given("^class \"([^\"]*)\" exists in the \"([^\"]*)\" package$")
    public void classExistsInThePackage(String className, String packagName) throws Throwable {
        AddClassAssertions(AssertOnClass.For(className, packagName).exists());
    }

    @Given("^I parse an argument signature as \"([^\"]*)\"$")
    public void iParseAnArgumentSignatureAsListStringMapIntegerClass(String signature) throws Throwable {
        Add(SignatureParser.class, new SignatureParser(signature));
    }

    @Then("^the result is a \"([^\"]*)\" \"([^\"]*)\" class$")
    public void theResultIsAClass(String sigType, String sigClass) throws Throwable {
        assertEquals(
                sigType,
                Get(TypeDef.class).category
        );
        assertEquals(
                TypeDef.nameToClass(sigClass),
                Get(TypeDef.class).parameterClass
        );
    }

    @And("^the result has (\\d+) subtypes$")
    public void theResultHasTwoSubtypes(int subtypeCount) throws Throwable {
        assertEquals(
                subtypeCount,
                Get(TypeDef.class).genericTypes.size()
        );
    }

    @And("^subtype (\\d+) of the result is a \"([^\"]*)\" \"([^\"]*)\" class$")
    public void subtypeOfTheResultIsAClass(int index, String subType, String subClass) throws Throwable {
        assertEquals(
                subType,
                Get(TypeDef.class).genericTypes.get(index).category
        );
        assertEquals(
                TypeDef.nameToClass(subClass),
                Get(TypeDef.class).genericTypes.get(index).parameterClass
        );
    }

    @When("^I focus on subtype (\\d+) of the result$")
    public void iFocusOnSubtypeOfTheResult(int index) throws Throwable {
        Add(TypeDef.class, Get(TypeDef.class).genericTypes.get(index));
    }

    @Then("^The signature is parsed to an \"([^\"]*)\" type with (\\d+) result$")
    public void theSignatureIsParsedToAnTypeWithResult(String sigType, int count) throws Throwable {
        String expectedType = Get(SignatureParser.class).getType();
        assertEquals(expectedType, sigType);
        assertEquals(count, Get(SignatureParser.class).getArgumentList().size());
        Add(TypeDef.class, Get(SignatureParser.class).getArgumentList().get(count-1));
    }

    @Then("^The signature is parsed to \"([^\"]*)\" type$")
    public void theSignatureIsParsedToType(String sigType) throws Throwable {
        String actualType = Get(SignatureParser.class).getType();
        assertEquals(sigType, actualType);
    }

    @And("^I focus on member (\\d+) of argumentList$")
    public void iFocusOnMemberOfArgumentList(int index) throws Throwable {
        Add(TypeDef.class, Get(SignatureParser.class).getArgumentList().get(index));
    }

    @And("^The method has return value equal to \"([^\"]*)\"$")
    public void theMethodHasReturnValueEqualTo(String returnSignature) throws Throwable {
        assertEquals(
                returnSignature,
                Get(SignatureParser.class).getReturnType().typeName()
        );
    }

    @And("^the argumentList has (\\d+) members$")
    public void theArgumentListHasMembers(int count) throws Throwable {
        assertEquals(count, Get(SignatureParser.class).getArgumentList().size());
    }

    @And("^The method name is \"([^\"]*)\"$")
    public void theMethodNameIs(String methodName) throws Throwable {
        assertEquals(methodName, Get(SignatureParser.class).getMethodName());
    }

    @Then("^a method with signature \"([^\"]*)\" exists on the class$")
    public void aMethodWithSignatureExistsOnTheClass(String signature) throws Throwable {
        Add(MethodAssertions.class, GetClassAssertions().Method(new SignatureParser(signature)).exists());
    }

    @And("^the method has a \"([^\"]*)\" annotation$")
    public void theMethodHasAAnnotation(String annotation) throws Throwable {
        Get(MethodAssertions.class).Annotation(annotation).exists();
    }
}
