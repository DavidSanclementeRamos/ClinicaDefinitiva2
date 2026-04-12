@startuml
title Diagrama de Contenedores - Sistema de Gestión Odontológica (Estado Actual)

actor "👩‍⚕ Odontólogo" as Odontologo
actor "🧑‍💼 Administrador" as Administrador
actor "🧑‍🤝‍🧑 Paciente" as Paciente
actor "👨‍👩‍👦 Responsable del Paciente" as Responsable

' Sistema externo real
actor "💳 Stripe" as Stripe

' Contenedores internos del sistema
node "🦷 Sistema de Gestión Odontológica" {
  [🌐 Frontend Web] as Frontend
  [⚙ Backend API] as Backend
  database "📂 Base de Datos" as DB
  [📄 Documentación] as Docs
}

' Relaciones de actores con el sistema
Odontologo --> Frontend : Usa interfaz web\n(consultas, agenda, tratamientos)
Paciente --> Frontend : Agenda citas\nConsulta historial
Responsable --> Frontend : Supervisa citas\nAccede información del paciente
Administrador --> Frontend : Gestiona pagos, pacientes,\nreportes, asigna roles

' Conexiones internas
Frontend --> Backend : Solicitudes HTTP/REST
Backend --> DB : CRUD de agregados y VOs
Backend --> Docs : Acceso a documentación

' Conexiones externas reales
Backend --> Stripe : Procesa pagos (API)

note right of Frontend
  El frontend web está planificado.
  Actualmente la API se consume directamente
  o mediante herramientas como Postman/Swagger.
end note

' Nota sobre integraciones futuras
note bottom of Stripe
  Stripe es la única integración externa implementada.
  PayU, Twilio, SendGrid, JasperReports están planificadas
  como futuras contribuciones (ver CONTRIBUTING.md).
end note

@enduml