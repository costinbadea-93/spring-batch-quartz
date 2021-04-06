package com.example.reports.quartzconfig;

import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfig {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobLocator jobLocator;

    public QuartzConfig() {
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public JobDetail jobDetail(){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "reportsGenerationJob");
        return JobBuilder.newJob(ReportsQuartzJob.class)
                .withIdentity("jobDetails",null)
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobDetailTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(3)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail())
                .withIdentity("AnimalJobReportTrigger",null)
                .withSchedule(scheduleBuilder)
                .build();
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException, SchedulerException
    {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(jobDetailTrigger());
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setJobDetails(jobDetail());
        scheduler.setApplicationContextSchedulerContextKey("applicationContext");
        return scheduler;
    }

    public Properties quartzProperties() throws IOException
    {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
