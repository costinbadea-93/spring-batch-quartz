package com.example.reports.batchconfig;

import com.example.reports.animal.Animal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportWriter implements ItemWriter<Animal> {

    private static final Logger logger = LoggerFactory.getLogger(ReportWriter.class);

    @Override
    public void write(List<? extends Animal> animals){
        logger.warn("generating report for data {}", animals);
    }
}
