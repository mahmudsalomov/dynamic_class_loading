package uz.maniac4j.dynamic_class_loading.dynamic;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicSpringBeanRegistrar {
    public static void registerBean(ConfigurableApplicationContext context, Class<?> clazz) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(clazz);

        registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
    }
}
