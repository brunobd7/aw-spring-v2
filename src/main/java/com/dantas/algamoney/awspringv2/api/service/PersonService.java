package com.dantas.algamoney.awspringv2.api.service;

import com.dantas.algamoney.awspringv2.api.model.Person;
import com.dantas.algamoney.awspringv2.api.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    public Person updatePerson(Person personInput,Long personId) {

        Person personFounded = findPersonById(personId);

        BeanUtils.copyProperties(personInput, personFounded, "id");

        personFounded = repository.save(personFounded);

        return personFounded;
    }

    public Person updatePersonStatus(Boolean isActive, Long personId){
        Person personFounded = findPersonById(personId);
        personFounded.setIsActive(isActive);
        return repository.save(personFounded);
    }

    private Person findPersonById(Long personId) {
        return repository.findById(personId).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

}
