package org.decembrist.spring.interfacebean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * Scans bean definitions to find classes with {@link org.decembrist.spring.interfacebean.IBean} annotations
 * registers it as bean
 */
@Component
@ConditionalOnProperty(prefix = "spring", value = "interface-bean", havingValue = "true", matchIfMissing = true)
public class InterfaceBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private BeanDefinitionRegistry registry;

    private boolean init = false;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        loadIBeans(beanFactory);
    }

    private void loadIBeans(BeanFactory beanFactory) throws BeansException {
        if (registry == null) {
            throw new InterfaceBeanException("BeanDefinitionRegistry must not be null");
        }
        if (!init) {
            ClassPathScanningCandidateComponentProvider scanner = setUpScanner();
            List<String> basePackages = AutoConfigurationPackages.get(beanFactory);
            basePackages.stream()
                    .map(scanner::findCandidateComponents)
                    .flatMap(Collection::stream)
                    .forEach(definition -> registry.registerBeanDefinition(definition.getBeanClassName(), definition));
            init = true;
        }
    }

    /**
     * Exclude classes with stereotype annotations from scanning
     */
    private ClassPathScanningCandidateComponentProvider setUpScanner() {
        ClassPathScanningCandidateComponentProvider scanner
                = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(IBean.class));
        scanner.addExcludeFilter(new AnnotationTypeFilter(Component.class));
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            scanner.addExcludeFilter(new AnnotationTypeFilter(
                    ((Class<? extends Annotation>) ClassUtils.forName("javax.annotation.ManagedBean", classLoader)),
                    false));
        } catch (ClassNotFoundException ex) {
            // JSR-250 1.1 API (as included in Java EE 6) not available - simply skip.
        }
        try {
            scanner.addExcludeFilter(new AnnotationTypeFilter(
                    ((Class<? extends Annotation>) ClassUtils.forName("javax.inject.Named", classLoader)),
                    false));
        }
        catch (ClassNotFoundException ex) {
            // JSR-330 API not available - simply skip.
        }

        return scanner;
    }
}
