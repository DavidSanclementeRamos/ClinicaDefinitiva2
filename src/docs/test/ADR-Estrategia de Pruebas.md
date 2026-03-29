

# ADR Estrategia de Pruebas

## Contexto
El proyecto sigue una arquitectura hexagonal (puertos y adaptadores) con separación clara de capas: dominio, aplicación, infraestructura. Los módulos principales son:

- **Actor** (dentista, paciente, guardian, recepcionista)
- **Autenticación y Autorización** (UserIdentity, roles, permisos)
- **Operations (Turnos)** (Shift, ExcludedBlock)
- **Billing (Facturación)** (Invoice, InvoiceItem, Rate)
- **Clinical Treatments** (Treatment, TreatmentPhase)
- **Dental Services** (ProvidedService y sus detalles)
- **Schedule (Citas)** (Appointment)
- **Accounting** (Company, Contract, JournalEntry, LedgerAccount, OpeningBalance, AdministrativeReport, ThirdParties)
- **Payment** (Payment, gateways)

Se requiere una estrategia de pruebas que garantice la calidad, facilite la detección temprana de errores y permita refactorizaciones seguras, sin incurrir en costes excesivos de mantenimiento. Además, debe ser coherente con la arquitectura modular y la separación de responsabilidades.

## Decisión
Se adopta una **pirámide de pruebas** con los siguientes niveles:

### 1. Pruebas unitarias (base)
- **Dominio**: se probarán todas las entidades, value objects, servicios de dominio y reglas de negocio. Se utilizará JUnit 5 + AssertJ. Se mockearán dependencias solo cuando sea necesario (ej. servicios de dominio que usan repositorios).
- **Mapeadores de aplicación e infraestructura**: se probarán con datos de ejemplo para verificar conversiones correctas (aunque en muchos casos se considera código trivial, se cubrirán los más críticos).

### 2. Pruebas de integración (capa media)
- **Repositorios JPA**: se probarán con una base de datos real (Testcontainers) para validar consultas JPQL, relaciones y transacciones. Se utilizará `@DataJpaTest`.
- **Servicios de aplicación**: se probarán con mocks de los repositorios y gateways (`@ExtendWith(MockitoExtension.class)`) pero también con integración real de repositorios para escenarios complejos.
- **Gateways externos** (Stripe, EPS): se probarán con mocks para evitar dependencias reales. Solo en entornos controlados se harán pruebas de integración real.

### 3. Pruebas de capa web
- **Controladores REST**: se usarán `@WebMvcTest` para levantar solo la capa web, con los servicios de aplicación mockeados. Se validarán respuestas HTTP, serialización JSON y validaciones de entrada.

### 4. Pruebas de extremo a extremo (opcional, tope de la pirámide)
- Solo para flujos críticos (ej. ciclo completo de facturación + pago). Se ejecutarán con `@SpringBootTest` y Testcontainers. Se mantendrán en número reducido.

## Herramientas
- **JUnit 5**: framework base.
- **Mockito**: creación de mocks y spies.
- **AssertJ**: aserciones fluidas.
- **Testcontainers**: para pruebas de integración con bases de datos reales (PostgreSQL, MySQL).
- **@DataJpaTest**: para pruebas de capa JPA con base de datos embebida (H2) o Testcontainers.
- **@WebMvcTest**: para pruebas de controladores.
- **@SpringBootTest**: solo para pruebas E2E.

## Organización de pruebas
La estructura de carpetas de pruebas reflejará la estructura del código fuente, manteniendo la separación por módulos:

