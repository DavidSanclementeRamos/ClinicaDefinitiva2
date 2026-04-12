@startuml
title Diagrama de Componentes - Backend (Arquitectura Hexagonal - Estado Actual)

' ========================================
' CAPA DE DOMINIO
' ========================================
package "🔵 Dominio" {

  package "Agregados y VOs" {
    [Actor] as Actor
    [Authentication] as Auth
    [Billing] as Billing
    [DentalService] as Dental
    [Schedule] as Schedule
    [Administration] as Admin
    [ClinicalTreatments] as Clinical
    note right of Actor : Dentist, Patient, Guardian, Receptionist\n+ VOs (Age, FullName, etc.)
    note right of Billing : Invoice, Rate, Payment\n+ VOs (InvoiceNumber, etc.)
    note right of Dental : ProvidedService, ServiceDetails\n+ VOs (ServiceCode, etc.)
    note right of Schedule : Appointment, Shift\n+ VOs (AppointmentStatus, etc.)
    note right of Admin : Accounting, Authorization, Operations\n+ VOs (ReportStatus, etc.)
    note right of Clinical : Treatment, TreatmentPhase
  }

  package "Servicios de Dominio y Políticas" {
    [InvoiceDomainService]
    [InvoiceItemFactoryService]
    [ServiceRatePolicy]
    [ServiceDeactivationValidator]
    [UserAccessValidator]
    [UserDeactivationPolicy]
    [AuthorizationService]
    [DentistDeactivationValidator]
    [PatientDeactivationValidator]
  }

  package "Excepciones y Catálogos" {
    [BusinessRuleViolationException]
    [DomainAggregateException]
    [ValueObjectValidationException]
    [AggregateBusinessRuleViolationException]
    [ErrorCatalog (por agregado)]
  }

  package "Puertos de Salida (Interfaces)" {
    [Repository Interfaces]
    [ExternalService Ports]
    note right : PatientRepository, InvoiceRepository,\nRateRepository, etc.
  }
}

' ========================================
' CAPA DE APLICACIÓN
' ========================================
package "🟡 Aplicación" {

  package "Casos de Uso (Input Ports)" {
    [PatientUseCase]
    [DentistUseCase]
    [InvoiceUseCase]
    [ProvidedServiceUseCase]
    [ShiftUseCase]
    [AuthenticationUseCase]
    [AuthorizationUseCase]
    [UserIdentityUseCase]
    [CompanyUseCase]
    [ContractUseCase]
    [JournalEntryUseCase]
    note right : Interfaz por cada agregado
  }

  package "Servicios de Aplicación" {
    [PatientApplicationService]
    [InvoiceApplicationService]
    [ProvidedServiceApplicationService]
    [ShiftApplicationService]
    [AuthenticationService]
    [AuthorizationHelper]
    [CompanyApplicationService]
    [ContractApplicationService]
    [JournalEntryApplicationService]
    note right : Implementan los casos de uso
  }

  package "DTOs y Mappers de Aplicación" {
    [CreatePatientDto, UpdateContactDto, etc.]
    [PatientReadMapper]
    [PatientCreateAssembler]
    [InvoiceReadMapper]
    [InvoiceWriteMapper]
    [ProvidedServiceReadMapper]
    [ProvidedServiceWriteMapper]
    [ShiftReadMapper]
    [ShiftWriteMapper]
    note right : Mappers específicos por operación
  }
}

' ========================================
' CAPA DE INFRAESTRUCTURA
' ========================================
package "🟫 Infraestructura" {

  package "Adaptadores de Entrada (REST)" {
    [PatientController]
    [DentistController]
    [InvoiceController]
    [ProvidedServiceController]
    [ShiftController]
    [AuthenticationController]
    [CompanyController]
    [ContractController]
    [JournalEntryController]
    [GlobalControllerAdvice]
    note right : Manejo de excepciones y respuestas HTTP
  }

  package "Seguridad" {
    [JwtAuthenticationFilter]
    [CustomUserDetailsService]
    [SecurityConfig]
    [RequiresPermission Annotation]
    [AuthorizationInterceptor]
    note right : Spring Security + JWT
  }

  package "Adaptadores de Salida (Persistencia)" {
    [JpaPatientRepository]
    [JpaInvoiceRepository]
    [JpaProvidedServiceRepository]
    [JpaShiftRepository]
    [JpaCompanyRepository]
    [JpaContractRepository]
    [JpaJournalEntryRepository]
    [JpaRoleRepository]
    [JpaUserIdentityRepository]
    note right : Implementan los puertos de salida
  }

  package "Adaptadores de Salida (Externos)" {
    [StripeAdapter]
    note right : Solo Stripe está integrado actualmente.\nPayU, Twilio, SendGrid, JasperReports\nson futuros (ver CONTRIBUTING.md)
  }
}

' ========================================
' RELACIONES PRINCIPALES
' ========================================

' Capa de presentación → Aplicación
PatientController --> PatientUseCase
InvoiceController --> InvoiceUseCase
ProvidedServiceController --> ProvidedServiceUseCase
ShiftController --> ShiftUseCase
AuthenticationController --> AuthenticationUseCase

' Aplicación → Dominio (puertos de salida)
PatientApplicationService --> [Repository Interfaces]
InvoiceApplicationService --> [Repository Interfaces]
InvoiceApplicationService --> InvoiceDomainService
InvoiceApplicationService --> InvoiceItemFactoryService
ProvidedServiceApplicationService --> [Repository Interfaces]
ProvidedServiceApplicationService --> ServiceRatePolicy
ProvidedServiceApplicationService --> ServiceDeactivationValidator
ShiftApplicationService --> [Repository Interfaces]
AuthenticationService --> [Repository Interfaces]
AuthenticationService --> UserAccessValidator

' Servicios de dominio entre sí
UserDeactivationPolicy --> DentistDeactivationValidator
UserDeactivationPolicy --> PatientDeactivationValidator
AuthorizationService --> [Authorization Policies]

' Infraestructura externa
StripeAdapter --> [ExternalService Ports]

' Nota sobre integraciones pendientes
note bottom of StripeAdapter : Stripe es la única integración externa real.\nPayU, Twilio, SendGrid, JasperReports\nestán planificadas como contribuciones.

@enduml