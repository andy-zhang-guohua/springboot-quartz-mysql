package org.andy.demo.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzSchedulerConfig {

    /**
     * 定义 SchedulerFactoryBean bean，名字为 schedulerFactoryBean, 注入两个属性 :
     * 1. dataSource -- 该属性来自 spring 配置文件定义的数据源
     * 2. quartzProperties -- 该属性来自另外一个Bean quartzProperties 定义的 quartz 配置
     *
     * @param dataSource
     * @param quartzProperties
     * @return
     * @throws Exception
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, Properties quartzProperties) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties);// 使用 quartz 配置文件

        // 使用 spring 数据源代替 quartz 配置文件 中定义的数据源， 注意，这里如果 quartz 配置中的数据源和 spring 数据源如果不同，
        // 注释掉改行可以起到让应用的业务数据 和 仅仅跟 quartz 调度相关的调度数据 使用不同的数据源的效果 。
        //factory.setDataSource(dataSource);

        return factory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException {
        return schedulerFactoryBean.getScheduler();
    }

    /**
     * 定义 Quartz 属性配置 bean，名称为 quartzProperties
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();// 注意 : 这一句必须要调用，否则配置文件并不会被加载，下面的 getObject() 会返回 null
        Properties props = propertiesFactoryBean.getObject();
        return props;
    }

    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

}