```
src/test/java/com/example/ClinicaDefinitiva/
  domain/
    actor/
      DentistTest.java
      PatientTest.java
      GuardianTest.java
      ReceptionistTest.java
    billing/
      InvoiceTest.java
      InvoiceItemTest.java
      RateTest.java
    payment/
      PaymentTest.java
      PayerTest.java
    schedule/
      AppointmentTest.java
    clinicalTreatments/
      TreatmentTest.java
      TreatmentPhaseTest.java
    dentalService/
      ProvidedServiceTest.java
      ServiceDetailsTest.java
    accounting/
      CompanyTest.java
      ContractTest.java
      JournalEntryTest.java
      LedgerAccountTest.java
      OpeningBalanceTest.java
      AdministrativeReportTest.java
      ThirdPartiesTest.java
    authentication/
      UserIdentityTest.java
    authorization/
      RolTest.java
      UserRolAssignmentTest.java
    operations/
      ShiftTest.java
      ExcludedBlockTest.java
  application/
    actor/
      DentistApplicationServiceTest.java
      PatientApplicationServiceTest.java
    billing/
      InvoiceApplicationServiceTest.java
    payment/
      PaymentApplicationServiceTest.java
    schedule/
      AppointmentApplicationServiceTest.java
    clinicalTreatments/
      TreatmentApplicationServiceTest.java
    dentalService/
      ProvidedServiceApplicationServiceTest.java
    accounting/
      CompanyApplicationServiceTest.java
      ContractApplicationServiceTest.java
      JournalEntryApplicationServiceTest.java
      LedgerAccountApplicationServiceTest.java
      OpeningBalanceApplicationServiceTest.java
      AdministrativeReportApplicationServiceTest.java
      ThirdPartiesApplicationServiceTest.java
    authentication/
      UserIdentityApplicationServiceTest.java
    authorization/
      RolApplicationServiceTest.java
      UserRolAssignmentApplicationServiceTest.java
    operations/
      ShiftApplicationServiceTest.java
  infrastructure/
    persistence/
      actor/
        DentistRepositoryTest.java
        PatientRepositoryTest.java
      billing/
        InvoiceRepositoryTest.java
        RateRepositoryTest.java
      payment/
        PaymentRepositoryTest.java
      schedule/
        AppointmentRepositoryTest.java
      clinicalTreatments/
        TreatmentRepositoryTest.java
      dentalService/
        ProvidedServiceRepositoryTest.java
      accounting/
        CompanyRepositoryTest.java
        ContractRepositoryTest.java
        JournalEntryRepositoryTest.java
        LedgerAccountRepositoryTest.java
        OpeningBalanceRepositoryTest.java
        AdministrativeReportRepositoryTest.java
        ThirdPartiesRepositoryTest.java
      authentication/
        UserIdentityRepositoryTest.java
      authorization/
        RolRepositoryTest.java
        UserRolAssignmentRepositoryTest.java
      operations/
        ShiftRepositoryTest.java
    rest/
      actor/
        DentistControllerTest.java
      billing/
        InvoiceControllerTest.java
      payment/
        PaymentControllerTest.java
      schedule/
        AppointmentControllerTest.java
      clinicalTreatments/
        TreatmentControllerTest.java
      dentalService/
        ProvidedServiceControllerTest.java
      accounting/
        CompanyControllerTest.java
        ContractControllerTest.java
        JournalEntryControllerTest.java
        LedgerAccountControllerTest.java
        OpeningBalanceControllerTest.java
        AdministrativeReportControllerTest.java
        ThirdPartiesControllerTest.java
      authentication/
        UserIdentityControllerTest.java
      authorization/
        RolControllerTest.java
        UserRolAssignmentControllerTest.java
      operations/
        ShiftControllerTest.java
    gateway/
      StripePaymentGatewayTest.java
      EPSPaymentGatewayTest.java
```

## Convenciones de nombres
- **Clases de prueba**: `NombreClaseTest.java`
- **Métodos de prueba**: usar `@DisplayName` con descripción legible en español.
- **Cobertura mínima**:
  - Dominio: 90%
  - Servicios de aplicación: 80%
  - Repositorios: 70%
  - Controladores: 60%
- No se requiere cobertura para getters/setters triviales ni para código generado automáticamente.

## Ejecución
- Las pruebas unitarias se ejecutarán en cada compilación (`mvn test`).
- Las pruebas de integración se ejecutarán en un perfil específico (`mvn test -Pintegration`) para no ralentizar el desarrollo local.
- En CI/CD se ejecutarán todas las pruebas.

## Estado
Aceptado.

---

# Tabla de Casos de Prueba

