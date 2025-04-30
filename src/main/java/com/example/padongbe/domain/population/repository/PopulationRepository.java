package com.example.padongbe.domain.population.repository;

import com.example.padongbe.domain.population.entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopulationRepository extends JpaRepository<Population, Long> {

}
