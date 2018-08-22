package com.curisprofound.spring.reactivestackdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.ipc.netty.NettyContext;

@Configuration
@EnableWebFlux
@ComponentScan
@SpringBootApplication
public class ReactiveStackDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveStackDemoApplication.class, args);
//		try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
//				ReactiveStackDemoApplication.class)) {
//			context.getBean(NettyContext.class).onClose().block();
//		}
	}
}
