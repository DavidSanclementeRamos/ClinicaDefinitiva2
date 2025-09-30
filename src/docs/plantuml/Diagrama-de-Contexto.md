@startuml
title Diagrama de Contexto - Sistema de Gestión Odontológica Exhibitable

' Actores principales
actor "👩‍⚕ Odontólogo" as Odontologo
actor "🧑‍💼 Administrador (Agregado)" as Administrador
actor "🧑‍🤝‍🧑 Paciente" as Paciente
actor "👨‍👩‍👦 Responsable del Paciente" as Responsable

' Sistemas externos
actor "📦 Sistema de Pagos (PayU)" as PayU
actor "📦 Sistema de Notificaciones (Twilio)" as Twilio
actor "📦 API de Correos (SendGrid)" as EmailAPI
actor "📦 API de Reportes (JasperReports)" as JasperAPI

' Caja negra del sistema
rectangle "🦷 Sistema de Gestión Odontológica" {
[🌐 Frontend Web] as Frontend
[⚙ Backend] as Backend
}

' Conexiones de actores con el sistema
Odontologo --> Frontend : • Consultar agenda\n• Registrar tratamientos\n• Acceder historial
Paciente --> Frontend : • Agenda citas\n• Consultar historial
Responsable --> Frontend : • Acceder información del paciente\n• Supervisar citas

' Administrador como agregado con funcionalidades variables
Administrador --> Frontend : • Gestionar pagos\n• Gestionar pacientes\n• Generar reportes\n• Asignar roles (según rol interno)

' Conexión Frontend ↔ Backend
Frontend --> Backend : Solicitudes de negocio

' Backend con sistemas externos
Backend --> PayU : Procesa transacciones
Backend --> Twilio : Envía notificaciones SMS
Backend --> EmailAPI : Envía correos electrónicos
Backend --> JasperAPI : Genera reportes PDF

@enduml