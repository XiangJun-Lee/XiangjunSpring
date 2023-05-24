package com.xiangjun.service;

import com.xiangjun.spring.*;

/**
 * @author leelixiangjun
 * @date 2023/5/22 23:04
 */
@Component
public class UserService implements BeanNameAware, InitializingBean, UserInterface {

    @Autowired
    private OrderService orderService;

    private String beanName;

    @Override
    public void test(){
        System.out.println(this.orderService);

    }

    @Override
    public void test2() {
        System.out.println(this.beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("userService Aware回调");
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {

    }
}