A continuación se listan los casos de prueba más relevantes por módulo, agrupados por tipo (unitaria, integración, web). Esta tabla servirá como guía para la implementación.

## Módulo Actor

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| ACT-UNIT-001 | Unitaria | Dentist | Registrar odontólogo con edad válida | Edad entre 25 y 130 | Person con edad 30 | Dentist creado con estado AVAILABLE |
| ACT-UNIT-002 | Unitaria | Dentist | Registrar odontólogo con edad inválida | Edad 20 | Person con edad 20 | Lanza BusinessRuleViolationException |
| ACT-UNIT-003 | Unitaria | Dentist | Aplicar vacaciones | Odontólogo disponible | start, end válidos | Estado cambia a VACATION, fechas almacenadas |
| ACT-UNIT-004 | Unitaria | Patient | Registrar paciente adulto con guardian | Edad 20, guardianId null | - | Lanza BusinessRuleViolationException |
| ACT-UNIT-005 | Unitaria | Patient | Registrar paciente menor con guardian | Edad 15, guardianId presente | - | Patient creado correctamente |
| ACT-UNIT-006 | Unitaria | Guardian | Registrar responsable con edad inválida | Edad 21 | - | Lanza BusinessRuleViolationException |
| ACT-INT-001 | Integración | DentistRepository | Guardar y recuperar dentista | - | Dentist completo | Se guarda, al recuperar coincide |
| ACT-WEB-001 | Web | DentistController | GET /api/v1/dentists/{id} | Dentista existe en BD | ID válido | Status 200, body con datos del dentista |

## Módulo Billing

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| INV-UNIT-001 | Unitaria | Invoice | Crear factura institucional | - | contractId, providerId, etc. | Invoice creada en estado DRAFT |
| INV-UNIT-002 | Unitaria | Invoice | Agregar ítem a factura en borrador | Factura en DRAFT | InvoiceItem válido | Item agregado, totales recalculados |
| INV-UNIT-003 | Unitaria | Invoice | Emitir factura sin ítems | Factura en DRAFT, sin ítems | - | Lanza BusinessRuleViolationException |
| INV-UNIT-004 | Unitaria | Invoice | Recibir pago parcial | Factura en PENDING | paymentAmount=50000 (total 100000) | totalPaid=50000, estado sigue PENDING |
| INV-UNIT-005 | Unitaria | Invoice | Recibir pago que completa factura | Factura en PENDING, total 100000 | paymentAmount=100000 | totalPaid=100000, estado cambia a PAID, evento publicado |
| INV-UNIT-006 | Unitaria | Rate | Crear tarifa activa | - | serviceId, amount, payerType | Rate creado en estado ACTIVE, validFrom=now |
| INV-UNIT-007 | Unitaria | Rate | Finalizar vigencia de tarifa | Rate activo | endDate después de validFrom | status cambia a EXPIRED, validTo asignado |
| INV-INT-001 | Integración | InvoiceRepository | Buscar facturas por paciente | Paciente tiene facturas | patientId | Página de facturas |
| INV-WEB-001 | Web | InvoiceController | POST /api/v1/invoices/particular | Datos válidos | JSON de factura particular | Status 201, body con factura creada |

