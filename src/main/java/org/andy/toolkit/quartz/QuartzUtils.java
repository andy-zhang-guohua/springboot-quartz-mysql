package org.andy.toolkit.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by ZhangGuohua on 2017/7/26.
 */
public final class QuartzUtils {
    private static Scheduler scheduler;

    /**
     * 外部设置该工具类使用的 Scheduler 实例
     *
     * @param scheduler
     */
    public static synchronized void setScheduler(Scheduler scheduler) {
        QuartzUtils.scheduler = scheduler;
    }

    /**
     * 获取该工具类使用的 Scheduler 实例，如果外部没有设置该工具的此属性，该工具类内部先从 StdSchedulerFactory 创建一个 Scheduler 到此属性字段
     *
     * @return
     */
    public static Scheduler getScheduler() {
        if (scheduler == null) {
            synchronized (QuartzUtils.class) {// 如果 scheduler 为空尝试锁住当前类然后初始化其 scheduler 属性
                if (scheduler == null) { // 如果锁定当前类之后 scheduler 仍然为空，说明已经安全排斥了其他其他同步方法同时设置该属性的可能性，下面对该属性 scheduler 进行设置
                    scheduler = createStdScheduler();
                }
            }
        }

        return scheduler;
    }

    private static Scheduler createStdScheduler() {
        try {
            // 配置文件的加载规则 :
            // 1. 尝试 system.environment 中配置属性 org.quartz.properties 作为配置文件
            // 2. 尝试读取当前目录的文件 quartz.properties 作为配置文件
            // 3. 尝试使用当前 classLoader.getResourceAsStream("quartz.properties") 作为配置文件
            // 4. 尝试使用当前 classLoader.getResourceAsStream("/quartz.properties") 作为配置文件
            // 5. 如果以上尝试均失败，使用 quartz jar 包中自己提供的缺省配置 org/quartz/quartz.properties, 这个缺省配置会使用 RAMStore 保存信息
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            return scheduler;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
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
            Scheduler scheduler = getScheduler();
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
        Scheduler scheduler = getScheduler();
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
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
        Scheduler scheduler = getScheduler();

        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);

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
        Scheduler scheduler = getScheduler();
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    /**
     * 继续调度一个Job
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public static void jobResume(String jobClassName, String jobGroupName) throws Exception {
        Scheduler scheduler = getScheduler();
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

}
