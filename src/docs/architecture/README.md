# 📚 Catálogo de Architecture Decision Records (ADRs)

Este directorio contiene las **decisiones arquitectónicas activas** del proyecto.  
Cada ADR documenta un problema, el contexto, las alternativas evaluadas y la decisión tomada, con sus consecuencias.

Los ADRs son el principal artefacto de trazabilidad arquitectónica del sistema.  
**Si quieres entender por qué el sistema está diseñado como está, empieza por aquí.**

---

## 📌 Estado de los ADRs

| Estado | Significado | Ubicación                        |
|--------|-------------|----------------------------------|
| ✅ Activo | Decisión vigente y aplicada | `./decisions/` (ver lista abajo) |
| 📁 Deprecated / Superado | Decisión reemplazada por otra más reciente | `../evolution/deprecated/`       |
| 📖 Histórico / Lección aprendida | Documenta evolución, no decisión vigente | `../evolution/lessons-learned/`  |

> **Nota:** Los ADRs superados o históricos se han movido a `../evolution/` para mantener este índice limpio.  
> Si buscas una decisión antigua (ej. ADR-02, ADR-03), consulta las carpetas de evolución.

---

## 🗂️ Estructura del catálogo de ADRs activos

Los ADRs se organizan por **categorías** para facilitar la navegación.  
Cada ADR conserva su numeración original para referencias cruzadas.

---

## 1. ARQUITECTURA GENERAL (38 ADRs activos)

| ADR | Título | Resumen |
|-----|--------|---------|
| ADR-01 | Migración progresiva a arquitectura hexagonal | Adopción de Ports & Adapters, separación dominio/aplicación/infraestructura. |
| ADR-05 | Módulo independiente para Servicios | Extrae `dental.care.services` de Administration como bounded context propio. |
| ADR-06 | Separación de Facturación y Pagos | Crea módulos `billing` y `payments` independientes. |
| ADR-07 | Redefinición del módulo Administration | Enfoca Administration en contratos, gastos y roles administrativos. |
| ADR-08 | Estrategia de Integraciones | Anti-Corruption Layer y Gateway para servicios externos (Stripe, DIAN, EPS). |
| ADR-10 | Persistencia para ProvidedService | Tabla principal + tablas one-to-one por tipo de detalle (ortodoncia, cirugía, etc.). |
| ADR-11 | Implementación inicial del módulo contable (CRUD) | Prototipo contable como base para evolución. |
| ADR-12 | Nuevos agregados contables | Empresa, Tercero, CuentaContable, SaldoInicial, MovimientoContable. |
| ADR-13 | DTOs por operación y update por tipo de datos | Separa `UpdateContactDto` y `UpdateSensitiveDto` para seguridad y claridad. |
| ADR-14 | Separación de Identity y Administración | `UserIdentity` (autenticación) separado de roles/permisos (administración). |
| ADR-15 | Revocación de permisos | Solo en roles clonados, no en roles base. |
| ADR-17 | Plan de cuentas y asientos contables | Modelo contable: PUC desde JSON, `LedgerAccount`, `JournalEntry` con líneas. |
| ADR-18 | Simplificación general de jerarquía de excepciones | Reemplaza decenas de clases específicas por excepciones parametrizadas. |
| ADR-19 | Catálogo único con contextos diferenciados | Introduce `DomainContext` (EntityContext / VOContext) para trazabilidad. |
| ADR-20 | Alcance experimental – Módulo Actor | Define reglas aplicadas, pospuestas y eliminadas en Actor. |
| ADR-21 | Catálogos de errores por agregado con interfaz común | Cada agregado tiene su enum de errores implementando `ErrorCatalog`. |
| ADR-26 | Separación de descubrimientos por VO en Services | Cada VO estratégico tiene su archivo de descubrimiento. |
| ADR-27 | Relación entre descubrimientos de reglas y ADRs formales | Define cómo vincular documentos exploratorios con decisiones formales. |
| ADR-28 | Ubicación de validaciones de longitud en hexagonal | Reglas de negocio en dominio, restricciones técnicas en aplicación/DTO. |
| ADR-31 | Identificadores de agregados (UUID vs Long) | Usa `Long` autogenerado por BD, pero con VO semántico (`PatientId`, etc.). |
| ADR-32 | DTO simplificado y DTO de detalle | Dos niveles: listados (paginación) y detalle completo. |
| ADR-33 | `PageResponse<T>` como estándar | Encapsula metadatos de paginación, evita exponer `Page` de Spring Data. |
| ADR-34 | Separación `save`/`update` en puertos | Repositorios solo exponen `save` genérico; UseCases tienen métodos semánticos. |
| ADR-35 | Paginación obligatoria en relaciones 1:N | Toda relación 1:N devuelve `Page<DTO>` (excepción si cardinalidad ≤10). |
| ADR-36 | Excepciones de negocio en capa de aplicación | Repositorios devuelven `Optional`, Application Service lanza `NotFoundException`. |
| ADR-37 | Arquitectura hexagonal para módulo de acceso | Aplica hexagonal a autenticación/autorización con puertos y adaptadores. |
| ADR-38 | `UserDeactivationPolicy` como orquestador | Centraliza validaciones de desactivación que involucran múltiples agregados. |
| ADR-39 | Ubicación de validaciones de desactivación | Regla: validación interna en agregado, cross-aggregate en Domain Service, múltiples en Policy. |
| ADR-40 | Estrategia híbrida (Outcome vs Exceptions) | `Outcome` para módulos técnicos (autenticación), excepciones para reglas de negocio. |
| ADR-42 | `AggregateBusinessRuleViolationException` | Para acumular múltiples violaciones de reglas (ej. desde `Outcome`). |
| ADR-43 | Centralización de parámetros de seguridad en `SecurityPolicy` | Configuración externa (maxAttempts, lockDuration) inyectada desde properties. |
| ADR-45 | Manejo de "not found" en agregados técnicos con Outcome | En agregados que usan `Outcome`, el caso "no encontrado" se maneja con excepción. |
| ADR-46 | Integración JWT y Spring Security | JWT como implementación de `TokenServicePort`, Spring Security en infraestructura. |
| ADR-47 | Modelo híbrido RBAC/ABAC para autorización | 80% RBAC centralizado (`RoleBasedPolicy`), 20% políticas ABAC contextuales. |
| ADR-48 | Obtención de `UserId` y `RolId` en casos de uso | Parámetros explícitos desde controller, no `SecurityUtils`. |
| ADR-50 | Simplificación de VOContext y eliminación de CodeVO | Reduce `VOContext` de 50+ a 8 entradas por módulo; elimina `CodeVO`. |
| ADR-51 | Authorization Helper Pattern | Helper centralizado para construir `SecurityContext` y auditar decisiones. |
| ADR-52 | Jerarquía definitiva de excepciones y gobernanza | Unifica jerarquía, corrige bugs, extiende `ErrorCatalog`. **ADR central de errores.** |
| ADR-53 | Abandono del historial de catálogos eliminados | Reconoce que documentar cada eliminación es inviable; adopta numeración secuencial. |

