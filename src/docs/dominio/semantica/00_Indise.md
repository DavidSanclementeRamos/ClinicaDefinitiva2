# Índice Semántico del Dominio

Este índice agrupa los documentos que explican las diferencias conceptuales, motivaciones y roles semánticos de los objetos del dominio clínico. Cada archivo responde a una pregunta clave sobre la legitimidad y el propósito de los artefactos modelados.

---

## 📁 doc/dominio/semantica/

### ▸ diferencia-timeslot-workinghours.md
*Propósito*: Explica por qué TimeSlot es un Value Object estructural y WorkingHours representa un compromiso ético del profesional.  
*Pregunta que responde*: ¿Por qué no deben compartir responsabilidades ni lógica de validación?

---

### ▸ diferencia-weeklyavailability-workinghours.md
*Propósito*: Justifica la separación semántica entre la disponibilidad de agenda (WeeklyAvailability) y la jornada laboral declarada (WorkingHours).  
Incluye la reubicación del método isCompliantWith() y la introducción del atributo declaredHoursPerWeek.  
*Pregunta que responde*: ¿Dónde vive la regla de cumplimiento de jornada y por qué?

---

### ▸ motivacion-agregado-schedule.md
*Propósito*: Documenta la motivación ética y arquitectónica detrás de la creación del agregado Schedule.  
*Pregunta que responde*: ¿Por qué Schedule debe existir como agregado raíz y qué responsabilidades legítimas concentra?

---

## 🧭 Sugerencias de navegación

- Para validar reglas de negocio → ver diferencia-weeklyavailability-workinghours.md
- Para entender la estructura de tiempo → ver diferencia-timeslot-workinghours.md
- Para justificar el diseño del agregado → ver motivacion-agregado-schedule.md

---

## 📌 Convención

Cada documento responde a una *pregunta semántica clave* del dominio.  
Su propósito es reforzar la *legitimidad ética, estructural y exhibible* del sistema clínico.