
# Visión general de la arquitectura

Este documento describe la arquitectura del sistema clínico odontológico a alto nivel.  
Está dirigido a desarrolladores, arquitectos y evaluadores que quieran entender rápidamente cómo está organizado el sistema, sus principios rectores y los contextos delimitados que lo componen.

---

## 1. Contexto del sistema

El sistema gestiona la operación de una clínica odontológica:  
pacientes, odontólogos, agenda de citas, facturación, servicios clínicos, contabilidad y autorización de usuarios.

**Actores principales:**  
- Pacientes  
- Odontólogos  
- Recepcionistas  
- Personal administrativo / contable  
- EPS y aseguradoras (integración externa, planificada)  

**Integraciones externas actuales:**  
- **Stripe**: pasarela de pagos (implementada).  

**Integraciones externas planificadas (futuras contribuciones):**  
- DIAN (facturación electrónica real – actualmente simulada)  
- Notificaciones por SMS/email (Twilio, SendGrid)  
- Reportes PDF (JasperReports)  
- Otras pasarelas de pago (PayU)

> 📌 No se incluye un diagrama de contexto por ahora, ya que las únicas integraciones externas reales son mínimas. En su lugar, se describen textualmente.

---

## 2. Principios arquitectónicos

| Principio | Aplicación |
|-----------|-------------|
| **Arquitectura hexagonal** | El dominio es el núcleo, libre de infraestructura. Los adaptadores (REST, JPA, eventos) se conectan mediante puertos. |
| **Domain-Driven Design** | Agregados, Value Objects, repositorios y servicios de dominio. Cada módulo es un *bounded context*. |
| **Separación de responsabilidades** | Identity (autenticación) vs. Administración (roles, permisos) vs. Dominio clínico. |
| **Manejo híbrido de errores** | `Outcome` para flujos técnicos (autenticación, autorización); excepciones para reglas de negocio. |
| **Autorización RBAC/ABAC híbrida** | 80% de permisos simples basados en rol; 20% contextual (ownership, sector, especialidad). |
| **Evolución documentada** | Cada decisión importante se registra como ADR. El material histórico se conserva para mostrar aprendizaje. |

---

## 3. Estructura hexagonal (capas)

```
┌─────────────────────────────────────────────────────────────┐
│                    ADAPTADORES DE ENTRADA                    │
│  (REST Controllers, Filtros JWT, Consumidores de eventos)   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      PUERTOS DE ENTRADA                      │
│                (Interfaces de casos de uso)                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   CAPA DE APLICACIÓN                         │
│  - Orquesta casos de uso                                     │
│  - Traduce entre DTOs y objetos de dominio                  │
│  - Maneja transacciones                                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       DOMINIO                                │
│  - Agregados, Value Objects, servicios de dominio           │
│  - Reglas de negocio e invariantes                          │
│  - Catálogos de errores (por agregado)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      PUERTOS DE SALIDA                       │
│         (Repositorios, servicios externos)                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   ADAPTADORES DE SALIDA                      │
│  - JPA Repositories                                          │
│  - Clientes HTTP (Stripe)                                   │
│  - Servicios de mensajería (planificados)                   │
└─────────────────────────────────────────────────────────────┘
```

> 📌 Los diagramas de contenedores y componentes actualizados están en [Ver diagrama de Contenedores](c4/Contenedores.svg) y [Ver diagrama de Componentes.svg](c4/Componentes.svg).

---

## 4. Bounded contexts principales

