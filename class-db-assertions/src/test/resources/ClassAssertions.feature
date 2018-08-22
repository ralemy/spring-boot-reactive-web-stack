# Feature File for Cucumber Testing
# Created 8/7/18 9:27 AM by rezaalemy

Feature: Writing assertions with Reflection
  As a developer who wants to practice TDD
  I need a library to examine classes based on reflection
  So that I can write unit tests on classes before they exist

  Scenario: Should be able to check for existence of a class
    Given class "TestClass" exists in the "com.curisprofound.test.fixtures" package

  Scenario: Should correctly interpret Signatures
    Given I parse an argument signature as "List<String, Map<Integer,Class<?>>>"
    Then  The signature is parsed to "argumentList" type
    And   the argumentList has 1 members
    And   I focus on member 0 of argumentList
    And   the result is a "generic" "List" class
    And   the result has 2 subtypes
    And   subtype 0 of the result is a "simple" "String" class
    And   subtype 1 of the result is a "generic" "Map" class
    When  I focus on subtype 1 of the result
    Then  the result has 2 subtypes
    And   subtype 0 of the result is a "simple" "Integer" class
    And   subtype 1 of the result is a "generic" "Class" class
    When  I focus on subtype 1 of the result
    Then  the result has 1 subtypes
    And   subtype 0 of the result is a "wildcard" "Class" class


  Scenario: Should correctly identify method signatures
    Given I parse an argument signature as "myMethod (List<String>, int, Object): Map<Integer,String>"
    Then  The signature is parsed to "method" type
    And   The method has return value equal to "java.util.Map<java.lang.Integer,java.lang.String>"
    And   the argumentList has 3 members
    And   The method name is "myMethod"
    When  I focus on member 0 of argumentList
    Then  the result is a "generic" "List" class
    And   the result has 1 subtypes
    And   subtype 0 of the result is a "simple" "String" class
    When  I focus on member 1 of argumentList
    Then  the result is a "simple" "int" class
    When  I focus on member 2 of argumentList
    Then  the result is a "simple" "Object" class

  Scenario: Should correctly parse signature for methods with void return type and no argumentList
    Given I parse an argument signature as "myMethod():void"
    Then  The method has return value equal to "java.lang.Void"
    And   the argumentList has 0 members
    When  I parse an argument signature as "myMethod()"
    Then  The method has return value equal to "java.lang.Void"

  Scenario: Should be able to check for existence of a method by signature
    Given class "TestClass" exists in the "com.curisprofound.test.fixtures" package
    Then  a method with signature "myMethod(List<String>, Integer, Object): int" exists on the class
    And   the method has a "Bean" annotation

