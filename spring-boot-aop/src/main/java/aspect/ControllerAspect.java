package aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * @author : linghan.ma
 * @Package aspect
 * @Description:
 * @date Date : 2020年03月12日 1:09 AM
 **/
public class ControllerAspect {


    @Pointcut("execution(* controller.AspectController.advice())")
    public void pointCut(){}

    @Before("pointCut()")
    public void permissionCheck(JoinPoint point) {
        System.out.println("@Before：模拟权限检查...");
        System.out.println("@Before：目标方法为：" +
                point.getSignature().getDeclaringTypeName() +
                "." + point.getSignature().getName());
        System.out.println("@Before：参数为：" + Arrays.toString(point.getArgs()));
        System.out.println("@Before：被织入的目标对象为：" + point.getTarget());
    }
}