---

## 2. DOMINIO GENERAL (8 ADRs activos)

| # Original   | Título | Resumen |
|--------------|--------|---------|
| ADR-01 (Dom) | Implementación estratégica de Value Objects | Adopta uso sistemático de VOs inmutables con validaciones en construcción. |
| ADR-02 (Dom) | Implementación sistemática de reglas de negocio por agregado | Establece reglas explícitas, documentadas por operación y asociadas a agregados. |
| ADR-04 (Dom) | Inquietud sobre el rol de los Servicios de Dominio | Aclara cuándo usar Domain Services vs métodos en agregados. |
| ADR-06 (Dom) | DTOs específicos por operación vs VO reutilizable | Prioriza DTOs específicos por operación sobre VOs globales reutilizables. |
| ADR-07 (Dom) | Estrategia de construcción de objetos en el dominio | Define cuándo usar Builder, constructor, record o factory method. |
| ADR-11 (Dom) | Creación del módulo independiente Clinical Treatments | Extrae `Treatment` de Dental Services a un módulo propio. |
| ADR-12 (Dom) | Validación de Null en Agregados vs Value Objects | Los agregados no deben re-validar null de VOs; los VOs ya garantizan su invarianza. |
| ADR-13 (Dom) | Eliminación de validación de `id` en constructores de agregados | Elimina `Objects.requireNonNull(id)` porque el `id` es responsabilidad de infraestructura. |

---

## 3. AUTORIZACIÓN (2 ADRs activos)

| # | Título | Resumen |
|---|--------|---------|
| Aut-001 | Gestión de roles múltiples con UserRolAssignment | Permite múltiples roles por usuario (simultáneos, temporales, con rol primario). |
| Aut-002 | Modelado de Permiso como VO estático | Catálogo cerrado de permisos en código, no entidad persistida. |

---

