@startuml
title Diagrama de Contenedores - Sistema de Gestión Odontológica

actor "👩‍⚕ Odontólogo" as Odontologo
actor "🧑‍💼 Administrador (Agregado)" as Administrador
actor "🧑‍🤝‍🧑 Paciente" as Paciente
actor "👨‍👩‍👦 Responsable del Paciente" as Responsable

' Sistemas externos
actor "📦 Sistema de Pagos (PayU)" as PayU
actor "📦 Sistema de Notificaciones (Twilio)" as Twilio
actor "📦 API de Correos (SendGrid)" as EmailAPI
actor "📦 API de Reportes (JasperReports)" as JasperAPI

' Contenedores internos del sistema
node "🦷 Sistema de Gestión Odontológica" {
[🌐 Frontend Web] as Frontend
[⚙ Backend API] as Backend
database "📂 Base de Datos" as DB
[📄 Documentación Ética] as Docs
}

' Relaciones de actores con el sistema
Odontologo --> Frontend : Usa interfaz web\n(consultas, agenda, tratamientos)
Paciente --> Frontend : Agenda citas\nConsulta historial
Responsable --> Frontend : Supervisa citas\nAccede información del paciente
Administrador --> Frontend : Gestiona pagos, pacientes,\nreportes, asigna roles

' Conexiones internas
Frontend --> Backend : Solicitudes HTTP/REST
Backend --> DB : CRUD de pacientes, pagos, citas
Backend --> Docs : Acceso a trazabilidad

' Conexiones externas
Backend --> PayU : Procesa transacciones
Backend --> Twilio : Envía notificaciones SMS
Backend --> EmailAPI : Envía correos electrónicos
Backend --> JasperAPI : Genera reportes PDF

@enduml