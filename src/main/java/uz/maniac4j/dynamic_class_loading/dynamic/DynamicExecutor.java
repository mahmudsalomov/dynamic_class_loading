package uz.maniac4j.dynamic_class_loading.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class DynamicExecutor {

    @Autowired
    private ApplicationContext applicationContext;  // Spring's application context

    public Object executeMethod(Class<?> clazz, String methodName) throws Exception {
        //Object instance = clazz.getDeclaredConstructor(ApplicationContext.class).newInstance(applicationContext);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        return clazz.getMethod(methodName).invoke(instance);
    }

    public Object executeBeanMethod(Class<?> clazz, String methodName) throws Exception {
//        Object instance = clazz.getDeclaredConstructor(ApplicationContext.class).newInstance(applicationContext);
        Object bean = applicationContext.getBean(clazz);
        return clazz.getMethod(methodName).invoke(bean);
    }

    public Object executeMethodWithArg(Class<?> clazz, String methodName, Object arg) throws Exception {
        Object instance = clazz.getDeclaredConstructor().newInstance();

        Method method = clazz.getMethod(methodName, arg.getClass());

        return method.invoke(instance, arg);
    }



    public Object executeDynamicClassWithBeans(Class<?> clazz) throws Exception {

        Object instance = clazz.getDeclaredConstructor().newInstance();


//        UserService userService = applicationContext.getBean(UserService.class);
//
//
//        Method setMyServiceMethod = clazz.getMethod("setMyService", UserService.class);
//        setMyServiceMethod.invoke(instance, userService);

        Method executeMethod = clazz.getMethod("execute");
        Object result = executeMethod.invoke(instance);

        System.out.println("Result from dynamic class: " + result);
        return result;
    }
}