## Módulo Payment

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| PAY-UNIT-001 | Unitaria | Payment | Crear pago pendiente | - | invoiceId, amount, method, payer | Payment con status PENDING |
| PAY-UNIT-002 | Unitaria | Payment | Confirmar pago en efectivo | Payment PENDING con CASH | - | Status CONFIRMED, transactionReference generado |
| PAY-UNIT-003 | Unitaria | Payment | Confirmar pago con gateway | Payment PENDING con STRIPE | transactionRef, gatewayPaymentId | Status CONFIRMED, transactionReference almacenado |
| PAY-UNIT-004 | Unitaria | Payment | Fallar pago | Payment PENDING | errorMessage | Status FAILED, errorMessage almacenado |
| PAY-UNIT-005 | Unitaria | Payment | Reembolso parcial | Payment CONFIRMED con amount 100000 | refundAmount 30000 | refundedAmount=30000, status CONFIRMED |
| PAY-UNIT-006 | Unitaria | Payment | Reembolso total | Payment CONFIRMED con amount 100000 | refundAmount 100000 | refundedAmount=100000, status REFUNDED |
| PAY-UNIT-007 | Unitaria | Payment | Reembolso que excede monto | Payment CONFIRMED con amount 100000 | refundAmount 150000 | Lanza BusinessRuleViolationException |
| PAY-UNIT-008 | Unitaria | Payer | Crear pagador paciente | nombre | type=PATIENT | isPatient() true, isInstitutional() false |
| PAY-UNIT-009 | Unitaria | Payer | Crear pagador institucional | nombre, identifier | type=EPS | isEPS() true, isInstitutional() true |
| PAY-INT-001 | Integración | PaymentProcessingService | Procesar pago en efectivo con mock | Invoice existe, total pendiente 200000 | amount 100000, method CASH | Payment creado y confirmado, evento publicado |
| PAY-INT-002 | Integración | PaymentProcessingService | Procesar pago con Stripe (mock gateway) | Gateway simulado éxito | amount 50000, method STRIPE | Payment confirmado, transactionReference almacenado |
| PAY-INT-003 | Integración | PaymentRepository | Guardar y recuperar pago con BD real | - | Payment completo | Se guarda y recupera correctamente |
| PAY-INT-004 | Integración | PaymentRepository | Buscar pagos por factura | Dos pagos asociados a invoiceId=1 | invoiceId=1 | Lista con ambos pagos |
| PAY-WEB-001 | Web | PaymentController | POST /api/v1/payments | Servicio mockeado devuelve PaymentDto | JSON válido | Status 201, body con datos del pago |
| PAY-WEB-002 | Web | PaymentController | POST /api/v1/payments con amount negativo | - | JSON con amount negativo | Status 400, error de validación |

## Módulo Schedule

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| SCH-UNIT-001 | Unitaria | Appointment | Crear cita con fecha futura | - | start después de now | Appointment creado con status SCHEDULED |
| SCH-UNIT-002 | Unitaria | Appointment | Cancelar cita dentro de 24h | start dentro de 24h | reason | Lanza BusinessRuleViolationException |
| SCH-UNIT-003 | Unitaria | Appointment | Marcar como no-show | Appointment SCHEDULED | reason | Status NO_SHOW |
| SCH-UNIT-004 | Unitaria | Appointment | Completar cita | Appointment SCHEDULED | completion con notas y duración real | Status COMPLETED |
| SCH-UNIT-005 | Unitaria | Appointment | Verificar solapamiento | Appointment existente 10:00-11:00 | candidate 10:30-11:30 | conflictsWith() true |
| SCH-INT-001 | Integración | AppointmentRepository | Buscar citas conflictivas con lock | - | dentista, start, end | Lista de citas conflictivas con lock pesimista |
| SCH-WEB-001 | Web | AppointmentController | POST /api/v1/appointments | Servicio mockeado devuelve AppointmentDto | JSON válido | Status 201 |

## Módulo Clinical Treatments

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| TRE-UNIT-001 | Unitaria | Treatment | Crear tratamiento con fecha inicio pasada | startDate <= hoy | startDate, patient, dentist | Treatment creado en estado ACTIVE |
| TRE-UNIT-002 | Unitaria | Treatment | Completar tratamiento | Treatment ACTIVE | actualEndDate | Status COMPLETED, actualEndDate asignado |
| TRE-UNIT-003 | Unitaria | Treatment | Cancelar tratamiento | Treatment ACTIVE | reason con al menos 10 caracteres | Status CANCELLED |
| TRE-UNIT-004 | Unitaria | TreatmentPhase | Iniciar fase | Phase en PENDING | - | Status IN_PROGRESS |
| TRE-UNIT-005 | Unitaria | TreatmentPhase | Completar fase | Phase en IN_PROGRESS | completedDate | Status COMPLETED, completedDate asignado |
| TRE-INT-001 | Integración | TreatmentRepository | Guardar tratamiento con fases | - | Treatment con dos fases | Se guarda, las fases se persisten en cascada |
| TRE-WEB-001 | Web | TreatmentController | POST /api/v1/treatments | Datos válidos | JSON de tratamiento | Status 201 |

