package com.curisprofound.spring.reactivestackdemo.Config;

import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockPostProcessor implements BeanPostProcessor {

    public final List<Class> classes = new ArrayList<>();

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        classes.add(org.pf4j.PluginManager.class);

        return classes.stream()
                .filter(c -> c.isInstance(bean))
                .map(c-> ComponentMocker.MockComponent(c,bean))
                .findAny()
                .orElse(bean);
    }
}