## 4. ACTORES (10 ADRs activos)

| # Original   | Título | Resumen |
|--------------|--------|---------|
| ADR-02 (Act) | Delegación de lógica de desactivación de Dentist a Domain Service | Mueve lógica compleja a `DentistDeactivationService`. |
| ADR-03 (Act) | Mantener mutación local en Dentist | El agregado mantiene métodos puros; Domain Service orquesta efectos. |
| ADR-04 (Act) | Separación de edición de datos de paciente | Distingue datos de contacto (blandos) de datos sensibles (con bloqueo por citas). |
| ADR-05 (Act) | Representación de TypeGuardian como VO híbrido | Instancias estáticas + fábrica para valores dinámicos. |
| ADR-06 (Act) | Validación de responsable en paciente | Valida existencia de `Guardian` para menores en el constructor. |
| ADR-07 (Act) | Ubicación del patrón Builder en Dentist | Builder como clase interna para agregados complejos. |
| ADR-10 (Act) | Modelado de Persona | Rechaza herencia, usa `PersonInfo` VO y IDs separados. |
| ADR-11 (Act) | Separación de estado entre User y Dentist | `UserStatus` y `AvailabilityStatus` como VOs separados. |
| ADR-13 (Act) | Eliminación del patrón Builder en Receptionist | Usa método de fábrica `registerReceptionist` en lugar de Builder. |
| ADR-17 (Act) | Modelado del agregado Recepcionista | Mantiene Receptionist simple (sin disponibilidad ni jornada) por ser rol administrativo. |

---

## 5. CITAS (6 ADRs activos)

| # | Título | Resumen |
|---|--------|---------|
| Cita-04 | Sustitución de retorno booleano por excepciones semánticas | Reemplaza `canScheduleBetween` booleano por `validateScheduleBetween` que lanza excepciones. |
| Cita-05 | Revisión de uso de queries de Schedule | Valida tiempo mínimo y ventana máxima directamente en `Appointment`, no mediante queries. |
| Cita-06 | Uso del patrón Builder en Appointment | Mantiene Builder para Appointment (agregado rico) como excepción justificada. |
| Cita-07 | Consolidación de Shift como única fuente de verdad temporal | Elimina `Availability`, extiende `Shift` con `ExcludedBlocks`. |
| Cita-08 | Transformación de Schedule en ScheduleQueryService | Elimina Schedule como agregado, lo convierte en servicio de consulta. |
| Cita-09 | Eliminación de validaciones redundantes de estado de usuario | Elimina `DentistCanScheduleBetween` y `PatientCanScheduleBetween`. |

---

## 6. FACTURACIÓN (4 ADRs activos)

| # | Título | Resumen |
|---|--------|---------|
| Fact-01 | Validación de tarifas vigentes al momento de facturar | Impide facturar con tarifas vencidas; alertas preventivas. |
| Fact-02 | Snapshot inmutable de precios en facturación | Copia el precio en `InvoiceItem` al facturar, evitando cambios retroactivos. |
| Fact-03 | Cumplimiento normativo DIAN Colombia | Numeración consecutiva, CUFE, QR, transmisión a DIAN, notas crédito, conservación 5 años. |
| Fact-04 | Uso de ProviderId como emisor oficial | Introduce `ProviderId` (clínica) como emisor; mantiene `DentistId` como profesional que prestó el servicio. |

---

## 7. SERVICIOS (8 ADRs activos)

| # | Título | Resumen |
|---|--------|---------|
| Serv-02 | Ubicación del VO Price | `Price` en shared-kernel, usado por Servicios y Facturación. |
| Serv-03 | Precio base en el agregado Service | `ProvidedService` tiene `basePrice` para garantizar consistencia. |
| Serv-05 | Delegación de validaciones a VOs y renombrado Money → Price | Crea `Name`, `Description` VOs; renombra `Money` a `Price`. |
| Serv-07 | Ubicación de VOs de composición en ProvidedService | Mantiene VOs de composición en el mismo paquete que la entidad raíz por ergonomía. |
| Serv-08 | Duda sobre redundancia en VOs de Service | Aclara que `ServiceId`, `ServiceName`, `ServiceCatalog` son complementarios. |
| Serv-09 | Delegación de validación de cambio de tarifa | Crea Domain Service `ServiceRatePolicy` para validar rango de cambio de tarifa. |
| Serv-10 | Simplificación del flujo de creación de InvoiceItem – eliminación de ServiceRendered | Supera ADR-01 y ADR-04. No se implementa `ServiceRendered`; se usan directamente `ProvidedService` y `Rate` en `InvoiceItemFactoryService`. |
---