## Módulo Dental Services

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| SER-UNIT-001 | Unitaria | ProvidedService | Crear servicio activo | - | name, category, code, rate, duration | ProvidedService creado en estado ACTIVE |
| SER-UNIT-002 | Unitaria | ProvidedService | Actualizar tarifa con justificación | Servicio activo | newRate, justification | BaseRate actualizada |
| SER-UNIT-003 | Unitaria | ProvidedService | Desactivar servicio con motivo corto | Servicio activo | reason < 10 chars | Lanza BusinessRuleViolationException |
| SER-UNIT-004 | Unitaria | OrthodonticDetails | Crear detalles ortodoncia válidos | applianceType válido, duración 6-48 | applianceType="METAL_BRACKETS", duration=24 | OrthodonticDetails creado |
| SER-UNIT-005 | Unitaria | OrthodonticDetails | Crear detalles con duración inválida | duración 50 | - | Lanza ValueObjectValidationException |
| SER-INT-001 | Integración | ProvidedServiceRepository | Guardar servicio con detalles | - | ProvidedService con OrthodonticDetails | Se guarda, los detalles se persisten en tabla separada |
| SER-WEB-001 | Web | ProvidedServiceController | GET /api/v1/services/{id} | Servicio existe | ID válido | Status 200, body con detalles del servicio |

## Módulo Accounting

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| ACC-UNIT-001 | Unitaria | JournalEntry | Crear asiento con partida doble | - | líneas con débitos=créditos | JournalEntry creado en estado no posteado |
| ACC-UNIT-002 | Unitaria | JournalEntry | Agregar línea que desbalancea | Asiento con débitos=créditos | línea adicional | validateBalance() lanza excepción |
| ACC-UNIT-003 | Unitaria | JournalEntry | Postear asiento | Asiento balanceado | - | posted = true |
| ACC-UNIT-004 | Unitaria | Contract | Crear contrato con fechas válidas | startDate antes de endDate | startDate, endDate | Contract creado en estado ACTIVE |
| ACC-UNIT-005 | Unitaria | Contract | Suspender contrato sin razón | Contract ACTIVE | reason = null | Lanza BusinessRuleViolationException |
| ACC-UNIT-006 | Unitaria | Company | Crear empresa con datos válidos | - | name, nit, tipoPersona, etc. | Company creada en estado ACTIVE |
| ACC-UNIT-007 | Unitaria | Company | Actualizar estado de inactivo a activo | Company INACTIVE | - | Lanza BusinessRuleViolationException |
| ACC-INT-001 | Integración | JournalEntryRepository | Guardar asiento con líneas | - | JournalEntry con 2 líneas | Se guarda, las líneas se persisten |
| ACC-WEB-001 | Web | JournalEntryController | POST /api/v1/journal-entries | Datos válidos | JSON de asiento | Status 201 |

## Módulo Operations (Turnos)

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| SHI-UNIT-001 | Unitaria | Shift | Crear turno con horario válido | start < end | dentistId, date, start, end | Shift creado en estado ACTIVE |
| SHI-UNIT-002 | Unitaria | Shift | Excluir bloque de tiempo dentro del turno | Turno 8-17, bloque 12-13 dentro | blockStart, blockEnd, reason | Bloque agregado a excludedBlocks |
| SHI-UNIT-003 | Unitaria | Shift | Excluir bloque que solapa con otro | Ya existe bloque 12-13 | bloque 12:30-13:30 | Lanza BusinessRuleViolationException |
| SHI-UNIT-004 | Unitaria | Shift | Verificar si puede acomodar cita | Turno 8-17, sin bloques | cita 9-10 | canAccommodateAppointment() true |
| SHI-UNIT-005 | Unitaria | Shift | Cancelar turno con motivo | Turno ACTIVE | reason | Status CANCELLED, cancellationReason asignado |
| SHI-INT-001 | Integración | ShiftRepository | Buscar turnos solapados | Dos turnos solapados | dentista, fecha, horario | Lista con el turno solapado |
| SHI-WEB-001 | Web | ShiftController | POST /api/v1/shifts | Datos válidos | JSON de turno | Status 201 |

