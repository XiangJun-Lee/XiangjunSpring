package com.xiangjun.service;

import com.xiangjun.spring.*;

/**
 * @author leelixiangjun
 * @date 2023/5/22 23:04
 */
@Component
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test(){
        System.out.println(this.orderService);
        System.out.println(this.beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {

    }
}
