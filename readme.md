2017-07-27
### 向Quartz的Job动态传递参数

#### 1. 在运行前传入参数： JobBuilder或者TriggerBuilder对象上调用 usingJobData().例如：      

<pre>      
     JobDetail jobDetail = JobBuilder.newJob(...).withIdentity(jobClassName, jobGroupName)
                    .usingJobData("order_id", 201707271745001L) // 参数 1
                    .usingJobData("user_name", "Tom") // 参数 2
                    .build();
</pre>



#### 2. 其次在job实现类逻辑中获得参数
######     2.1 在execute方法中，传入上下文context，如下所示：
<pre>      
        public void execute(JobExecutionContext context) 
</pre>      
######     2.2 再获得JobDataMap，从Map中获得所需数据，示例代码如下 ：
      
<pre>  
         String jobName = context.getJobDetail().getName();
         JobDataMap dataMap = context.getMergedJobDataMap();// JobDetails 和 Trigger 上分别设置的数据的合并
         String strData = dataMap.getString("love");
</pre>                  


2017-07-26

### 初始化创建 SpringBoot + Spring MVC + Quartz + MySQL 学习项目

#### 本工程所用到的技术或工具有：
* Spring Boot
* Mybatis
* Quartz
* PageHelper
* VueJS
* ElementUI
* MySql数据库


#### 如何运行本项目 :
* 1. 确保已经为该项目创建 MySQL 数据库
* 2. 在上面创建的数据库中运行doc/quartz.ddl中的SQL
* 3. 修改配置文件 quartz.properties , application.yml 中的数据库连接参数
* 4. 运行 Application : Spring Boot工程已经集成了服务器。右键点击 Application.java -> Run As -> Java Application即可运行工程。
* 5. 默认端口为8080，启动后在浏览器地址栏输入 http://localhost:8080/CronJobManager.html 就可以看到效果
* 6. 增加任务 : 任务名称要求是类名，包括包名


#### 参考文档 :
* http://blog.csdn.net/u012907049/article/details/70273080
* http://blog.csdn.net/u012907049/article/details/73801122

###### 本项目基于以上文档提供的项目改造

### 相关工具
* http://cron.qqe2.com/  -- 在线 cron 表达式生成工具


