package com.xiangjun.spring;

/**
 * @author leelixiangjun
 * @date 2023/5/24 22:33
 */
public interface BeanPostProcessor {

    Object postProcessorBeforeInitialization(String beanName, Object bean);

    Object postProcessorAfterInitialization(String beanName, Object bean);

}
