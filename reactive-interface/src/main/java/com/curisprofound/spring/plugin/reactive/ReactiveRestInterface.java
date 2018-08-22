package com.curisprofound.spring.plugin.reactive;

import com.curisprofound.spring.plugin.Pf4jPlugin;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;

public interface ReactiveRestInterface extends Pf4jPlugin {

    List<RouterFunction<?>> routerFunctions();
    List<Object> annotatedHandlers();

}
