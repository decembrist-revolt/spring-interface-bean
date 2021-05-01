package org.decembrist.spring.interfacebean;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables context interface bean definition
 * All class with interface {@link org.decembrist.spring.interfacebean.IBean } become beans
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(InterfaceBeanPostProcessor.class)
public @interface EnableInterfaceBeanScan {
}
