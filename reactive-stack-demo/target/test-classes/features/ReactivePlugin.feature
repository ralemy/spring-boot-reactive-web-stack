# Feature File for Cucumber Testing
# Created 8/9/18 5:32 AM by rezaalemy
Feature: I need a plugin that supports reactive web
  So that I can implement non-blocking use cases using the plugin

  @ReactiveEndpoints
  Scenario: Should have a reactive endpoint
    Given  There exists a class named "AppConfig" in "com.curisprofound.spring.reactivestackdemo.config" package
    And    the "Configuration" annotation exists in the class annotations
    And    the class has a method with signature "routeHello():RouterFunction<ServerResponse>"
    And    the method has a "Bean" annotation
    When   I GET the "/health" endpoint with mock user
    Then   The response status would be "OK"
    And    The response would be "endpoint established"
    When   I GET the "/hello" endpoint with mock user
    Then   The response would be "Hello World"
    When   I GET the "/health" endpoint
    Then   The response status would be "302"

  @ReactivePlugin
  Scenario: Should have a plugin manager for reactive plugins
    Given  There exists a class named "AppConfig" in "com.curisprofound.spring.reactivestackdemo.config" package
    And    the class has a method with signature "pluginManager():PluginManager"
    And    the method has a "Bean" annotation
    Then   a "PluginManager" class is autowired
    And    the "PluginManager" property would be mocked

  @ReactivePlugin
  Scenario: Should have a config class to include plugins
    Given  There exists a class named "PluginConfig" in "com.curisprofound.spring.reactivestackdemo.config" package
    And    the "Component" annotation exists in the class annotations
    And    The class implements the "org.springframework.beans.factory.BeanFactoryAware" interface
    Then   the class has a method with signature "getPluginEndpoints(PluginManager):RouterFunction<?>"
    And    the method has a "Bean" annotation

    
  Scenario: Should add RouterFunctions as endpoints
    Given  a "PluginManager" class is autowired
    And    the plugin Manager class is mocked to return one mocked plugin for ReactiveRestInterface class
    Then   the mocked ReactiveRestInterface plugin will be called to provide functional endpoints
    When   I GET the "/plugins" endpoint with mock user
    Then   The response status would be "2xx"
    And    The response would contain "mocked Plugin"

  Scenario: Should expose plugin endpoints
    Given  a "PluginManager" class is autowired
    When   I GET the "/plugins/mocked" endpoint with mock user
    Then   The response would be "returned from mocked plugin"

