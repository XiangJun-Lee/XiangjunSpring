package com.xiangjun.spring;

import java.io.File;
import java.net.URL;

/**
 * @author leelixiangjun
 * @date 2023/5/22 22:59
 */
public class XiangjunApplicationContext {
    private Class configClass;

    public XiangjunApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描
        // 1. 判断类是否有 ComponentScan 注解
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            // 2. 拿到注解中的扫描路径
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String value = componentScanAnnotation.value();
            String path = value.replace(".", "/");
            // 3. 通过类加载器 获取 path 下的资源
            ClassLoader classLoader = XiangjunApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            // 4.将资源封装成file，并判断是否为文件夹
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                // 5. 获取路径下的所有文件
                File[] files = file.listFiles();
                for (File f : files) {
                    // 6. 判断文件是否为.class文件
                    String absolutePath = f.getAbsolutePath();
                    if (absolutePath.endsWith(".class")) {
                        try {
                            // 7. 拼装类的全限定名，通过类加载器对类进行加载
                            String className = value + "." + f.getName().replace(".class", "");
                            Class<?> clazz = classLoader.loadClass(className);
                            // 8. 判断加载的类是否有 Component 注解
                            if (clazz.isAnnotationPresent(Component.class)) {
                                // 9. 找到Bean对象
                                System.out.println(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        return null;
    }
}
