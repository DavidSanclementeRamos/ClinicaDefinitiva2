
# Value Objects del dominio

Los Value Objects (VOs) son el corazón del modelo de dominio. Cada VO encapsula validaciones, reglas de negocio y comportamiento de un dato sensible, garantizando que el dominio nunca entre en un estado inválido.

## 📌 Fuente de verdad

**El código fuente es la única fuente de verdad.**  
Todos los VOs están implementados en Java dentro del paquete `com.example.ClinicaDefinitiva.domain.*.vo` (o `*.model` para los detalles de composición).  
Sus validaciones y reglas se encuentran en los constructores y métodos de fábrica (`of`, `from`, `valueOf`).

## 📜 Contexto histórico

Al inicio del proyecto, se intentó documentar **cada VO individualmente** en archivos Markdown separados. Sin embargo, con el crecimiento del sistema (actualmente más de 80 VOs activos), esta práctica se volvió **insostenible**.  

Por ello, **se abandonó la documentación individual**. Los archivos Markdown antiguos se han movido a `docs/evolution/deprecated-vos/` y **no se actualizan**. Se conservan únicamente como evidencia del proceso de aprendizaje.

## 📋 Listado de Value Objects activos

A continuación se listan los VOs activos del sistema, agrupados por módulo. Para conocer las validaciones concretas, **consultar directamente el código fuente** en los paquetes indicados.

### 🧑‍⚕️ Módulo Actor (`domain.actor.vo`)

| VO | Propósito |
|----|-----------|
| `Age` | Edad derivada desde fecha de nacimiento, con rangos y categorías |
| `BloodType` | Tipo de sangre (A+, A-, B+, etc.) |
| `DateOfBirth` | Fecha de nacimiento (no futura, ≤130 años) |
| `DentistAvailabilityStatus` | Estado operativo del odontólogo (AVAILABLE, SICK_LEAVE, VACATION) |
| `DentistId` | Identificador único de odontólogo |
| `Document` | Número de documento (cédula) con validación de formato |
| `FullName` | Nombre y apellido (no nulos, no vacíos) |
| `GuardianId` | Identificador único de responsable |
| `PatientId` | Identificador único de paciente |
| `Person` | Agregación de VOs de persona (dni, fullname, phone, address, etc.) |
| `ReceptionId` | Identificador único de recepcionista |
| `Sector` | Área administrativa del secretario (RECEPTION, BILLING, etc.) |
| `Specialties` | Conjunto de especialidades del odontólogo |
| `Specialty` | Especialidad individual (ORTHODONTICS, ORAL_SURGERY, etc.) |
| `TypeGuardian` | Rol del responsable (Madre, Padre, Tutor legal, etc.) |
| `WorkingHours` | Rango horario de atención (inicio, fin, día, horas declaradas) |

> **Nota:** `Address` y `PhoneNumber` son VOs globales (ver sección **Módulo Compartido**).

### 🔐 Módulo Autenticación (`domain.authentication.vo`)

| VO | Propósito |
|----|-----------|
| `HashedPassword` | Hash de contraseña (nunca texto plano) |
| `UserIdentityId` | Identificador de identidad de usuario |
| `UserIdentityName` | Nombre de usuario (longitud 3-30) |
| `UserIdentityStatus` | Estado del usuario (ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION) |

### 🏢 Módulo Administración / Contabilidad (`domain.administration.accounting.vo`)

| VO | Propósito |
|----|-----------|
| `AdministrativeReportId` | Identificador de reporte administrativo |
| `CompanyId` | Identificador de empresa |
| `CompanyStatus` | Estado de la empresa (ACTIVE, INACTIVE, SUSPENDED) |
| `ContractId` | Identificador de contrato/convenio |
| `Document` | Documento adjunto (nombre, url, tipo, tamaño) |
| `Indicator` | Indicador de gestión (nombre, valor, unidad) |
| `JournalEntryId` | Identificador de asiento contable |
| `LedgerAccountId` | Identificador de cuenta contable |
| `Nit` | NIT colombiano (con validación de formato) |
| `OpeningBalanceId` | Identificador de saldo inicial |
| `Period` | Período contable (fecha inicio y fin) |
| `ReportStatus` | Estado de reporte (DRAFT, UNDER_REVIEW, PUBLISHED, ARCHIVED) |
| `ThirdPartiesId` | Identificador de tercero |

### 🔐 Módulo Autorización (`domain.administration.authorization.vo`)

| VO | Propósito |
|----|-----------|
| `ActionCatalog` | Catálogo de acciones (CREATE, READ, UPDATE, DELETE, etc.) |
| `Permission` | Permiso (recurso + acción) |
| `ResourceCatalog` | Catálogo de recursos (DENTIST, PATIENT, INVOICE, etc.) |
| `RolId` | Identificador de rol |
| `SecurityContext` | Contexto de seguridad para autorización ABAC |
| `UserRolAssignmentId` | Identificador de asignación rol-usuario |

### 🏢 Módulo Operaciones (`domain.administration.operations.vo`)

| VO | Propósito |
|----|-----------|
| `ExcludedBlock` | Bloque de tiempo excluido dentro de un turno (almuerzo, reunión) |
| `ShiftId` | Identificador de turno operativo |
| `ShiftStatus` | Estado del turno (ACTIVE, COMPLETED, CANCELLED) |

### 💰 Módulo Facturación (`domain.billing.vo`)

