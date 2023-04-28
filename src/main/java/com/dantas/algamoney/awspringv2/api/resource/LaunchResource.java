package com.dantas.algamoney.awspringv2.api.resource;

import com.dantas.algamoney.awspringv2.api.event.ListenerResourceCreated;
import com.dantas.algamoney.awspringv2.api.event.ResourceCreatedEvent;
import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.repository.LaunchRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/launches")
public class LaunchResource {

    @Autowired
    private LaunchRepository repository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @GetMapping("/")
    public ResponseEntity<List<Launch>> getAllLauches() {
        List<Launch> launchList = repository.findAll();
        return launchList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(launchList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Launch> getLaunchById(@PathVariable Long id){
        Launch launchFounded = repository.findById(id).orElse(new Launch());

        return Objects.isNull(launchFounded.getId())
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(launchFounded);

    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLaunch(@Valid @RequestBody Launch launch, HttpServletResponse response){
        Launch createdLaunch = repository.save(launch);

        eventPublisher.publishEvent(new ResourceCreatedEvent(createdLaunch, response,createdLaunch.getId()));
    }
}
