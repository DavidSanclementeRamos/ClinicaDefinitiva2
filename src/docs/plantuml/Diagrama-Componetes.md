@startuml
title Diagrama de Componentes - Backend (con namespaces calificados)

package "🟦 Dominio" {

' --- Actor ---
package "Actor" {
package "Actor.vo" {
[Edad]
[Sexo]
}
package "Actor.model" {
[Paciente]
[Odontologo]
}
package "Actor.dto" {
[PacienteDTO]
}
package "Actor.enum" {
[TipoDocumento]
}
package "Actor.exception" {
[PacienteNoEncontrado]
}
}

' --- Administración ---
package "Administración" {
package "Administración.vo" {
[Rol]
}
package "Administración.model" {
[Administrador]
}
package "Administración.dto" {
[RolDTO]
}
package "Administración.enum" {
[TipoRol]
}
package "Administración.exception" {
[RolInvalido]
}
}

' --- Identity ---
package "Identity" {
package "Identity.vo" {
[Usuario]
}
package "Identity.model" {
[Credenciales]
}
package "Identity.dto" {
[UsuarioDTO]
}
package "Identity.enum" {
[EstadoUsuario]
}
package "Identity.exception" {
[UsuarioNoAutenticado]
}
}

' --- Schedule ---
package "Schedule" {
package "Schedule.vo" {
[FechaHora]
}
package "Schedule.model" {
[Cita]
[Appointment]
[TimeSlot]
}
package "Schedule.dto" {
[CitaDTO]
}
package "Schedule.enum" {
[EstadoCita]
}
package "Schedule.exception" {
[CitaNoDisponible]
}
}

' --- Servicios de dominio transversales ---
[Motor de Reglas Clínicas]
[Catálogo de Errores]
}
' =========================
' Aplicación
' =========================
package "🟨 Aplicación" {
[Servicio de Validación]
[Servicio de Gestión de Roles]
[Servicio de Gestión de Pacientes]
[Servicio de Gestión de Pagos]
[Servicio de Gestión de Reportes]
[Servicio de Notificaciones]
}

' =========================
' Infraestructura
' =========================
package "🟫 Infraestructura" {
[Repositorio de Pacientes]
[Repositorio de Pagos]
[Repositorio de Roles]
[API Pagos (PayU)]
[API Notificaciones (Twilio)]
[API Correos (SendGrid)]
[API Reportes (JasperReports)]
}

' =========================
' Relaciones
' =========================
[Servicio de Validación] --> [Edad]
[Servicio de Validación] --> [Sexo]
[Servicio de Validación] --> [Usuario]
[Servicio de Validación] --> [FechaHora]

[Servicio de Gestión de Pacientes] --> [Paciente]
[Servicio de Gestión de Pacientes] --> [Repositorio de Pacientes]
[Servicio de Gestión de Pacientes] --> [Motor de Reglas Clínicas]

[Servicio de Gestión de Roles] --> [Administrador]
[Servicio de Gestión de Roles] --> [Repositorio de Roles]

[Servicio de Gestión de Pagos] --> [Repositorio de Pagos]
[Servicio de Gestión de Pagos] --> [API Pagos (PayU)]

[Servicio de Gestión de Reportes] --> [API Reportes (JasperReports)]

[Servicio de Notificaciones] --> [API Notificaciones (Twilio)]
[Servicio de Notificaciones] --> [API Correos (SendGrid)]

[Motor de Reglas Clínicas] --> [Edad]
[Motor de Reglas Clínicas] --> [Sexo]
[Motor de Reglas Clínicas] --> [Catálogo de Errores]

@enduml




@enduml