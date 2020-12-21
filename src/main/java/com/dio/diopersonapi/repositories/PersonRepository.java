package com.dio.diopersonapi.repositories;

import com.dio.diopersonapi.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