| Contexto                           | Descripción | ADR relevante                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|------------------------------------|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Actor**                          | Pacientes, odontólogos, guardianes, recepcionistas. | [ADR-(Actores)-10](decisions/domain/actor/ADR-%28Actores%29-10-Modelado%20de%20Persona.md) , [ADR-(Actores)-11](decisions/domain/actor/ADR-%28Actores%29-11-Separaci%C3%B3n%20de%20estado%20entre%20User%20y%20Dentist.md),[ADR-(Arquitectura)-39](decisions/arch/ADR-%28Arquitectura%29-39-Ubicaci%C3%B3n%20de%20validaciones%20de%20desactivaci%C3%B3n.md) , [ADR-(Arquitectura)-38](decisions/arch/ADR-%28Arquitectura%29-38-UserDeactivationPolicy%20como%20orquestador%20de%20validaciones.md)   
| **Schedule**                       | Citas, turnos (Shift), disponibilidad. | [ADR-(Cita)-07](decisions/domain/schedule/ADR-%28Cita%29-07-Consolidaci%C3%B3n%20de%20Shift%20como%20%C3%BAnica%20fuente%20de%20verdad%20temporal.md) , [ADR-(Arquitectura)-41](decisions/arch/ADR-%28Arquitectura%29-41-ScheduleRepository%20en%20lugar%20de%20AppointmentRepository.md)                                                                                                                                                                                                            |
| **Billing**                        | Facturación, tarifas, cumplimiento DIAN (simulado). | [ADR-(Arquitectura)-06](decisions/arch/ADR-%28Arquitectura%29-06-Separaci%C3%B3n%20de%20Facturaci%C3%B3n%20y%20Pagos%20en%20m%C3%B3dulos%20independientes.md) , [ADR-(Facturación)-02](decisions/domain/billing/ADR-%28Facturaci%C3%B3n%29-02-Snapshot%20Inmutable%20de%20Precios%20en%20Facturaci%C3%B3n.md)                                                                                                                                                                                        |
| **dental.care.services**           | Catálogo de servicios odontológicos (ProvidedService) y sus variantes (ortodoncia, cirugía, etc.). | [ADR-(Arquitectura)-05](decisions/arch/ADR-%28Arquitectura%29-05-Creaci%C3%B3n%20de%20un%20m%C3%B3dulo%20independiente%20para%20Servicios.md) , [ADR-(Arquitectura)](decisions/arch/ADR-%28Arquitectura%29-10-dentalService.md)                                                                                                                                                                                                                                                                      |
| **Authentication & Authorization** | UserIdentity (autenticación), roles, permisos (RBAC/ABAC). | [ADR-(Arquitectura)-14](decisions/arch/ADR-%28Arquitectura%29-14-Separaci%C3%B3n%20identidad%20del%20usuario%20con%20roles%20y%20permisos.md) , [ADR-(Arquitectura)-47](decisions/arch/ADR-%28Arquitectura%29-47-Modelo%20h%C3%ADbrido%20RBAC%20y%20ABAC%20para%20autorizaci%C3%B3n.md) , [ADR-(Arquitectura)-48](decisions/arch/ADR-%28Arquitectura%29-48-Obtenci%C3%B3n%20de%20UserId%20y%20RolId%20en%20casos%20de%20uso.md)                                                                      |
| **Accounting**                     | Plan de cuentas, asientos contables, saldos iniciales. | [ADR-(Arquitectura)-17](decisions/arch/ADR-%28Arquitectura%29-17-Manejo%20de%20Plan%20de%20Cuentas%20y%20Asientos%20Contables.md) , [ADR-(Arquitectura)-11](decisions/arch/ADR-%28Arquitectura%29-11-Implementaci%C3%B3n-inicial-de-m%C3%B3dulo-contable.md) ,[ADR-(Arquitectura)-12](decisions/arch/ADR-%28Arquitectura%29-12-Nuevos-agregados-en-modulo-contable.md) , [ADR-(Arquitectura)-07](decisions/arch/ADR-%28Arquitectura%29-07-Redefinici%C3%B3n%20del%20m%C3%B3dulo%20Administration.md) |

---

## 5. Decisiones arquitectónicas clave

A continuación se resumen las decisiones más importantes.  
Para profundizar, consultar los ADRs en [`decisions/`](./decisions/).

- **ADR-01**: Migración progresiva a arquitectura hexagonal.  
- **ADR-14**: Separación de Identity (credenciales) y Administración (roles, permisos).  
- **ADR-40**: Estrategia híbrida de errores: `Outcome` para módulos técnicos, excepciones para reglas de negocio.  
- **ADR-47**: Modelo híbrido RBAC/ABAC para autorización.  
- **ADR-52**: Jerarquía definitiva de excepciones y gobernanza de catálogos de error.  
- **ADR-31**: Identificadores de agregados como `Long` autogenerado (simplicidad), con VO semántico.  
- **ADR-10**: Persistencia de `ProvidedService` con tabla principal y tablas one-to-one por tipo de detalle.  
- **ADR-Persistencia-JPA**: Decisiones de mapeo JPA (`@Embeddable`, `@ElementCollection`, `@MapsId`, etc.).  


---

## 6. Modelo de base de datos

El esquema de base de datos se documenta en un **diagrama entidad-relación** ubicado en [ClinicaDefinitiva_DB.svg](c4/ClinicaDefinitiva_DB.svg).  
**La fuente de verdad absoluta son las clases JPA** (`@Entity`) en el código fuente. El diagrama es una ayuda visual y puede quedar desactualizado; ante cualquier discrepancia, prevalece el mapeo definido en las entidades JPA.


---

## 7. Cómo navegar la documentación

| Carpeta | Contenido |
|---------|-----------|
| [`decisions/`](./decisions/) | ADRs (decisiones arquitectónicas) activos y por contexto. |
| [`c4/`](c4/) | Diagramas C4 (contenedores, componentes, base de datos) en PlantUML. |
| [`../domain/`](../domain/) | Reglas de negocio vigentes, glosario. |
| [`../guides/`](../guides/) | Guías prácticas (setup, contribución, pruebas). |
| [`../support/`](../support/) | Catálogo de errores (resumido). |
| [`../evolution/`](../evolution/) | Material histórico (aprendizajes iniciales, ADRs superados). |

---

## 8. Tecnologías principales

- **Java 17+**  
- **Spring Boot 3** (Web, Data JPA, Security)  
- **JPA / Hibernate** (persistencia)  
- **MySQL** (base de datos)  
- **JWT** (autenticación stateless)  
- **Maven** (construcción)  
- **PlantUML** (diagramas como código)  

---

## 9. Próximos pasos para un nuevo desarrollador

1. Lee [STORY.md](../../../STORY.md) para entender el origen y motivación del proyecto.  
2. Revisa este documento para tener una visión general.  
3. Explora los diagramas C4 para ver la arquitectura lógica y el modelo de base de datos.  
4. Elige un contexto (ej. `Actor`, `Schedule`) y revisa sus ADRs y código fuente.  
5. Sigue la guía de contribución [CONTRIBUTING.md](../../../CONTRIBUTING.md) si quieres aportar.

---

**Última actualización:** 2026-04-08  
**Mantenedor:** David Stiven Sanclemente 
**Licencia:** [MIT](../../../LICENSE.md)


