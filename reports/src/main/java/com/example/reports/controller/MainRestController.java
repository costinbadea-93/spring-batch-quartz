package com.example.reports.controller;

import com.example.reports.animal.Animal;
import com.example.reports.repository.AnimalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainRestController {
    private final AnimalRepository animalRepository;
    private final JobLauncher jobLauncher;
    private final Job job;

    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);

    public MainRestController(AnimalRepository animalRepository,
                              JobLauncher jobLauncher,
                              Job job) {
        this.animalRepository = animalRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/addNewAnimal")
    public ResponseEntity<Animal> addAnimal(@RequestBody Animal animal) {
        return ResponseEntity.ok().body(animalRepository.save(animal));
    }

    @GetMapping("/generateReports")
    public BatchStatus generateReports() {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(map);
        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            while (jobExecution.isRunning()){
                logger.warn("processing job ....");
            }
            return jobExecution.getStatus();
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return BatchStatus.FAILED;
    }
}
