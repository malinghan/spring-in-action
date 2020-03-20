package com.someecho.joinpoint;

import com.someecho.joinpoint.aspect.MainAspect;
import com.someecho.joinpoint.manager.AdviceManager;
import com.someecho.joinpoint.manager.impl.AdviceManagerImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author : linghan.ma
 * @Package com.someecho.joinpoint
 * @Description:
 * @date Date : 2020年03月12日 7:59 PM
 **/
@Configuration
@EnableAspectJAutoProxy
public class SpringAopDemo {

    @Bean
    MainAspect getMainAspect(){
        return new MainAspect();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringAopDemo.class);
         context.register(AdviceManagerImpl.class);
        context.register(MainAspect.class);
        /**
         *
         * https://blog.csdn.net/kuaisuzhuceh/article/details/44756333
         *
         * 对于Spring AOP 采用两种代理方法，一种是常规JDK，一种是CGLIB，
         * 我的UserDao了一个接口IUserDao，当代理对象实现了至少一个接口时，默认使用JDK动态创建代理对象，
         * 当代理对象没有实现任何接口时，就会使用CGLIB方法。
         *
         * 如果你的代理对象没有实现接口的方法，就将代理对象转换成接口。
         */
        AdviceManager adviceManager =(AdviceManager) context.getBean("adviceManagerImpl");
        adviceManager.print("hello");
        //Exception in thread "main" java.lang.ClassCastException: com.sun.proxy.$Proxy26 cannot be cast to
        // com.someecho.joinpoint.manager.impl.AdviceManagerImpl
        //	at com.someecho.joinpoint.SpringAopDemo.main(SpringAopDemo.java:40)
    }
}
