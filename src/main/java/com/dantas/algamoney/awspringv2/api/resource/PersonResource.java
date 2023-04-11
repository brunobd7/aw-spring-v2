package com.dantas.algamoney.awspringv2.api.resource;

import com.dantas.algamoney.awspringv2.api.event.ResourceCreatedEvent;
import com.dantas.algamoney.awspringv2.api.model.Person;
import com.dantas.algamoney.awspringv2.api.repository.PersonRepository;
import com.dantas.algamoney.awspringv2.api.service.PersonService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/person")
public class PersonResource {


    @Autowired
    private PersonRepository repository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PersonService service;


    @GetMapping
    public ResponseEntity<List<Person>> listAllPeople() {
        return ResponseEntity.ok(repository.findAll());
    }


    @PostMapping
    public ResponseEntity<Person> createPersonRegister(@Valid @RequestBody Person person, HttpServletResponse response) {

        Person personCreated = repository.save(person);

        eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, personCreated.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{personId}")
    public ResponseEntity<Person> findPersonById(@PathVariable Long personId) {

        Person founded = repository.findById(personId).orElse(null);

        return Objects.isNull(founded) ? ResponseEntity.noContent().build() : ResponseEntity.ok(founded);
    }

    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Long personId) {
        repository.deleteById(personId);
    }


    @PutMapping("/{personId}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long personId, @Valid @RequestBody Person person){

        Person personUpdated = service.updatePerson(person,personId);
        return ResponseEntity.ok(personUpdated);
    }

    @PutMapping("/{personId}/isActive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePerson(@PathVariable Long personId, @RequestBody Boolean isActive){
        service.updatePersonStatus(isActive, personId);
    }

}
