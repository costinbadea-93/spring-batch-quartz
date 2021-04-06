package com.example.reports.batchconfig;

import com.example.reports.animal.Animal;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;



@Component
public class ReportsProcessor implements ItemProcessor<Animal, Animal> {

    @Override
    public Animal process(Animal animal) throws Exception {
        return animal;
    }
}
