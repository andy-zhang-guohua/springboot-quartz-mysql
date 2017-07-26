2017-07-26

### 学习 SpringBoot + Spring MVC + Quartz + MySQL

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
* 4. 运行 Application : Spring Boot工程已经集成了服务器。右键点击DemoApplication.java -> Run As -> Java Application即可运行工程。
* 5. 默认端口为8080，启动后在浏览器地址栏输入 http://localhost:8080/JobManager.html 就可以看到效果
* 6. 增加任务 : 任务名称要求是类名，包括包名


参考文档 :
http://blog.csdn.net/u012907049/article/details/70273080
http://blog.csdn.net/u012907049/article/details/73801122

本项目基于以上文档改造