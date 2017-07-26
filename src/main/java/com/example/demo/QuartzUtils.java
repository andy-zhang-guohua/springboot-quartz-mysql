package com.example.demo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by ZhangGuohua on 2017/7/26.
 */
public final class QuartzUtils {
    private static SchedulerFactory schedulerFactory;

    /**
     * 外部设置该工具类使用的 SchedulerFactory 实例
     *
     * @param schedulerFactory
     */
    public static void setSchedulerFactory(SchedulerFactory schedulerFactory) {
        QuartzUtils.schedulerFactory = schedulerFactory;
    }

    /**
     * 获取该工具类使用的 SchedulerFactory 实例，如果外部没有设置该工具的此属性，该工具类内部先从 StdSchedulerFactory 创建一个 SchedulerFactory到此属性字段
     *
     * @return
     */
    public static SchedulerFactory getSchedulerFactory() {
        if (schedulerFactory == null) {
            schedulerFactory = new StdSchedulerFactory();
        }

        return schedulerFactory;
    }

    /**
     * 重新调度一个 cron job
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    public static void jobReschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        try {
            SchedulerFactory schedulerFactory = getSchedulerFactory();
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

    /**
     * 从调度器中删除一个Job
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public static void jobDelete(String jobClassName, String jobGroupName) throws Exception {
        SchedulerFactory sf = getSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        sched.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        sched.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    /**
     * 增加一个 Job 到调度器
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    public static void addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        SchedulerFactory sf = getSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        // 启动调度器
        sched.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder).build();

        try {
            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败" + e);
            throw new Exception("创建定时任务失败");
        }
    }

    public static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }


    /**
     * 暂停调度一个Job
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public static void jobPause(String jobClassName, String jobGroupName) throws Exception {
        SchedulerFactory sf = getSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    /**
     * 继续调度一个Job
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public static void jobResume(String jobClassName, String jobGroupName) throws Exception {
        SchedulerFactory sf = getSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

}
