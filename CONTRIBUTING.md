
# Contribuir al proyecto Clínica Odontológica

Gracias por tu interés en este proyecto. Es un sistema de gestión clínica construido con arquitectura hexagonal y DDD, diseñado como portafolio profesional.  
Tu ayuda es bienvenida para completar funcionalidades, corregir errores, mejorar la documentación y preparar el proyecto para producción.

Antes de contribuir, lee nuestra [guía de documentación](src/docs/README.md) y el [código de conducta](CODE_OF_CONDUCT.md).

---

## 📌 Estado actual del proyecto (abril 2026)

El proyecto se encuentra en una fase estable de arquitectura, pero **aún hay mucho por hacer**:

- ✅ **Módulos con endpoints probados y funcionales**: Autenticación, Autorización, Operación (Shift) y Actor (Dentist, Patient, Guardian, Receptionist).
- ⚠️ **Módulos pendientes de probar a nivel de endpoints**: Facturación (Billing), Servicios Odontológicos (DentalService), Contabilidad (Accounting) y Tratamientos Clínicos (ClinicalTreatments).  
  Estos módulos pueden contener bugs no detectados, errores de lógica o problemas de integración. Se necesita ayuda para probarlos y corregirlos.
- ❌ **Faltan pruebas de integración** para la mayoría de los módulos (solo Actor tiene tests con Testcontainers).
- ❌ **No hay pruebas de concurrencia** (necesarias para agendamiento de citas y generación de números de factura).
- ❌ **Varias funcionalidades documentadas en ADRs no están implementadas** (ver sección "Tareas prioritarias").

Tu contribución puede marcar la diferencia.

---

## 🧭 Filosofía del proyecto

Antes de contribuir, familiarízate con los principios rectores:

