package com.dantas.algamoney.awspringv2.api.resource;

import com.dantas.algamoney.awspringv2.api.model.Person;
import com.dantas.algamoney.awspringv2.api.repository.PersonRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/person/")
public class PersonResource {


    @Autowired
    private PersonRepository repository;


    @GetMapping
    public ResponseEntity<List<Person>> listAllPeople() {
        return ResponseEntity.ok(repository.findAll());
    }


    @PostMapping
    public ResponseEntity<Person> createPersonRegister(@Valid @RequestBody Person person, HttpServletResponse response) {

        Person personCreated = repository.save(person);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(personCreated.getId())
                .toUri();

        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).build();
    }

}
