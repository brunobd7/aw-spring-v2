package com.dantas.algamoney.awspringv2.api.repository;

import com.dantas.algamoney.awspringv2.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
