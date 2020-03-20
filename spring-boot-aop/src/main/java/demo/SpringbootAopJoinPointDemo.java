package demo;

import controller.AspectController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author : linghan.ma
 * @Package demo
 * @Description:
 * @date Date : 2020年03月12日 1:07 AM
 **/
@SpringBootApplication
public class SpringbootAopJoinPointDemo {

    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        ApplicationContext applicationContext = SpringApplication.run(SpringbootAopJoinPointDemo.class, args);
        AspectController aspectController = applicationContext.getBean(AspectController.class);
        // 依赖查找集合对象
        aspectController.advice();
    }
}
