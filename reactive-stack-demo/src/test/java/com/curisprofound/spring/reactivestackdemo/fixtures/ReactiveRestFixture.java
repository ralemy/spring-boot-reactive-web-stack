package com.curisprofound.spring.reactivestackdemo.fixtures;

import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

public class ReactiveRestFixture {

    public static WebTestClient bindToRoutes(List<? extends RouterFunction<?>> routerFunctions){
        assertThat(routerFunctions.size(), greaterThan(0));
        RouterFunction<ServerResponse> router = routerFunctions.stream()
                .map(r -> (RouterFunction<ServerResponse>) r)
                .reduce((a, r) -> (RouterFunction<ServerResponse>) a.andOther(r))
                .orElse(null);
        return WebTestClient.bindToRouterFunction(router).build();
    }

    public static WebTestClient bindToApplicationContext(ApplicationContext context){
        return WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .build();
    }

    public static WebTestClient.ResponseSpec checkStatusCode(WebTestClient.ResponseSpec spec, String status) {
        if (status.equalsIgnoreCase("ok"))
            return spec.expectStatus().isOk();
        if (status.equalsIgnoreCase("2xx"))
            return spec.expectStatus().is2xxSuccessful();
        if (status.equalsIgnoreCase("3xx"))
            return spec.expectStatus().is3xxRedirection();
        if (status.equalsIgnoreCase("4xx"))
            return spec.expectStatus().is4xxClientError();
        if (status.equalsIgnoreCase("5xx"))
            return spec.expectStatus().is5xxServerError();
        if (status.equalsIgnoreCase("1xx"))
            return spec.expectStatus().is1xxInformational();
        else
            return spec.expectStatus().isEqualTo(Integer.valueOf(status));
    }
}
