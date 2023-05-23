package com.xiangjun.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leelixiangjun
 * @date 2023/5/22 22:59
 */
public class XiangjunApplicationContext {
    private final Class configClass;

    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // 单例池
    private ConcurrentHashMap<String, Object> singletonBeanMap = new ConcurrentHashMap<>();

    public XiangjunApplicationContext(Class configClass) {
        this.configClass = configClass;

        /**
         *  包扫描，将所有的bean定义为beanDefinition对象，存储到map中
         */
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
                                // 9. 创建BeanDefinition对象
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    beanDefinition.setSocpe(scopeAnnotation.value());
                                } else {
                                    beanDefinition.setSocpe("singleton");
                                }
                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();
                                if (beanName.equals("")) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        /**
         * 实例化单例bean
         */
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getSocpe().equals("singleton")) {
                Object o = this.createBean(beanName, beanDefinition);
                singletonBeanMap.put(beanName, o);
            }
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        /**
         * bean的生命周期
         */
        try {
            // 实例化
            Object instance = clazz.getConstructor().newInstance();

            // 依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }

            // Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                // 不关注具体做什么，只是在初始化的时候，调用这个方法
                ((InitializingBean) instance).afterPropertiesSet();
            }


            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        }
        String socpe = beanDefinition.getSocpe();
        if (socpe.equals("singleton")) {
            Object bean = singletonBeanMap.get(beanName);
            // 如果getBean时还未加载
            if (bean == null) {
                Object o = this.createBean(beanName, beanDefinition);
                singletonBeanMap.put(beanName, o);
                return o;
            }
            return bean;
        }
        return createBean(beanName, beanDefinition);
    }
}
