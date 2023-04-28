package com.dantas.algamoney.awspringv2.api.resource;

import com.dantas.algamoney.awspringv2.api.event.ListenerResourceCreated;
import com.dantas.algamoney.awspringv2.api.event.ResourceCreatedEvent;
import com.dantas.algamoney.awspringv2.api.exception.CustomApiErrorMessage;
import com.dantas.algamoney.awspringv2.api.exception.InvalidOrInactivePersonException;
import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.repository.LaunchRepository;
import com.dantas.algamoney.awspringv2.api.service.LaunchService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.aspectj.asm.IProgramElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/launches")
public class LaunchResource {

    @Autowired
    private LaunchRepository repository;

    @Autowired
    private LaunchService service;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

   @Autowired
    private MessageSource messageSource;


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

        Launch createdLaunch = service.saveLaunch(launch);

        eventPublisher.publishEvent(new ResourceCreatedEvent(createdLaunch, response,createdLaunch.getId()));
    }

    @ExceptionHandler({InvalidOrInactivePersonException.class})
    public ResponseEntity<Object> handleInvalidOrInactivePersonException(InvalidOrInactivePersonException ex){

        String userMessage = messageSource.getMessage("person.inactiveOrInvalidPerson",null, LocaleContextHolder.getLocale());
        String exMessage = ex.toString();

        List<CustomApiErrorMessage> errorList = List.of(new CustomApiErrorMessage(userMessage, exMessage));

        return ResponseEntity.badRequest().body(errorList);
    }
}
