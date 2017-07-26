package com.example.demo.controller;

import com.example.demo.entity.JobAndTrigger;
import com.example.demo.job.BaseJob;
import com.example.demo.service.IJobAndTriggerService;
import com.github.pagehelper.PageInfo;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/job")
public class JobController {
    @Autowired
    IJobAndTriggerService iJobAndTriggerService;

    @Autowired
    Scheduler scheduler;

    private static Logger log = LoggerFactory.getLogger(JobController.class);


    @PostMapping(value = "/addjob")
    public void addjob(@RequestParam(value = "jobClassName") String jobClassName,
                       @RequestParam(value = "jobGroupName") String jobGroupName,
                       @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        addJob(jobClassName, jobGroupName, cronExpression);
    }

    public static void addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {

        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        addJob(scheduler, jobClassName, jobGroupName, cronExpression);
    }

    public static void addJob(Scheduler sched, String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        // 启动调度器  
        sched.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).build();

        try {
            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败" + e);
            throw new Exception("创建定时任务失败");
        }
    }


    @PostMapping(value = "/pausejob")
    public void pausejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        jobPause(jobClassName, jobGroupName);
    }

    public static void jobPause(String jobClassName, String jobGroupName) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例  
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @PostMapping(value = "/resumejob")
    public void resumejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        jobresume(jobClassName, jobGroupName);
    }

    public static void jobresume(String jobClassName, String jobGroupName) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @PostMapping(value = "/reschedulejob")
    public void rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
                              @RequestParam(value = "jobGroupName") String jobGroupName,
                              @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        jobreschedule(jobClassName, jobGroupName, cronExpression);
    }

    public static void jobreschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败" + e);
            throw new Exception("更新定时任务失败");
        }
    }


    @PostMapping(value = "/deletejob")
    public void deletejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        jobdelete(jobClassName, jobGroupName);
    }

    public static void jobdelete(String jobClassName, String jobGroupName) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        sched.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        sched.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @GetMapping(value = "/queryjob")
    public Map<String, Object> queryjob(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        PageInfo<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        return map;
    }

    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }


}