## Módulo Authentication & Authorization

| ID | Tipo | Clase bajo prueba | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|------|-------------------|-----------|----------------|---------|-------------------|
| AUTH-UNIT-001 | Unitaria | UserIdentity | Registrar usuario con email válido | - | email, password, name | UserIdentity creado en estado PENDING_VERIFICATION |
| AUTH-UNIT-002 | Unitaria | UserIdentity | Verificar usuario | UserIdentity no verificado | - | verified = true |
| AUTH-UNIT-003 | Unitaria | UserIdentity | Bloquear usuario tras intentos fallidos | 4 intentos fallidos, maxAttempts=5 | 5to intento fallido | lockedUntil = now + lockDuration |
| AUTH-UNIT-004 | Unitaria | Rol | Crear rol personalizado | - | baseType, description | Rol creado en estado ACTIVE |
| AUTH-UNIT-005 | Unitaria | Rol | Agregar permiso a rol editable | Rol editable | permission | Permiso agregado al set |
| AUTH-UNIT-006 | Unitaria | UserRolAssignment | Asignar rol permanente | Usuario y rol existen | userId, rolId, isPrimary | Asignación creada con validFrom=now, validTo=null |
| AUTH-INT-001 | Integración | UserIdentityRepository | Guardar y buscar por email | - | email | Usuario recuperado |
| AUTH-WEB-001 | Web | UserIdentityController | POST /api/v1/users/register | Datos válidos | JSON de registro | Status 201 |


# Tabla de Casos de Prueba – Value Objects

