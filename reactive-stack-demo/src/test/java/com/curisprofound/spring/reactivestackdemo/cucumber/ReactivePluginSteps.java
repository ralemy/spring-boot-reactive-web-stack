package com.curisprofound.spring.reactivestackdemo.cucumber;

import com.curisprofound.spring.plugin.reactive.ReactiveRestInterface;
import com.curisprofound.spring.reactivestackdemo.StepsBase;
import com.curisprofound.spring.reactivestackdemo.fixtures.ReactiveRestFixture;
import com.curisprofound.test.assertions.*;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReactivePluginSteps extends StepsBase {

    @Autowired
    List<? extends  RouterFunction<?>> routerFunctions;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PluginManager pluginManager;

    @And("^the class has a method with signature \"([^\"]*)\"$")
    public void theClassHasAMethodWithSignature(String signature) throws Throwable {
        Add(MethodAssertions.class,
                GetClassAssertions().Method(new SignatureParser(signature))
        );
    }

    @And("^the method has a \"([^\"]*)\" annotation$")
    public void theMethodHasAAnnotation(String annotation) throws Throwable {
        Get(MethodAssertions.class).Annotation(annotation).exists();
    }


    @When("^I GET the \"([^\"]*)\" endpoint$")
    public void iGETTheEndpoint(String endpoint) throws Throwable {
        WebTestClient.ResponseSpec response = ReactiveRestFixture.bindToApplicationContext(context)
                .get().uri(endpoint).exchange();
        Add(WebTestClient.ResponseSpec.class, response);
    }

    @Then("^The response would be \"([^\"]*)\"$")
    public void theResponseWouldBe(String expected) throws Throwable {
        String actual = Get(WebTestClient.ResponseSpec.class).expectBody(String.class).returnResult().getResponseBody();
        assertEquals(
                expected, actual
        );
    }

    @When("^I GET the \"([^\"]*)\" endpoint with mock user$")
    public void iGETTheEndpointWithMockUser(String endpoint) throws Throwable {
        WebTestClient.ResponseSpec response = ReactiveRestFixture.bindToRoutes(routerFunctions)
                .get().uri(endpoint).exchange();
        Add(WebTestClient.ResponseSpec.class, response);
    }

    @Then("^The response status would be \"([^\"]*)\"$")
    public void theResponseStatusWouldBe(String status) throws Throwable {
        ReactiveRestFixture.checkStatusCode(Get(WebTestClient.ResponseSpec.class),status);
    }

    @Then("^a \"([^\"]*)\" class is autowired$")
    public void aClassIsAutowired(String className) throws Throwable {
        Class expected = AssertOnClass.For(className).classType();
        Optional<FieldAssertions> target = AssertOnClass.For(this.getClass()).getFields()
                .filter(f -> f.isSubtypeOf(expected))
                .findAny();
        assertTrue("No field of type " + expected, target.isPresent());
        assertNotNull(" field " + target.get().fieldName() + " is null", target.get().getValue(this));
        Add(expected, target.get().getValue(this));
    }


    @And("^the \"([^\"]*)\" property would be mocked$")
    public void thePropertyWouldBeMocked(String className) throws Throwable {
        Class expected = AssertOnClass.For(TypeDef.nameToClass(className)).classType();
        Object target = Get(expected);
        assertNotNull(className + " has not been stashed to the world object", target);
        assertTrue(
                className + " is not Mocked",
                MockUtil.isMock(target)
        );

    }

    @And("^the plugin Manager class is mocked to return one mocked plugin for ReactiveRestInterface class$")
    public void thePluginManagerClassIsMockedToReturnOneMockedPluginForClass() throws Throwable {
        List<ReactiveRestInterface> plugins = pluginManager.getExtensions(ReactiveRestInterface.class);
        assertEquals(
                1,
                plugins.size()
        );
        assertTrue(
                "ReactiveRestInterface plugin is not mocked",
                MockUtil.isMock(plugins.get(0))
        );
        Add(ReactiveRestInterface.class, plugins.get(0));
    }

    @Then("^the mocked ReactiveRestInterface plugin will be called to provide functional endpoints$")
    public void theMockedPluginWillBeCalledToProvideFunctionalEndpoints() throws Throwable {
        Mockito.verify(Get(ReactiveRestInterface.class)).routerFunctions();
    }

    @And("^The response would contain \"([^\"]*)\"$")
    public void theResponseWouldContain(String expected) throws Throwable {
        String actual = Get(WebTestClient.ResponseSpec.class).expectBody(String.class).returnResult().getResponseBody();
        assertThat(
                actual, Matchers.containsString(expected)
        );
    }
}
