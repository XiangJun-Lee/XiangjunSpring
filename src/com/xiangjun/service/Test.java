package com.xiangjun.service;

import com.xiangjun.spring.XiangjunApplicationContext;

/**
 * @author leelixiangjun
 * @date 2023/5/22 22:59
 */
public class Test {
    public static void main(String[] args) {
        XiangjunApplicationContext applicationContext = new XiangjunApplicationContext(ApplicationConfig.class);

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
