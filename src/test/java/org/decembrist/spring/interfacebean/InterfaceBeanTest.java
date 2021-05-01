package org.decembrist.spring.interfacebean;

import org.decembrist.spring.interfacebean.correctTest.ComponentInterfaceBean;
import org.decembrist.spring.interfacebean.correctTest.InterfaceBean;
import org.decembrist.spring.interfacebean.correctTest.InterfaceBean2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;

public class InterfaceBeanTest {

    @Test
    public void correctTest() {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("org.decembrist.spring.interfacebean.correctTest");
        Object interfaceBean1 = context.getBean(InterfaceBean.class);
        Object interfaceBean2 = context.getBean(InterfaceBean2.class);
        Object interfaceBean3 = context.getBean(ComponentInterfaceBean.class);

        Assertions.assertNotNull(interfaceBean1);
        Assertions.assertNotNull(interfaceBean2);
        Assertions.assertNotNull(interfaceBean3);
        Assertions.assertEquals(interfaceBean1.getClass(), InterfaceBean.class);
        Assertions.assertEquals(interfaceBean2.getClass(), InterfaceBean2.class);
        Assertions.assertEquals(interfaceBean3.getClass(), ComponentInterfaceBean.class);
    }

    @Test
    public void singletonTest() {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("org.decembrist.spring.interfacebean.correctTest");
        Object interfaceBean1 = context.getBean(InterfaceBean.class);
        Object interfaceBean2 = context.getBean(InterfaceBean.class);
        Assertions.assertEquals(interfaceBean1, interfaceBean2);
    }

    @Test
    public void propertyOkTest() {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        MutablePropertySources propertySources = new MutablePropertySources();
        HashMap<String, Object> source = new HashMap<>();
        source.put("spring.interface-bean", "true");
        PropertySource propertySource = new PropertySource("jopa", source) {
            @Override
            public Object getProperty(String name) {
                if ("spring.interface-bean".equals(name)) {
                    return "true";
                }
                return null;
            }
        };
        propertySources.addFirst(propertySource);
        StandardEnvironment spyEnv = new StandardEnvironment(propertySources) {
        };
        context.setEnvironment(spyEnv);
        context.scan("org.decembrist.spring.interfacebean.correctTest");
        context.refresh();
        context.getBean(InterfaceBean.class);
    }

    @Test
    public void propertyBadTest() {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        MutablePropertySources propertySources = new MutablePropertySources();
        HashMap<String, Object> source = new HashMap<>();
        source.put("spring.interface-bean", "false");
        PropertySource propertySource = new PropertySource("jopa", source) {
            @Override
            public Object getProperty(String name) {
                if ("spring.interface-bean".equals(name)) {
                    return "false";
                }
                return null;
            }
        };
        propertySources.addFirst(propertySource);
        StandardEnvironment spyEnv = new StandardEnvironment(propertySources) {
        };
        context.setEnvironment(spyEnv);
        context.scan("org.decembrist.spring.interfacebean.correctTest");
        context.refresh();
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(InterfaceBean.class));
    }

}
