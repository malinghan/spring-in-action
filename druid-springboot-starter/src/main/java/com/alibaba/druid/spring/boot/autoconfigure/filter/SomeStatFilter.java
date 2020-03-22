package com.alibaba.druid.spring.boot.autoconfigure.filter;

/**
 * @author : linghan.ma
 * @Package com.alibaba.druid.spring.boot.autoconfigure.filter
 * @Description:
 * @date Date : 2020年03月22日 9:32 AM
 **/

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * Created by magic~ on 2018/9/17.
 */
public class SomeStatFilter extends StatFilter
{
    private final static Log LOG = LogFactory.getLog(SomeStatFilter.class);

    private final static Log SQL_LOG_MONITOR = LogFactory.getLog("sqlMonitor");

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public SomeStatFilter()
    {
    }

    public SomeStatFilter(String projectName)
    {
        this.projectName=projectName;
    }
    @Override
    protected void handleSlowSql(StatementProxy statementProxy)
    {
        try
        {

            final long nanos=statementProxy.getLastExecuteTimeNano();
            long  millis = nanos / (1000 * 1000);
            if (millis >= slowSqlMillis) {
                String slowParameters = buildSlowParameters(statementProxy);
                String lastExecSql = statementProxy.getLastExecuteSql();

                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(projectName).append(">>>")
                        .append("slowSql:")
                        .append(lastExecSql)
                        .append(slowParameters)
                        .append(",")
                        .append("time:")
                        .append(millis);
                SQL_LOG_MONITOR.info(stringBuffer.toString());
            }
        } catch (Exception e)
        {
            LOG.error("sql_log_monitor error",e);
        }

    }
}