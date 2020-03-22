package com.alibaba.druid.spring.boot.autoconfigure.interceptor;

/**
 * @author : linghan.ma
 * @Package com.alibaba.druid.spring.boot.autoconfigure.interceptor
 * @Description:
 * @date Date : 2020年03月22日 9:35 AM
 **/
import com.alibaba.druid.filter.stat.StatFilterContext;
import com.alibaba.druid.filter.stat.StatFilterContextListenerAdapter;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.support.spring.stat.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.TargetSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;

/**
 * Created by magic~ on 2018/9/17.
 */
public class SomeStatInterceptor implements MethodInterceptor, InitializingBean, DisposableBean
{

    public static final String          PROP_NAME_PROFILE   = "druid.profile";

    private final static Log            LOG                 = LogFactory.getLog(SomeStatInterceptor.class);

    private final static Log SERVICE_LOG_MONITOR      = LogFactory.getLog("serviceMonitor");

    private static SpringStat           springStat          = new SpringStat();

    private SomeStatInterceptor.SpringMethodContextListener statContextListener = new SomeStatInterceptor.SpringMethodContextListener();


    private String projectName;
    private long slowServiceMillis;

    public SomeStatInterceptor(){

    }

    public SomeStatInterceptor(String projectName, long slowServiceMillis){
        this.projectName=projectName;
        this.slowServiceMillis=slowServiceMillis;
    }

    public static String getPropNameProfile() {
        return PROP_NAME_PROFILE;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getSlowServiceMillis() {
        return slowServiceMillis;
    }

    public void setSlowServiceMillis(long slowServiceMillis) {
        this.slowServiceMillis = slowServiceMillis;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringStatManager.getInstance().addSpringStat(springStat);

        StatFilterContext.getInstance().addContextListener(statContextListener);
    }

    @Override
    public void destroy() throws Exception {
        StatFilterContext.getInstance().removeContextListener(statContextListener);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        SpringMethodStat lastMethodStat = SpringMethodStat.current();

        SpringMethodInfo methodInfo = getMethodInfo(invocation);

        SpringMethodStat methodStat = springStat.getMethodStat(methodInfo, true);

        if (methodStat != null) {
            methodStat.beforeInvoke();
        }

        long startNanos = System.nanoTime();

        Throwable error = null;
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            long endNanos = System.nanoTime();

            long nanos = endNanos - startNanos;

            final long time=nanos/(1000*1000);
            if (methodStat!=null&&lastMethodStat!=null&&time>=slowServiceMillis)
            {
                try
                {
                    StringBuffer  stringBuffer=new StringBuffer();
                    stringBuffer.append(projectName).append(">>>")
                            .append("methodName:")
                            .append(methodStat.getMethodInfo().getClassName())
                            .append(".")
                            .append(methodStat.getMethodInfo().getMethodName())
                            .append(",")
                            .append("concurrentMax:")
                            .append(methodStat.getConcurrentMax())
                            .append(",")
                            .append("jdbcExecuteTime:")
                            .append("0")
                            .append(",")
                            .append("time:")
                            .append(time);
                    SERVICE_LOG_MONITOR.info(stringBuffer.toString());
                } catch (Exception e)
                {
                    LOG.error("service_log_monitor error",e);
                }
            }
            if (methodStat != null) {
                methodStat.afterInvoke(error, nanos);
            }

            SpringMethodStat.setCurrent(lastMethodStat);
        }
    }

    public SpringMethodInfo getMethodInfo(MethodInvocation invocation) {
        Object thisObject = invocation.getThis();
        Method method = invocation.getMethod();

        if (thisObject == null) {
            return new SpringMethodInfo(method.getDeclaringClass(), method);
        }

        if (method.getDeclaringClass() == thisObject.getClass()) {
            return new SpringMethodInfo(method.getDeclaringClass(), method);
        }

        {
            Class<?> clazz = thisObject.getClass();
            boolean isCglibProxy = false;
            boolean isJavassistProxy = false;
            for (Class<?> item : clazz.getInterfaces()) {
                if (item.getName().equals("net.sf.cglib.proxy.Factory")) {
                    isCglibProxy = true;
                    break;
                } else if (item.getName().equals("javassist.util.proxy.ProxyObject")) {
                    isJavassistProxy = true;
                    break;
                }
            }

            if (isCglibProxy || isJavassistProxy) {
                Class<?> superClazz = clazz.getSuperclass();

                return new SpringMethodInfo(superClazz, method);
            }
        }

        Class<?> clazz = null;
        try {
            // 最多支持10层代理
            for (int i = 0; i < 10; ++i) {
                if (thisObject instanceof org.springframework.aop.framework.Advised) {
                    TargetSource targetSource = ((org.springframework.aop.framework.Advised) thisObject).getTargetSource();

                    if (targetSource == null) {
                        break;
                    }

                    Object target = targetSource.getTarget();
                    if (target != null) {
                        thisObject = target;
                    } else {
                        clazz = targetSource.getTargetClass();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOG.error("getMethodInfo error", ex);
        }

        if (clazz == null) {
            return new SpringMethodInfo(method.getDeclaringClass(), method);
        }

        return new SpringMethodInfo(clazz, method);
    }

    class SpringMethodContextListener extends StatFilterContextListenerAdapter {

        @Override
        public void addUpdateCount(int updateCount) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcUpdateCount(updateCount);
            }
        }

        @Override
        public void addFetchRowCount(int fetchRowCount) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcFetchRowCount(fetchRowCount);
            }
        }

        @Override
        public void executeBefore(String sql, boolean inTransaction) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcExecuteCount();
            }
        }

        @Override
        public void executeAfter(String sql, long nanos, Throwable error) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcExecuteTimeNano(nanos);
                if (error != null) {
                    springMethodStat.incrementJdbcExecuteErrorCount();
                }
            }
        }

        @Override
        public void commit() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcCommitCount();
            }
        }

        @Override
        public void rollback() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcRollbackCount();
            }
        }

        @Override
        public void pool_connect() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcPoolConnectionOpenCount();
            }
        }

        @Override
        public void pool_close(long nanos) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcPoolConnectionCloseCount();
            }
        }

        @Override
        public void resultSet_open() {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcResultSetOpenCount();
            }
        }

        @Override
        public void resultSet_close(long nanos) {
            SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcResultSetCloseCount();
            }
        }
    }
}
