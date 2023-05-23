package com.xiangjun.spring;

/**
 * @author leelixiangjun
 * @date 2023/5/23 23:48
 */
public class BeanDefinition {

    private Class type;

    private String socpe;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getSocpe() {
        return socpe;
    }

    public void setSocpe(String socpe) {
        this.socpe = socpe;
    }
}