| ID | Módulo | Value Object | Escenario | Precondiciones | Entrada | Resultado esperado |
|----|--------|--------------|-----------|----------------|---------|-------------------|
| VO-ACT-001 | Actor | Age | Crear edad válida | DateOfBirth = hace 30 años | - | Age = 30, isAdult() true |
| VO-ACT-002 | Actor | Age | Crear edad negativa (fecha futura) | DateOfBirth = mañana | - | Lanza ValueObjectValidationException |
| VO-ACT-003 | Actor | Age | isElderly() cuando edad ≥ 65 | DateOfBirth hace 70 años | - | isElderly() true |
| VO-ACT-004 | Actor | Document | Crear documento válido | "12345678" | - | Document creado |
| VO-ACT-005 | Actor | Document | Documento con formato inválido | "12-345" | - | Lanza ValueObjectValidationException |
| VO-ACT-006 | Actor | FullName | Crear nombre y apellido válidos | "Juan", "Pérez" | - | FullName creado |
| VO-ACT-007 | Actor | FullName | Nombre o apellido en blanco | " ", "Pérez" | - | Lanza ValueObjectValidationException |
| VO-ACT-008 | Actor | BloodType | Crear tipo de sangre válido | "A+" | - | BloodType = A+ |
| VO-ACT-009 | Actor | BloodType | Tipo de sangre inválido | "Z+" | - | Lanza ValueObjectValidationException |
| VO-ACT-010 | Actor | DentistAvailabilityStatus | Crear estado AVAILABLE | Status.AVAILABLE | - | isAvailable() true, isAbsent() false |
| VO-ACT-011 | Actor | DentistAvailabilityStatus | Crear estado SICK_LEAVE | Status.SICK_LEAVE | - | isAbsent() true, getPriority() NOT_ASSIGNABLE |
| VO-BIL-001 | Billing | CurrencyCode | Crear código de moneda válido | "COP" | - | CurrencyCode creado |
| VO-BIL-002 | Billing | CurrencyCode | Código de moneda inválido | "XXX" | - | Lanza ValueObjectValidationException |
| VO-BIL-003 | Billing | InvoiceNumber | Crear número de factura válido | "FAC-0001" | - | getPrefix() = "FAC", getSequence() = 1 |
| VO-BIL-004 | Billing | InvoiceNumber | Formato inválido | "FAC0001" | - | Lanza ValueObjectValidationException |
| VO-BIL-005 | Billing | InvoiceNumber | Número con prefijo correcto | "INV-0123" | - | hasPrefix("INV") true |
| VO-BIL-006 | Billing | InvoiceStatus | Transición válida: DRAFT → PENDING | status = DRAFT | - | canTransitionTo(PENDING) true |
| VO-BIL-007 | Billing | InvoiceStatus | Transición inválida: PAID → PENDING | status = PAID | - | canTransitionTo(PENDING) false, transitionTo() lanza |
| VO-BIL-008 | Billing | Quantity | Cantidad positiva | 5 | - | Quantity creada |
| VO-BIL-009 | Billing | Quantity | Cantidad cero o negativa | 0 | - | Lanza ValueObjectValidationException |
| VO-BIL-010 | Billing | Quantity | Cantidad excede máximo (1000) | 1500 | - | Lanza ValueObjectValidationException |
| VO-PAY-001 | Payment | Payer | Crear pagador paciente | type=PATIENT, name="Juan" | - | isPatient() true, isInstitutional() false |
| VO-PAY-002 | Payment | Payer | Crear pagador EPS con NIT | type=EPS, identifier="123", name="Sura" | - | isEPS() true, getIdentifier() = "123" |
| VO-PAY-003 | Payment | Payer | Crear pagador sin nombre | type=EPS, name=null | - | Lanza ValueObjectValidationException |
| VO-PAY-004 | Payment | PaymentMethod | Obtener método desde string válido | "CASH" | - | PaymentMethod.CASH |
| VO-PAY-005 | Payment | PaymentMethod | Método inválido | "INVALID" | - | Lanza ValueObjectValidationException |
| VO-PAY-006 | Payment | PaymentMethod | requiresGateway() para STRIPE | STRIPE | - | requiresGateway() true |
| VO-PAY-007 | Payment | PaymentMethod | isImmediate() para CASH | CASH | - | isImmediate() true |
| VO-PAY-008 | Payment | PaymentStatus | Transición válida: PENDING → CONFIRMED | status = PENDING | - | canTransitionTo(CONFIRMED) true |
| VO-PAY-009 | Payment | PaymentStatus | Transición inválida: CONFIRMED → PENDING | status = CONFIRMED | - | canTransitionTo(PENDING) false |
| VO-PAY-010 | Payment | PaymentStatus | isSuccessful() true para CONFIRMED | status = CONFIRMED | - | isSuccessful() true |
| VO-PAY-011 | Payment | TransactionReference | Crear referencia con gateway ID | "pi_123", "pi_123" | - | value() = "pi_123", gatewayPaymentId() = "pi_123" |
| VO-PAY-012 | Payment | TransactionReference | Referencia nula | null, "id" | - | Lanza ValueObjectValidationException |
| VO-SCH-001 | Schedule | AppointmentStatus | Transición válida: SCHEDULED → COMPLETED | status = SCHEDULED | - | canTransitionTo(COMPLETED) true |
| VO-SCH-002 | Schedule | AppointmentStatus | Transición inválida: COMPLETED → CANCELLED | status = COMPLETED | - | canTransitionTo(CANCELLED) false |
| VO-SCH-003 | Schedule | AppointmentStatus | isEditable() true para SCHEDULED | status = SCHEDULED | - | isEditable() true |
| VO-SCH-004 | Schedule | AppointmentStatus | isFinalState() true para COMPLETED | status = COMPLETED | - | isFinalState() true |
| VO-SCH-005 | Schedule | AppointmentType | Obtener tipo desde string | "ROUTINE_CHECKUP" | - | AppointmentType.ROUTINE_CHECKUP |
| VO-CLI-001 | ClinicalTreatments | TreatmentPhaseName | Crear nombre válido | "Colocación brackets" | - | getValue() = "Colocación brackets" |
| VO-CLI-002 | ClinicalTreatments | TreatmentPhaseName | Nombre muy corto | "A" | - | Lanza ValueObjectValidationException |
| VO-CLI-003 | ClinicalTreatments | PhaseStatus | Transición PENDING → IN_PROGRESS | status = PENDING | - | canTransitionTo(IN_PROGRESS) true |
| VO-CLI-004 | ClinicalTreatments | PhaseStatus | Transición IN_PROGRESS → COMPLETED | status = IN_PROGRESS | - | canTransitionTo(COMPLETED) true |
| VO-CLI-005 | ClinicalTreatments | PhaseStatus | Transición COMPLETED → PENDING | status = COMPLETED | - | canTransitionTo(PENDING) false |
| VO-DEN-001 | DentalService | ServiceCode | Crear código válido | "ORT-001" | - | getValue() = "ORT-001" |
| VO-DEN-002 | DentalService | ServiceCode | Código con longitud incorrecta | "ABC" (menor a 4) | - | Lanza ValueObjectValidationException |
| VO-DEN-003 | DentalService | ServiceCode | Código con caracteres no permitidos | "ORT@01" | - | Lanza ValueObjectValidationException |
| VO-DEN-004 | DentalService | ServiceDuration | Crear duración válida | 30 minutos | - | getMinutes() = 30, isShort() false, isLong() false |
| VO-DEN-005 | DentalService | ServiceDuration | Duración menor a mínima (15) | 10 | - | Lanza ValueObjectValidationException |
| VO-DEN-006 | DentalService | ServiceDuration | Duración mayor a máxima (480) | 500 | - | Lanza ValueObjectValidationException |
| VO-DEN-007 | DentalService | ServiceStatus | isActive() true para ACTIVE | status = ACTIVE | - | isActive() true |
| VO-DEN-008 | DentalService | ServiceStatus | isActive() false para INACTIVE | status = INACTIVE | - | isActive() false |
| VO-ACC-001 | Accounting | Period | Crear período válido | start=01-01-2025, end=31-12-2025 | - | contains(01-06-2025) true |
| VO-ACC-002 | Accounting | Period | Fecha fin anterior a inicio | start=01-01-2025, end=31-12-2024 | - | Lanza ValueObjectValidationException |
| VO-ACC-003 | Accounting | Period | isCurrentPeriod() verdadero | hoy dentro del rango | - | isCurrentPeriod() true |
| VO-ACC-004 | Accounting | CompanyStatus | Transición ACTIVE → INACTIVE | status = ACTIVE | - | canTransitionTo(INACTIVE) true |
| VO-ACC-005 | Accounting | CompanyStatus | isEditable() true para ACTIVE | status = ACTIVE | - | isEditable() true |
| VO-ACC-006 | Accounting | ReportStatus | canBeSubmittedForReview() true para DRAFT | status = DRAFT | - | canBeSubmittedForReview() true |
| VO-ACC-007 | Accounting | ReportStatus | canBeApproved() true para UNDER_REVIEW | status = UNDER_REVIEW | - | canBeApproved() true |
| VO-ACC-008 | Accounting | Nit | Crear NIT válido | "900123456-7" | - | getValue() = "900123456-7" |
| VO-ACC-009 | Accounting | Nit | Formato inválido | "ABC123" | - | Lanza ValueObjectValidationException |
| VO-ACC-010 | Accounting | NaturalezaCuenta | Obtener naturaleza desde string | "DEBITO" | - | NaturalezaCuenta.DEBITO |
| VO-OP-001 | Operations | ShiftStatus | Transición ACTIVE → COMPLETED | status = ACTIVE | - | canTransitionTo(COMPLETED) true |
| VO-OP-002 | Operations | ShiftStatus | Transición ACTIVE → CANCELLED | status = ACTIVE | - | canTransitionTo(CANCELLED) true |
| VO-OP-003 | Operations | ShiftStatus | Transición COMPLETED → ACTIVE | status = COMPLETED | - | canTransitionTo(ACTIVE) false |
| VO-OP-004 | Operations | ExcludedBlock | Crear bloque válido | start=12:00, end=13:00, reason="Almuerzo" | - | getDuration() = 1 hora |
| VO-OP-005 | Operations | ExcludedBlock | start >= end | start=13:00, end=12:00 | - | Lanza ValueObjectValidationException |
| VO-OP-006 | Operations | ExcludedBlock | overlapsWith() true | bloque1=12-13, bloque2=12:30-13:30 | - | overlapsWith() true |

