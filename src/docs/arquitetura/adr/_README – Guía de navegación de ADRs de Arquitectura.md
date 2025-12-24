

#  README – Guía de navegación de ADRs de Arquitectura

Este catálogo de Architecture Decision Records (ADRs) documenta las decisiones clave que dieron forma al sistema clínico-odontológico.  
No es solo una lista técnica: es una narrativa que permite al lector explorar cada ADR según el tema que más le interese.

---

##  Cómo navegar
Cada ADR responde a una pregunta arquitectónica concreta.  
Puedes leerlos en orden cronológico para seguir la evolución del sistema, o saltar directamente al tema que te llame la atención:

---

##  Identidad y Autorización
- [ADR-14: Separación de Identity y Administración](ADR-14-Seperacion-identidad-del-usuario-con-roles-y-permisos.md)  
  Explica por qué separamos credenciales técnicas de roles y permisos de negocio.
- [ADR-13: DTOs por operación y seguridad de datos sensibles](ADR-13-DTO-por-operaciones-y-updateDto-por-tipos-de-datos.md)  
  Cómo protegemos datos delicados con contratos API específicos.
- [ADR-15: Revocación de permisos](ADR-15-Revocacion-de-permisos.md)  
  Debate sobre si permitir quitar permisos a roles y la decisión de mantenerlo simple.
- [ADR-16: Permisos de menú en el sistema](ADR-16-Permisos-de-menu.md)  
  Cómo la UI refleja las reglas de negocio, gobernada desde el backend.

 Si te interesa la seguridad y control de acceso, empieza por aquí.

---

##  Administración y Contabilidad
- [ADR-07: Redefinición del módulo Administration](ADR-07-Redefinición%20del%20módulo%20Administration.md)  
  Primer paso para ampliar el alcance administrativo.
- [ADR-09: Nuevos agregados en Administration](ADR-09-Nuevos%20agregados%20en%20el%20módulo%20Administration.md) 
  Introducción de Empresa, Tercero, CuentaContable, SaldoInicial, MovimientoContable.
- [ADR-11: Implementación inicial del módulo contable](ADR-11-Implementación-inicial-de-módulo-contable.md) 
  Decisión de comenzar con un CRUD básico como prototipo.
- [ADR-17: Plan de cuentas y asientos contables](ADR-17-Manejo-de-plan-de-cuenta-y-asiento-contable.md)  
  Cómo modelamos el PUC colombiano y los asientos con JournalEntry.

 Si te interesa la gestión administrativa y contable, este es tu camino.

---

##  Modelado y Persistencia
- [ADR-10: Estrategia de modelado y persistencia para ProvidedService](ADR-10-dentalService.md)  
  Cómo representamos servicios odontológicos con composición y tablas específicas.
- [ADR-26: Identificadores únicos como String encapsulado en VO](ADR)  
  Decisión de usar UUID/ULID para trazabilidad global.

 Si te interesa el modelado técnico y persistencia, explora estos ADRs.

---

##  Reglas y Políticas
- ADR-30: Catálogo de reglas CRUD por rol  
  Qué operaciones puede hacer cada rol (DENTIST, GUARDIAN, PATIENT, RECEPTIONIST).
- ADR-29: Temas de estudio para comprender el modelo administrativo  
  Qué conceptos (RBAC, ABAC, ERP/CRM) hay que dominar para diseñar el módulo.
- ADR-28: Conocimientos necesarios para el dominio contable  
  Qué aprender para modelar correctamente contabilidad clínica en Colombia.

 Si te interesa la formalización de reglas y políticas, empieza aquí.

---

 📌 Consejos de lectura
- Busca lo que te interesa: seguridad, administración, persistencia o reglas.
- Lee las relaciones cruzadas: cada ADR menciona otros, formando un mapa completo.
- Recuerda que son artefactos vivos: reflejan decisiones tomadas en su momento, pero abiertas a evolución.

---

 ## Narrativa completa
En conjunto, estos ADRs cuentan la historia de cómo el sistema pasó de un núcleo clínico a un ecosistema administrativo-contable seguro y trazable.  
Cada decisión es un capítulo: desde separar identidad y administración, hasta modelar servicios, roles y cuentas contables.

---

