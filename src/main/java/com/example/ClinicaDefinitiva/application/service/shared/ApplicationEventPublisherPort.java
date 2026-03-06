
package com.example.ClinicaDefinitiva.application.service.shared;



/**
 * Puerto de salida: publicación de eventos de dominio.
 *
 * Define el contrato que el dominio necesita para publicar eventos
 * sin conocer el mecanismo concreto de publicación (Spring, Kafka, RabbitMQ, etc.).
 *
 * Por qué existe esta interfaz y no se usa Spring directamente en el dominio:
 * ─────────────────────────────────────────────────────────────────────────────
 * La arquitectura hexagonal prohíbe que el dominio dependa de frameworks.
 * Si usaras ApplicationEventPublisher de Spring directamente en Payment o Invoice,
 * el dominio quedaría acoplado a Spring. Eso significa que no podrías probar
 * Payment ni Invoice en un test unitario sin levantar contexto de Spring.
 *
 * Con este puerto:
 *   - El dominio solo conoce esta interfaz (sin importar Spring).
 *   - La infraestructura provee la implementación concreta (SpringEventPublisherAdapter).
 *   - En tests unitarios se inyecta un fake/mock de un solo método.
 *
 * Quién llama a este puerto:
 * ─────────────────────────────────────────────────────────────────────────────
 * NO el agregado. El agregado solo acumula eventos en pendingEvents.
 * Es el Application Service quien llama a pullDomainEvents() tras persistir
 * y luego delega la publicación a este puerto:
 *
 *   paymentRepository.save(payment);
 *   payment.pullDomainEvents().forEach(eventPublisher::publish);
 */
public interface ApplicationEventPublisherPort {

    /**
     * Publica un evento de dominio al bus de eventos de la aplicación.
     *
     * @param event cualquier objeto que represente un evento de dominio.
     *             
     */
    void publish(Object event);
}
