
## 📄 ADR-01: Migración progresiva a arquitectura hexagonal por problemas de cohesión y acoplamiento
Estado: En progreso
Fecha: 2025-08-25
Autor: David

## 🎯 Contexto
Este proyecto clínico nació como un ejercicio personal de aprendizaje y evolución profesional. Inicialmente fue desarrollado con un enfoque más tradicional, inspirado en cursos y ejemplos prácticos. Con el tiempo, fui incorporando mejoras como DTOs, mapeadores, validaciones y excepciones personalizadas.
Sin embargo, al intentar agregar nuevas funcionalidades (nuevos servicios, reglas clínicas y entidades), me encontré con una dificultad recurrente: no sabía dónde ubicar las nuevas clases dentro de la estructura existente. Esto reveló un problema más profundo: baja cohesión y alto acoplamiento en el diseño actual.

## ⚠️ Problema detectado
• 	La estructura de paquetes no ofrecía un criterio claro para ubicar nuevas clases.
• 	Las responsabilidades estaban mezcladas entre capas técnicas y de dominio.
• 	Cada nueva funcionalidad aumentaba el acoplamiento, dificultando la evolución del sistema.
• 	La falta de separación semántica hacía que el modelo clínico se diluyera entre detalles técnicos.
En resumen, el sistema no estaba preparado para crecer de manera legítima y ordenada.

## ✅ Decisión
He decidido migrar progresivamente el sistema hacia una arquitectura hexagonal (Ports & Adapters).
La migración no se realizará de una sola vez, sino poco a poco, mientras aprendo y refactorizo. Cada paso busca mejorar la cohesión, reducir el acoplamiento y dar un lugar legítimo a cada clase dentro de la estructura.

## 🧠 Justificación semántica y arquitectónica
• 	Separación clara de capas: dominio, aplicación e infraestructura tendrán límites definidos.
• 	Cohesión semántica: cada agregado clínico podrá expresar sus reglas sin depender de detalles técnicos.
• 	Evolución legítima: nuevas funcionalidades podrán incorporarse sin romper la estructura existente.
• 	Trazabilidad ética: cada decisión quedará documentada como ADR, vinculada a reglas clínicas y excepciones.
• 	Aprendizaje progresivo: la migración es también un proceso personal de formación y consolidación de buenas prácticas.

## 📘 Impacto esperado
• 	Refactorización gradual de entidades y Value Objects hacia un núcleo de dominio independiente.
• 	Creación de puertos para servicios externos (pagos, notificaciones, informes).
• 	Reubicación de clases en capas legítimas, reduciendo acoplamiento.
• 	Consolidación de catálogos de errores y excepciones clínicas dentro del dominio.
• 	Documentación viva de cada decisión arquitectónica.

## 🔄 Consecuencias
• 	La migración tomará tiempo, ya que se realiza en paralelo con el aprendizaje.
• 	Algunas partes del sistema convivirán temporalmente entre el diseño anterior y el nuevo.
• 	Habrá refactorizaciones intermedias que pueden generar inestabilidad momentánea.
• 	El resultado final será un sistema más exhibible, trazable y preparado para crecer.


