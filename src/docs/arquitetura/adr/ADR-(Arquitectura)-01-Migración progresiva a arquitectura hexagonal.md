
# ADR-01 (Arquitectura): Migración progresiva a arquitectura hexagonal

- **Estado:** En progreso
- **Fecha:** 2025-08-25
- **Autor:** David

## Contexto
El sistema odontológico fue inicialmente desarrollado con un enfoque tradicional, inspirado en cursos y ejemplos prácticos. Con el tiempo se incorporaron mejoras (DTOs, mapeadores, validaciones, excepciones personalizadas).  
Sin embargo, al intentar agregar nuevas funcionalidades (servicios, reglas clínicas, entidades), surgió un problema: **baja cohesión y alto acoplamiento**. La estructura de paquetes no ofrecía un criterio claro para ubicar nuevas clases, y las responsabilidades estaban mezcladas entre capas técnicas y de dominio.

## Problema detectado
- Dificultad para ubicar nuevas clases dentro de la estructura existente.
- Mezcla de responsabilidades entre capas técnicas y de dominio.
- Cada nueva funcionalidad aumentaba el acoplamiento.
- El modelo clínico se diluía entre detalles técnicos.

## Decisión
Migrar progresivamente el sistema hacia una **arquitectura hexagonal (Ports & Adapters)**.  
La migración será gradual, en paralelo con el aprendizaje y refactorización, buscando mejorar cohesión, reducir acoplamiento y dar un lugar legítimo a cada clase.

## Justificación semántica y arquitectónica
- Separación clara de capas: dominio, aplicación e infraestructura.
- Cohesión semántica: cada agregado clínico expresa sus reglas sin depender de detalles técnicos.
- Evolución legítima: nuevas funcionalidades se incorporan sin romper la estructura.
- Trazabilidad ética: cada decisión queda documentada como ADR.
- Aprendizaje progresivo: la migración es también un proceso personal de formación.

## Impacto esperado
- Refactorización gradual de entidades y Value Objects hacia un núcleo de dominio independiente.
- Creación de puertos para servicios externos (pagos, notificaciones, informes).
- Reubicación de clases en capas legítimas, reduciendo acoplamiento.
- Consolidación de catálogos de errores y excepciones clínicas dentro del dominio.
- Documentación viva de cada decisión arquitectónica.

## Consecuencias
- La migración tomará tiempo y convivirá con partes del diseño anterior.
- Habrá refactorizaciones intermedias que pueden generar inestabilidad momentánea.
- El resultado final será un sistema más exhibible, trazable y preparado para crecer.