
# Clínica Odontológica — Sistema de gestión clínica con arquitectura hexagonal y DDD

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE.md)
[![Documentation](https://img.shields.io/badge/docs-ready-blue)](docs/README.md)
[![Open Source](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://opensource.org/)

> Un sistema real de clínica odontológica que demuestra evolución arquitectónica, Domain-Driven Design y buenas prácticas.  
> **Se ofrece como material de estudio, base para proyectos educativos y punto de partida para contribuciones.**

---

## 🎯 ¿Qué hace este sistema?

Gestiona la operación completa de una clínica odontológica:

- **Actores clínicos** (pacientes, odontólogos, guardianes, recepcionistas)
- **Agenda de citas** (turnos, disponibilidad, conflictos)
- **Servicios odontológicos** (catálogo con variantes por especialidad)
- **Facturación** (tarifas vigentes, snapshot inmutable, cumplimiento DIAN simulado)
- **Autorización** (RBAC/ABAC híbrido, roles múltiples, permisos contextuales)
- **Contabilidad básica** (plan de cuentas, asientos, saldos iniciales)

---

## 🧠 ¿Por qué este proyecto es diferente?

No es un CRUD con Spring Boot. Es un **ejercicio de arquitectura y madurez técnica**:

- ✅ **Arquitectura hexagonal** – dominio puro, desacoplado de infraestructura.
- ✅ **Domain-Driven Design** – agregados, Value Objects, servicios de dominio, bounded contexts.
- ✅ **Decisiones documentadas** – más de 90 ADRs (Architecture Decision Records) que explican el *por qué*.
- ✅ **Estrategia híbrida de errores** – `Outcome` para flujos técnicos, excepciones para reglas de negocio.
- ✅ **Autorización contextual** – no solo roles, también ownership, sector y especialidad.
- ✅ **Evolución transparente** – incluye material histórico (decisiones iniciales, descubrimientos de reglas) para mostrar aprendizaje y refactorización.

---

## 📚 Documentación principal

| Recurso                                                              | Descripción |
|----------------------------------------------------------------------|-------------|
| [`STORY.md`](STORY.md)                                               | Origen, motivación y evolución del proyecto (léelo primero). |
| [`src/docs/README.md`](src/docs/architecture/README.md)                  | Guía completa de la documentación. |
| [`src/docs/architecture/overview.md`](src/docs/architecture/overview.md) | Visión general de la arquitectura (hexagonal, bounded contexts, principios). |
| [`src/docs/architecture/decisions/`](src/docs/architecture/decisions/)   | ADRs activos y pendientes (decisiones arquitectónicas clave). |
| [`src/docs/domain/`](src/docs/domain/)                                   | Reglas de negocio vigentes y glosario. |
| [`src/docs/guides/`](src/docs/guides)                                    | Guías prácticas (setup, contribución, pruebas). |
| [`src/docs/evolution/`](src/docs/evolution/)                             | Material histórico (aprendizajes iniciales, ADRs superados). |

> 📌 Los diagramas C4 están en [`src/docs/architecture/c4/`](src/docs/architecture/c4/).

---

## 📌 Estado actual del proyecto (abril 2026)

Este proyecto es **una base sólida de arquitectura hexagonal y DDD**, pero **no está completamente terminado**. Se ofrece como material de estudio y punto de partida.

### ✅ Lo que funciona y está probado
- **Autenticación y autorización** (JWT, RBAC/ABAC híbrido, `@RequiresPermission`)
- **Gestión de actores** (Paciente, Odontólogo, Guardián, Recepcionista)
- **Operaciones de agenda** (Shift)
- **Documentación de decisiones** (ADRs, guías, historia)

### ⚠️ Lo que está implementado pero no probado a nivel de endpoints
- **Facturación** (Billing) – lógica de dominio presente, pero los endpoints no han sido validados.
- **Servicios odontológicos** (DentalService) – catálogo y detalles implementados, sin pruebas de integración.
- **Contabilidad** (Accounting) – asientos, plan de cuentas, saldos iniciales, sin verificación end-to-end.
- **Tratamientos clínicos** (ClinicalTreatments) – estructura básica, pendiente de pruebas.

### ❌ Lo que falta o está pendiente
- **Integración real con DIAN** (actualmente simulado).
- **Notificaciones** (Twilio, SendGrid).
- **Reportes PDF** (JasperReports).
- **Otras pasarelas de pago** (PayU).
- **Pruebas de integración JPA** (deshabilitadas por simplicidad).
- **Pruebas de concurrencia** (críticas para agendamiento y numeración de facturas).
- **Dockerización y CI/CD** (pendiente).

> **Este proyecto no es "production‑ready", pero es una excelente base para aprender, enseñar o continuar.**  
> Si buscas un sistema completo, necesitarás invertir tiempo en completar las áreas pendientes.

---

## 🚀 Tecnologías utilizadas

- **Java 17+**  
- **Spring Boot 3** (Web, Data JPA, Security)  
- **JPA / Hibernate** (persistencia)  
- **MySQL** (base de datos)  
- **JWT** (autenticación stateless)  
- **Maven** (construcción)  
- **PlantUML** (diagramas como código)  

---

## 🛠️ Cómo ejecutar el proyecto (local)

```bash
# Clonar el repositorio
git clone https://github.com/DavidSanclementeRamos/ClinicaDefinitiva2.git
cd ClinicaDefinitiva2

# Configurar base de datos (editar application.properties o usar variables de entorno)

# Ejecutar con Maven
./mvnw spring-boot:run
```
## 🗄️ Base de datos

El script de creación del esquema se encuentra en [`src/main/resources/database/clinica.sql`](src/main/resources/database/clinica.sql).  
Puedes ejecutarlo en tu servidor MySQL/PostgreSQL para inicializar las tablas necesarias.

## 🧪 Probar la API

Puedes importar la colección de Postman desde [`ClinicaDefinitiva.postman_collection.json`](src/docs/api/postman/collections/ClinicaDefinitiva) para explorar todos los endpoints disponibles.

---

## 🤝 Contribuir

Las contribuciones son bienvenidas.  
El proyecto tiene muchas áreas abiertas: pruebas, corrección de bugs, nuevas funcionalidades, documentación e infraestructura.

Por favor, lee [`CONTRIBUTING.md`](CONTRIBUTING.md) para conocer las tareas prioritarias y cómo empezar.

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT.  
Consulta el archivo [`LICENSE`](LICENSE.md) para más detalles.

---

## 👤 Autor

**David Stiven Sanclemente** — arquitecto y desarrollador principal.  
Este proyecto nació como una iniciativa personal para demostrar crecimiento técnico y capacidad de diseñar sistemas complejos con trazabilidad real.

📫 Contacto: [davidrsmos434@gmail.com](davidrsmos434@gmail.com)  
🐙 GitHub: [github.com/DavidSanclementeRamos/ClinicaDefinitiva2](https://github.com/DavidSanclementeRamos)

Este proyecto no existiría sin el contenido de **[Dev Dominio](https://youtube.com/@devdominio?si=mXfCLc6KwuGY6B27)**, **[TodoCode / Ingeniera Lucina](https://youtube.com/playlist?list=PLQxX2eiEaqbzhvlMJZkyFoZpyo33T6rm7&si=9jdErvbPsPUYOHec)**, **[Píldoras Informáticas](https://youtube.com/playlist?list=PLU8oAlHdN5BktAXdEVCLUYzvDyqRQJ2lk&si=Yb9NolQZVehFP8Tb)** y varias personas cuyo trabajo gratuito en YouTube moldeó la forma en que pienso sobre arquitectura y diseño. La historia completa de esas influencias está en [`STORY.md`](STORY.md).

---

## 🙏 Epílogo

Este proyecto no está terminado, y quizás nunca lo esté.  
Pero lo que sí está aquí es una **base sólida**: decisiones documentadas, arquitectura limpia, código organizado y una historia real de aprendizaje.

Si eres profesor, úsalo como material de estudio.  
Si eres estudiante, estudia su arquitectura, no su código terminado.  
Si eres desarrollador, toma lo que sirva y mejora lo que falta.

El conocimiento no se guarda, se comparte.

— David Stiven Sanclemente

---

## ⭐ Si te gusta o te sirve

Dale una estrella ⭐ al repositorio.  
Ayuda a que otros profesionales lo encuentren y lo usen como referencia.

---

*“La arquitectura no es sobre frameworks, es sobre decisiones justificables.”*


