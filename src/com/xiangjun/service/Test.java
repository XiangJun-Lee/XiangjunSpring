package com.xiangjun.service;

import com.xiangjun.spring.XiangjunApplicationContext;

/**
 * @author leelixiangjun
 * @date 2023/5/22 22:59
 */
public class Test {
    public static void main(String[] args) {
        XiangjunApplicationContext applicationContext = new XiangjunApplicationContext(ApplicationConfig.class);

        UserInterface userService = (UserInterface) applicationContext.getBean("userService");
        System.out.println("----------------------------------------");
        userService.test();
        userService.test2();
    }
}
