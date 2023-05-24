package com.xiangjun.service;

import com.xiangjun.spring.BeanPostProcessor;
import com.xiangjun.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author leelixiangjun
 * @date 2023/5/24 22:34
 */
@Component
public class XiangjunBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialization(String beanName, Object bean) {
        System.out.println(beanName + "的初始化前后置处理器");
        if ("userService".equals(beanName)) {
            System.out.println("userService 初始化前 后置操作");
        }
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(String beanName, Object bean) {
        System.out.println(beanName + "的初始化后后置处理器");
        if ("userService".equals(beanName)) {
            System.out.println("userService 初始化前 后置操作");
            Object newProxyInstance = Proxy.newProxyInstance(XiangjunBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println(beanName+"的"+method.getName()+"方法的切面逻辑");
                    return method.invoke(bean, args);
                }
            });
            return newProxyInstance;
        }
        return bean;
    }
}
