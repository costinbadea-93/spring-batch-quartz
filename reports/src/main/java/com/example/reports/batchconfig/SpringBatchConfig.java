package com.example.reports.batchconfig;

import com.example.reports.animal.Animal;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean(name = "reportsGenerationJob")
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Animal> itemReader,
                   ItemProcessor<Animal, Animal> itemProcessor,
                   ItemWriter<Animal> itemWriter) {

        Step step = stepBuilderFactory.get("reportsGenerationJobStep")
                .<Animal,Animal>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("reportsGenerationJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

    }



    @Bean
    public ItemReader<Animal> itemReader(){
        JpaCursorItemReader<Animal> itemReader = new JpaCursorItemReader<>();
        itemReader.setQueryString("SELECT a FROM Animal a");
        itemReader.setEntityManagerFactory(entityManagerFactory);
        return itemReader;
    }


}
