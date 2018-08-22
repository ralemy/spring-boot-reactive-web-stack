package com.curisprofound.spring.reactivestackdemo.config;

import com.curisprofound.spring.plugin.reactive.ReactiveRestInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pf4j.PluginManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class PluginConfig implements BeanFactoryAware {
    private final ObjectMapper objectMapper;
    private final PluginManager pluginManager;
    private final ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }

    @Autowired
    public PluginConfig(PluginManager pm, ApplicationContext context){
        this.pluginManager = pm;
        this.applicationContext = context;
        this.objectMapper = new ObjectMapper();
    }

    @Bean
    public RouterFunction<?> getPluginEndpoints(PluginManager pm){
        return getReactiveRoutes(pm);
    }

    private RouterFunction<?> getReactiveRoutes(PluginManager pm) {
        RouterFunction<?> base = baseRoot(pm);
        RouterFunction<?> routes = pm.getExtensions(ReactiveRestInterface.class).stream()
                .flatMap(g -> g.routerFunctions().stream())
                .map(r-> (RouterFunction<ServerResponse>)r)
                .reduce((o,r )-> (RouterFunction<ServerResponse>) o.andOther(r))
                .orElse(null);
        return routes == null ? base : base.andOther(routes);
    }

    private RouterFunction<?> baseRoot(PluginManager pm) {
        return route(GET("/plugins"),
                req -> ServerResponse.ok().body(Mono.just(pluginNamesMono(pm)), String.class));
    }

    private String pluginNamesMono(PluginManager pm) {
        List<String> identityList = pm.getExtensions(ReactiveRestInterface.class).stream()
                .map(g-> g.getClass().getName() + ": " + g.identify())
                .collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(identityList);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
