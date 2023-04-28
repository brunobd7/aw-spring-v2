package com.dantas.algamoney.awspringv2.api.service;

import com.dantas.algamoney.awspringv2.api.exception.InvalidOrInactivePersonException;
import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.model.Person;
import com.dantas.algamoney.awspringv2.api.repository.LaunchRepository;
import com.dantas.algamoney.awspringv2.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LaunchService {


    @Autowired
    private LaunchRepository repository;

    @Autowired
    private PersonRepository personRepository;

    public Launch saveLaunch(Launch launch){

        Person personOfLaunch = personRepository.findById(launch.getPerson().getId())
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        if(!personOfLaunch.getIsActive())
            throw new InvalidOrInactivePersonException();

        return repository.save(launch);
    }



}