## 8. CONTABILIDAD  (6 ADRs activos)

| #       | Título | Resumen |
|---------|--------|---------|
| Cont-01 | Validación de EPS del paciente | Registro manual con trazabilidad y soportes; bloqueo de facturación sin EPS válida. |
| Cont-02 | Uso de Domain Services para orquestar lógica entre agregados | Patrón estándar para reglas que involucran múltiples agregados. |
| Cont-03 | Modelado del Plan de Cuentas | Estructura de referencia inmutable cargada desde JSON, no agregado. |
| Cont-04 | Eliminación del agregado Gasto (Expense) | Los gastos se registran mediante `AsientoContable` con cuentas tipo GASTO. |
| Cont-05 | Modelado de Reportes Contables | Reportes operativos como proyecciones; Balance General y Resultado como agregado `ReporteContable`. |
| Cont-06 | Inconsistencia entre catálogo de errores y excepciones | Alinea catálogo con formato de descubrimientos (`ERR_REPORT_XXX`). |

---

## 9. USUARIO (4 ADRs activos)

| # | Título | Resumen |
|---|--------|---------|
| User-02 | UserIdentity como agregado rico | Bloqueo por intentos, verificación, `canPerformSensitiveAction()`, uso de `Outcome`. |
| User-03 | Integración Spring Security con dominio | Spring Security para aspectos técnicos; dominio para reglas de negocio. |
| User-04 | Validación de usuarios con UserAccessValidator | Anti-corruption layer entre `Outcome` (UserIdentity) y `Exceptions` (negocio). |
| User-06 | Estrategia dual de construcción en Value Objects (of / create) | Supera ADR-005. Unifica el manejo de excepciones y Outcome en VOs. |
---

## 📁 ¿Dónde están los ADRs que no aparecen aquí?

Los siguientes ADRs se han movido a `../evolution/` por estar superados o ser de carácter histórico:

| Carpeta | Contenido |
|---------|-----------|
| `../evolution/deprecated/` | ADR-02, 03, 04, 09, 22, 23 (arquitectura), Cita-01, 02, 03 (citas), User-01 (usuario), Dominio-03 (excepciones granulares) |
| `../evolution/lessons-learned/` | ADR-20, 24, 29, 49 (alcances experimentales y diseño inicial por tutoriales), Actor-08, 09, 16 (evolución de validaciones) |

Además, existen **gaps intencionales** (números de ADR no utilizados):  
- Arquitectura: 25, 30,   
- Dominio general: 08, 09  
- Servicios: 06  

Estos huecos se documentan en [ADR-53 (abandono del historial de catálogos y numeración inmutable)](decisions/arch/ADR-%28Arquitectura%29-53-Abandono%20del%20historial%20de%20cat%C3%A1logos%20eliminados%20y%20de%20la%20numeraci%C3%B3n%20inmutable.md).

---

## 🔍 Cómo leer este índice

- **Si eres nuevo en el proyecto**: empieza por [ADR-01 (hexagonal)](decisions/arch/ADR-%28Arquitectura%29-01-Migraci%C3%B3n%20progresiva%20a%20arquitectura%20hexagonal.md),  [ADR-40 (errores híbridos)](decisions/arch/ADR-%28Arquitectura%29-40-Estrategia%20H%C3%ADbrida%20de%20Manejo%20de%20Errores%20-%20Outcome.md) y [ADR-47 (autorización)](decisions/arch/ADR-%28Arquitectura%29-47-Modelo%20h%C3%ADbrido%20RBAC%20y%20ABAC%20para%20autorizaci%C3%B3n.md).  
- **Si te interesa un área concreta**: usa las tablas por categoría.  
- **Si quieres entender la evolución**: consulta los ADRs históricos en `../evolution/lessons-learned/`.

---

## 📌 Convenciones de enlaces

Dentro de cada ADR encontrarás referencias a otros ADRs usando rutas relativas.  
Como algunos ADRs están en carpetas diferentes, los enlaces pueden ser de tipo:

- Ver [ADR-18](decisions/arch/ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)
- Ver evolución en [ADR-20](../evolution/lessons-learned/ADR-%28Arquitectura%29-20-Alcance%20Experimental%20del%20M%C3%B3dulo%20Actor.md)



---

**Última actualización:** 2026-04-07  
**Mantenedor:** David Stiven Sanclemente
**Licencia:** MIT