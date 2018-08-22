package com.curisprofound.test;


import com.curisprofound.test.assertions.ClassAssertions;
import com.curisprofound.test.assertions.TypeDef;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepsBase {


    private static World world;
    private  ObjectMapper objectMapper;
    private TypeFactory typeFactory;


    public StepsBase(){
        world = world == null ? new World() : world;
        objectMapper = new ObjectMapper();
        typeFactory = objectMapper.getTypeFactory();
        addClassNames();
    }

    private void addClassNames() {
        Map<String, Class<?>> classNames = new HashMap<>();
        TypeDef.setClassNames();
        setClassNames(classNames);
        TypeDef.addClassNames(classNames);
    }

    public void setClassNames(Map<String,Class<?>> classNames){
        //override with the application specific class names
    }


    public void tearDown() {
        world.Clear();
    }

    protected <T> List<T> jsonStringToClassArray(String content, Class<T> clazz) throws IOException {
        return objectMapper.readValue(
                content,
                typeFactory.constructCollectionType(List.class, clazz));

    }
    protected String jsonObjectToString(Object content) throws IOException {
        return objectMapper.writeValueAsString(content);
    }
    protected <T> T jsonStringToObject(String content , Class<T> clazz) throws IOException {
        return objectMapper.readValue(content,clazz);
    }

    public <T> T Get(Class<T> clazz, String key) {
        return world.Get(clazz, key);
    }

    public <T> T Get(Class<T> clazz) {
        return world.Get(clazz);
    }

    public String Get(String key) {
        return world.Get(String.class, key);
    }

    public <T> T Add(Class<T> clazz, T target, String key) {
        return world.Add(clazz, target, key);
    }

    public <T> T Add(Class<T> clazz, T target) {
        return world.Add(clazz, target);
    }

    public <T> T Add(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return world.Add(clazz);
    }

    public Class<?> getClassFromKey(String type) {
        return TypeDef.nameToClass(type);
    }

    public ClassAssertions AddClassAssertions(ClassAssertions classAssertions){
        return Add(ClassAssertions.class, classAssertions);
    }

    public ClassAssertions GetClassAssertions(){
        return Get(ClassAssertions.class);
    }

}
