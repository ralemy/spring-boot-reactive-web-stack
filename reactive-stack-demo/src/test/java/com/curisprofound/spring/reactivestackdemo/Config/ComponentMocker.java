package com.curisprofound.spring.reactivestackdemo.Config;


import com.curisprofound.spring.plugin.reactive.ReactiveRestInterface;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.springframework.web.reactive.function.server.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class ComponentMocker {

    public static Object MockComponent(Class<?> clazz, Object bean){
        if(PluginManager.class.isAssignableFrom(bean.getClass()))
            return MockPluginManager((PluginManager)bean);
        else
            return Mockito.mock(clazz, AdditionalAnswers.delegatesTo(bean));
    }

    private static Object MockPluginManager(PluginManager bean) {
        PluginManager mockedBean = Mockito.mock(bean.getClass(), AdditionalAnswers.delegatesTo(bean));
        List<ReactiveRestInterface> plugins= new ArrayList<>();
        plugins.add(newMockedReactiveRestInterface());
        doReturn(plugins).when(mockedBean).getExtensions(ReactiveRestInterface.class);
        return mockedBean;
    }

    private static ReactiveRestInterface newMockedReactiveRestInterface() {
        ReactiveRestInterface plugin = Mockito.mock(ReactiveRestInterface.class);
        List<RouterFunction<?>> routes = new ArrayList<>();
        routes.add(newRouterFunction());
        doReturn(routes).when(plugin).routerFunctions();
        doReturn("mocked Plugin").when(plugin).identify();
        return plugin;
    }

    private static RouterFunction<?> newRouterFunction() {
        return route(GET("/plugins/mocked"), req-> ok()
                .body(fromObject("returned from mocked plugin")));
    }


}
