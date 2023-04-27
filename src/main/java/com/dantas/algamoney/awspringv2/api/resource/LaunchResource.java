package com.dantas.algamoney.awspringv2.api.resource;

import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.repository.LaunchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/launches")
public class LaunchResource {

    @Autowired
    private LaunchRepository repository;


    @GetMapping
    public ResponseEntity<List<Launch>> getAllLauches() {
        List<Launch> launchList = repository.findAll();
        return launchList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(launchList);

    }
}
