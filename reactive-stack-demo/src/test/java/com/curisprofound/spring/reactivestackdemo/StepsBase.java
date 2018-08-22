package com.curisprofound.spring.reactivestackdemo;

import com.curisprofound.spring.reactivestackdemo.db.Author;
import com.curisprofound.spring.reactivestackdemo.db.Book;
import com.curisprofound.spring.reactivestackdemo.db.Publisher;
import org.pf4j.PluginManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

@ContextConfiguration(classes = ReactiveStackDemoApplication.class)
@SpringBootTest
@ComponentScan(basePackages = "com.curisprofound.spring.reactivestackdemo")
public class StepsBase extends com.curisprofound.test.StepsBase {
    @Override
    public void setClassNames(Map<String, Class<?>> classNames){
        classNames.put("Book", Book.class);
        classNames.put("ReactiveMongoRepository", ReactiveMongoRepository.class);
        classNames.put("Author", Author.class);
        classNames.put("Publisher", Publisher.class);
        classNames.put("ServerResponse", ServerResponse.class);
        classNames.put("RouterFunction", RouterFunction.class);
        classNames.put("PluginManager", PluginManager.class);
    }
}

