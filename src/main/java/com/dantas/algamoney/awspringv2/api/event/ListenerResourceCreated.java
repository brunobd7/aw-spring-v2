package com.dantas.algamoney.awspringv2.api.event;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class ListenerResourceCreated implements ApplicationListener<ResourceCreatedEvent> {

    @Override
    public void onApplicationEvent(ResourceCreatedEvent resourceCreatedEventent) {
        HttpServletResponse response = resourceCreatedEventent.getResponse();
        Long resourceCreatedId = resourceCreatedEventent.getId();

        setHeaderLocation(response, resourceCreatedId);

    }

    private static void setHeaderLocation(HttpServletResponse response, Long resourceCreatedId) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(resourceCreatedId)
               .toUri();

        response.setHeader("Location", uri.toASCIIString());
    }
}
