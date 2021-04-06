package com.example.reports.quartzconfig;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReportsQuartzJob extends QuartzJobBean {

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        try
        {
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
            jobLocator = applicationContext.getBean(JobLocator.class);
            jobLauncher = applicationContext.getBean(JobLauncher.class);
            Job job = jobLocator.getJob(jobName);
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
//            Map<String, JobParameter> map = new HashMap<>();
//            map.put("time", new JobParameter(System.currentTimeMillis()));
//            JobParameters jobParameters = new JobParameters(map);
//            Job job = (Job)applicationContext.getBean("demoJobOne");
//            jobLauncher = applicationContext.getBean(JobLauncher.class);
            jobLauncher.run(job, params);
        }
        catch (Exception e){
            e.printStackTrace();
        }

//        Map<String, JobParameter> map = new HashMap<>();
//        map.put("time", new JobParameter(System.currentTimeMillis()));
//        JobParameters jobParameters = new JobParameters(map);
//        try {
//            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
//            while (jobExecution.isRunning()){
//            }
//        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
//            e.printStackTrace();
//        }
    }
}
