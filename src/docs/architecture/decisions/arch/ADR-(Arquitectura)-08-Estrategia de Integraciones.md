# ADR-08 (Arquitectura): Estrategia de Integraciones

- **Estado:** Aprobado
- **Fecha**: 2025-10-11
- **Autor**: David Stiven Sanclemente

## Contexto
El sistema ha sido dividido en módulos independientes: Servicios, Facturación, Pagos y Administration.  
Para operar de forma robusta, se requieren integraciones externas:
- Stripe: gestión de pagos electrónicos en el módulo de Pagos.
- DIAN (Colombia): facturación electrónica y reportes tributarios.
- EPS / aseguradoras: validación y facturación de servicios cubiertos por convenios de salud.

Sin una estrategia clara, estas integraciones podrían acoplarse directamente a los módulos internos, generando dependencia y dificultando pruebas, cambios de proveedor o evolución tecnológica.

## Decisión
Se adoptará una política de integraciones desacopladas mediante APIs y adaptadores, siguiendo principios de alta cohesión y bajo acoplamiento:
- Ningún módulo interno invocará directamente servicios externos.
- Se definirán interfaces de integración (ej. StripeService, DIANReport, EPSIntegration) como contratos.
- Se implementará un External Service Gateway para centralizar la comunicación con APIs externas.
- Se aplicará el patrón Anti-Corruption Layer (ACL) para convertir datos entre formatos internos y externos.
- Las integraciones críticas (ej. DIAN) se implementarán con bajo acoplamiento y capacidad de pruebas unitarias/mocks.

## Consecuencias
Positivas
- Independencia de implementaciones externas: cambios en Stripe, DIAN o EPS no afectan el core.
- Facilidad de pruebas: adaptadores simulables con mocks.
- Escalabilidad: posibilidad de cambiar de proveedor (ej. Stripe → MercadoPago) modificando solo el adaptador.
- Se mantiene alta cohesión y bajo acoplamiento.

Negativas
- Mayor necesidad de documentar contratos de integración.
- Incremento en la lógica de mapeo y conversión dentro de los adaptadores.

## Plan de implementación
1. Definir contratos de API para StripeService, DIANReport y EPSIntegration.
2. Crear módulo integration.gateway con un External Service Gateway.
3. Implementar adaptadores siguiendo principios de Anti-Corruption Layer.
4. Añadir pruebas de integración con mocks para simular servicios externos.
5. Documentar flujos de datos y manejo de errores en docs/arquitectura/integraciones.md.

## Ejemplo
```java
// Contrato de integración
public interface StripeService {
PaymentResponse processPayment(PaymentRequest request);
}

// Adaptador con ACL
public class StripeAdapter implements StripeService {
private final StripeApiClient client;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        StripeCharge charge = client.createCharge(mapToStripeRequest(request));
        return mapToDomainResponse(charge);
    }
}
```

## Relación con otros ADR
- [ADR-(Arquitectura)-05-Creación de un módulo independiente para Servicios.md](ADR-%28Arquitectura%29-05-Creaci%C3%B3n%20de%20un%20m%C3%B3dulo%20independiente%20para%20Servicios.md)
- [ADR-(Arquitectura)-06-Separación de Facturación y Pagos en módulos independientes.md](ADR-%28Arquitectura%29-06-Separaci%C3%B3n%20de%20Facturaci%C3%B3n%20y%20Pagos%20en%20m%C3%B3dulos%20independientes.md)
- [ADR-(Arquitectura)-07-Redefinición del módulo Administration.md](ADR-%28Arquitectura%29-07-Redefinici%C3%B3n%20del%20m%C3%B3dulo%20Administration.md)