1. **Arquitectura hexagonal** – El dominio es el núcleo, libre de infraestructura.
2. **Domain-Driven Design** – Agregados, Value Objects, servicios de dominio y bounded contexts.
3. **Decisiones documentadas** – Cada cambio arquitectónico importante debe ir acompañado de un ADR (ver [`docs/architecture/decisions/`](src/docs/architecture/decisions).
4. **Código limpio y trazable** – Nombres semánticos, validaciones en el lugar correcto, excepciones significativas.
5. **Manejo híbrido de errores** – `Outcome` para módulos técnicos (autenticación), excepciones para reglas de negocio.

Lee [`STORY.md`](STORY.md) y [`docs/architecture/overview.md`](src/docs/architecture/overview.md) para más contexto.

---

## 🤝 ¿Cómo contribuir?

### 1. Reportar bugs o sugerir mejoras

Abre un **issue** en GitHub con la plantilla correspondiente:
- Para bugs: describe el problema, pasos para reproducir, entorno y evidencia (logs, capturas).
- Para mejoras: explica el caso de uso, la solución propuesta y por qué es valiosa.

Usa las etiquetas adecuadas: `bug`, `enhancement`, `documentation`, `help-wanted`, `good-first-issue`.

### 2. Contribuir con código

1. **Fork** el repositorio y crea una rama con un nombre descriptivo:  
   `feature/descripcion-corta` o `fix/descripcion-corta`.
2. Sigue las convenciones de código (ver más abajo).
3. Escribe **pruebas** que cubran tu cambio (unitarias para el dominio, de integración para infraestructura).
4. Asegúrate de que todas las pruebas pasen: `./mvnw clean verify`.
5. Envía un **Pull Request** (PR) con una descripción clara del problema y la solución.

### 3. Áreas donde más se necesita ayuda

Revisa la sección **"Tareas prioritarias"** más abajo. Si eres nuevo, busca issues con la etiqueta `good-first-issue`.

---

## 📋 Tareas prioritarias (por orden de importancia)

### 🔴 Alta prioridad

| Área | Tarea | Detalles | ADR relacionado |
|------|-------|----------|-----------------|
| **Seguridad** | Evitar duplicación de asignación usuario-actor | Un mismo `user_identity_id` no puede estar asociado a más de un actor (ej. no puede ser Dentist y Patient a la vez). | ADR-14 |
| **Autorización** | Mejorar excepciones de denegación | Actualmente se lanza `ERR_AUTH_PERMISSION_DENIED` genérico. Debe diferenciar entre ownership, sector, especialidad, etc. | ADR-47, ADR-52 |
| **Auditoría** | Sistema básico de auditoría | Registrar todas las acciones del sistema (quién, cuándo, qué, valores anteriores/nuevos). Pendiente de ADR. | (Nuevo ADR necesario) |
| **Datos** | Persistir motivo de cambio de estado | En operaciones como `deactivate`, `cancel`, `suspend`, la razón no se guarda en BD. Agregar columna `reason` en las entidades correspondientes. | ADR-15, ADR-38, ADR-39 |
| **Pruebas** | Tests de infraestructura (JPA) para todos los módulos | Actualmente solo Actor tiene tests con Testcontainers. Habilitar y extender a Billing, DentalService, Accounting, etc. | ADR-10, ADR-31 |
| **Verificación de usuario** | Endpoint para verificar cuenta | Crear `POST /api/v1/users/{id}/verify` (actualmente se hace manual en BD). | ADR-02 (User) |
| **Integración DIAN real** | Conectar con el servicio oficial de facturación electrónica | Sustituir la simulación actual por integración real (requiere certificado digital). | ADR-03 (Facturación) |

### 🟡 Prioridad media

| Área | Tarea | Detalles | ADR relacionado |
|------|-------|----------|-----------------|
| **Eventos** | Completar eventos de dominio de pago | `PaymentConfirmedEvent`, `PaymentFailedEvent`, `PaymentRefundedEvent` no funcionan completamente. Decidir medio de publicación (BD/outbox vs archivo). | ADR-37, ADR-06 (Facturación) |
| **Autorización** | Búsqueda de permisos por rol | Implementar `GET /roles/{id}/permissions` en lugar de buscar permisos individuales. | ADR-47 |
| **Refactor** | Enriquecer agregado `Receptionist` | Actualmente es simple; añadir disponibilidad, turnos, etc. (ver ADR-17). | ADR-17 (Actores) |
| **Integraciones externas** | PayU, Twilio, SendGrid, JasperReports | Implementar como alternativas o complementos a Stripe, notificaciones y reportes. | ADR-08 |
| **Pruebas de concurrencia** | Simular múltiples requests simultáneos | Especialmente para agendamiento de citas y generación de números de factura. | ADR-24, ADR-35 |
| **Documentación** | Actualizar diagramas C4 (contexto, contenedores, componentes) | Reflejar el estado real del sistema (sin integraciones falsas). | ADR-01 |
| **Infraestructura** | Dockerizar la aplicación | Crear `Dockerfile` y `docker-compose.yml` para BD + app. | (Ninguno) |
| **Pipeline CI/CD** | GitHub Actions para compilar, testear y desplegar | Automatizar validaciones en cada PR. | (Ninguno) |

### 🟢 Prioridad baja

| Área | Tarea | Detalles | ADR relacionado |
|------|-------|----------|-----------------|
| **Validaciones comentadas** | Decidir si implementar o eliminar código comentado en `Guardian` y `Patient` (bloqueo por citas próximas). | Actualmente está comentado por ser demasiado rígido. | ADR-04, ADR-16 (Actores) |
| **Traducción** | Traducir documentación al inglés | Al menos la guía de contribución y el README principal. | (Ninguno) |
| **Ejemplos de API** | Documentar endpoints con OpenAPI/Swagger | Generar `openapi.yaml` con anotaciones `@Operation`. | (Ninguno) |
| **Scripts de despliegue** | Automatizar despliegue en entorno de prueba | Usar `docker-compose` y scripts bash. | (Ninguno) |

---

## 🧪 Pruebas específicas que faltan

| Tipo | Módulos afectados | Estado |
|------|-------------------|--------|
| Tests unitarios de dominio | Contabilidad, Operaciones | Parcial (faltan muchos) |
| Tests de aplicación (servicios) | Facturación, Servicios, Tratamientos | Mínimos |
| Tests de integración JPA | Todos excepto Actor | ❌ No implementados (deshabilitados en Maven) |
| Tests de concurrencia | Schedule (agendamiento), Billing (numeración factura) | ❌ No implementados |
| Tests de endpoints (REST) | Billing, DentalService, Accounting, ClinicalTreatments | ❌ No probados (pueden tener bugs) |

Si quieres contribuir con pruebas, revisa la configuración de `maven-surefire-plugin` y los tests existentes en `src/test/java/com/example/ClinicaDefinitiva/infrastructure/`.

---

## 📂 ADRs pendientes de implementación

Los siguientes ADRs están aprobados pero **no implementados** (o solo parcialmente). Se han movido a `docs/architecture/decisions/pending/` y son excelentes puntos de partida para contribuir:

| ADR | Título | Estado | Dificultad |
|-----|--------|--------|------------|
| ADR-16 | Permisos de menú en el sistema | ❌ No implementado | Media |
| ADR-43 | Centralización de parámetros de seguridad en `SecurityPolicy` | ❌ No implementado | Baja |
| ADR-02 (User) | UserIdentity como agregado rico (parte de integración con Spring Security) | ⚠️ Parcial (faltan handlers de login) | Media |

Para ADR-02, se necesitan implementar `AuthenticationFailureHandler` y `AuthenticationSuccessHandler` que invoquen `recordFailedLogin()` y `recordSuccessfulLogin()`.

---

## 🔧 Convenciones de código

- **Java 17** – usa records para DTOs y VOs cuando sea apropiado.
- **Nombres** – en inglés, semánticos y legibles.
- **Formato** – sigue el estilo del código existente (indentación 4 espacios, llaves en la misma línea). Si usas IDE, puedes importar el formatter desde `config/` (si existe).
- **Tests** – escribe tests unitarios para el dominio y tests de integración para los adaptadores.
- **Commits** – mensajes claros, en presente imperativo, siguiendo [Conventional Commits](https://www.conventionalcommits.org/).  
  Ejemplo: `feat(billing): add rate validation before invoice emission`
- **Pull requests** – máximo 400 líneas cambiadas (salvo refactorizaciones masivas). Explica el qué y el porqué. Si resuelve un issue, referencia `#123`.

---

## 🧪 Ejecutar pruebas

```bash
# Todas las pruebas (unitarias + integración)
./mvnw clean verify

# Solo pruebas unitarias
./mvnw test

# Solo pruebas de integración (requieren Docker para Testcontainers)
./mvnw verify -DskipUnitTests
```

> **Nota:** Los tests de integración JPA están actualmente excluidos en `pom.xml`. Para habilitarlos, elimina el `<exclude>` correspondiente. Necesitarás Docker para ejecutar Testcontainers.

---


## 💬 Preguntas y discusión

- Usa los [issues](https://github.com/tu-usuario/clinica-odontologica/issues) para preguntas técnicas.
- Para discusiones más largas, abre un [Discussion](https://github.com/tu-usuario/clinica-odontologica/discussions) (si el repo lo tiene habilitado).

---

## 📜 Código de conducta

Esperamos un trato respetuoso, profesional y constructivo.  
No se tolerarán conductas abusivas, discriminatorias o acosadoras.  
Al contribuir, aceptas seguir el [Código de Conducta](CODE_OF_CONDUCT.md) (debes crear este archivo si no existe).

---

## 🙏 Agradecimientos

Gracias por ayudar a mejorar este proyecto. Cada contribución, por pequeña que sea, suma.  
Juntos podemos hacer de este sistema un referente de buenas prácticas en DDD y arquitectura hexagonal.

---

**Última actualización:** 2026-04-08  
**Mantenedor:** David  
**Licencia:** MIT
```

