package com.someecho.springbucks.repository;

import com.someecho.springbucks.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