| VO | Propósito |
|----|-----------|
| `CurrencyCode` | Código de moneda ISO 4217 (COP, USD, etc.) |
| `InvoiceId` | Identificador de factura |
| `InvoiceItemId` | Identificador de ítem de factura |
| `InvoiceNumber` | Número de factura (formato PREFIJO-NÚMERO) |
| `InvoiceStatus` | Estado de factura (DRAFT, PENDING, PAID, CANCELLED) |
| `ProviderId` | Identificador del proveedor/emisor (clínica) |
| `Quantity` | Cantidad de ítems (positiva, ≤1000) |
| `RateId` | Identificador de tarifa |

### 🦷 Módulo Servicios Odontológicos (`domain.dentalService.vo`)

| VO | Propósito |
|----|-----------|
| `AgeRange` | Rango de edad (minAge, maxAge) |
| `ServiceCatalog` | Catálogo de servicios con ID, nombre y categoría |
| `ServiceCode` | Código estandarizado del servicio (CUPS) |
| `ServiceDescription` | Descripción del servicio (mínimo 10 caracteres) |
| `ServiceDuration` | Duración del servicio (15-480 minutos) |
| `ServiceId` | Identificador de servicio |
| `ServiceName` | Nombre del servicio (predefinido o personalizado) |
| `ServiceStatus` | Estado del servicio (ACTIVE, INACTIVE, DEPRECATED) |

### 🦷 Módulo Servicios Odontológicos – Detalles de Composición (`domain.dentalService.model`)

Estos VOs estratégicos implementan la interfaz `ServiceDetails` y representan información específica por especialidad. Aunque residen en el paquete `model`, son Value Objects inmutables con validaciones propias.

| VO | Propósito |
|----|-----------|
| `AestheticDetails` | Detalles de servicios estéticos (blanqueamiento, carillas, etc.) |
| `ImplantologyDetails` | Detalles de implantología (cicatrización, injerto óseo, sitio) |
| `OrthodonticDetails` | Detalles de ortodoncia (tipo de aparato, duración, seguimiento) |
| `PediatricDetails` | Detalles de odontopediatría (rango de edad, manejo conductual, materiales) |
| `ProstheticDetails` | Detalles de prótesis (fija/removible, material, unidades) |
| `SurgicalDetails` | Detalles de cirugía (complejidad, anestesia, quirófano) |

### 💳 Módulo Pagos (`domain.payment.vo`)

| VO | Propósito |
|----|-----------|
| `Payer` | Quién realiza el pago (paciente, EPS, aseguradora, empresa) |
| `PaymentId` | Identificador de pago |
| `PaymentMethod` | Método de pago (CASH, CARD, STRIPE, EPS, CONTRACT, BANK_TRANSFER) |
| `PaymentStatus` | Estado del pago (PENDING, CONFIRMED, FAILED, REFUNDED, CANCELLED) |
| `TransactionReference` | Referencia de transacción externa (Stripe ID, comprobante, etc.) |

### 📅 Módulo Agenda (`domain.schedule.vo`)

| VO | Propósito |
|----|-----------|
| `AppointmentCompletion` | Finalización de cita (duración real + notas clínicas) |
| `AppointmentId` | Identificador de cita |
| `AppointmentStatus` | Estado de la cita (SCHEDULED, COMPLETED, CANCELLED, NO_SHOW) |
| `AppointmentType` | Tipo de cita (consulta, limpieza, cirugía, etc.) |

### 🧬 Módulo Tratamientos Clínicos (`domain.clinicalTreatments.vo`)

| VO | Propósito |
|----|-----------|
| `TreatmentId` | Identificador de tratamiento |
| `TreatmentPhase` | Fase de un tratamiento (nombre, fecha programada, estado, notas) |

### 🧩 Módulo Compartido (`domain.vo`)

| VO | Propósito |
|----|-----------|
| `Address` | Dirección postal (calle, ciudad, estado, país, código postal) |
| `AuditoriaInfo` | Información de auditoría (createdBy, createdAt, modifiedBy, modifiedAt) |
| `Email` | Correo electrónico con validación de formato y longitud |
| `Name` | Nombre genérico (longitud máxima 255, no vacío) |
| `Notes` | Notas/observaciones (longitud mínima opcional) |
| `PhoneNumber` | Número telefónico (formato internacional, 7-15 dígitos) |
| `Price` | Valor monetario con moneda y operaciones aritméticas |

## 🔍 Cómo consultar las validaciones

Cada VO implementa sus validaciones en el constructor o en métodos de fábrica estáticos (`of`, `from`, `valueOf`). Por ejemplo:

```java
// En DateOfBirth.java
private DateOfBirth(LocalDate value) {
    if (value == null) { throw ... }
    if (value.isAfter(LocalDate.now())) { throw ... }
    if (Period.between(value, LocalDate.now()).getYears() > 130) { throw ... }
    this.value = value;
}
```

Para entender una regla de negocio concreta, **busque el código fuente** del VO correspondiente en el paquete indicado.

## 📁 ¿Dónde está la documentación antigua?

Los archivos Markdown que documentaban VOs individualmente se han movido a `docs/evolution/deprecated-vos/` y **no se actualizan**. Se conservan únicamente como registro histórico del proceso de aprendizaje.

---

**Última actualización:** 2026-04-08  
**Mantenedor:** David Stiven Sanclemente


