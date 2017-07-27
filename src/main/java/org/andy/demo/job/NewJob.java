package org.andy.demo.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@Slf4j
public class NewJob implements BaseJob {
    public NewJob() {

    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("New Job执行时间: " + new Date());

        log.info("JobDataMap 参数 : {}", context.getJobDetail().getJobDataMap().getWrappedMap());
    }
}  