package com.example.reports.repository;

import com.example.reports.animal.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal,Integer> {
}
