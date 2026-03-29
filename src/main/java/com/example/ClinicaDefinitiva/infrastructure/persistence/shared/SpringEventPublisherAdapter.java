package com.example.ClinicaDefinitiva.infrastructure.persistence.shared;

import com.example.ClinicaDefinitiva.application.shared.service.ApplicationEventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adapter de infraestructura: implementa ApplicationEventPublisherPort
 * delegando en el mecanismo de eventos de Spring.
 *
 * Hexagonal: el dominio y la capa de aplicación solo conocen el puerto.
 * Este adapter es el único lugar donde Spring entra en contacto con
 * la publicación de eventos de dominio.
 */
@Component
public class SpringEventPublisherAdapter implements ApplicationEventPublisherPort {

    private final ApplicationEventPublisher springPublisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher springPublisher) {
        this.springPublisher = springPublisher;
    }

    @Override
    public void publish(Object event) {
        springPublisher.publishEvent(event);
    }
}
