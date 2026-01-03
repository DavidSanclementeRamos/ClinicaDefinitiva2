

# 📄 ADR 27 (Arquitectura): Relación entre descubrimientos de reglas y ADRs formales

Título: Manejo de descubrimientos de reglas frente a ADRs formales  
Estado: Accepted  
Fecha: 01 de enero de 2026

---

## Contexto
Durante el desarrollo de los módulos del sistema (ej. actor, schedule, dental.care.service), se generaron archivos llamados descubrimientos de reglas. Estos documentos tenían como propósito capturar hipótesis, ideas y observaciones iniciales sobre el comportamiento de cada agregado.

Con el tiempo, muchas de esas reglas quedaron obsoletas, se postergaron o fueron refinadas. En paralelo, se comenzaron a redactar ADRs (Architecture Decision Records), que tienen un carácter más formal: describen el contexto, las alternativas evaluadas, la decisión tomada y sus consecuencias.

El problema surge cuando las decisiones actuales contradicen o omiten reglas previamente registradas en los descubrimientos. Esto genera la duda de si se “ve mal” documentar ADRs que no siguen al pie de la letra lo establecido en esos archivos exploratorios.

---

## Problema
- Los descubrimientos de reglas reflejan aprendizaje y exploración inicial, no decisiones definitivas.
- Los ADRs reflejan decisiones arquitectónicas maduras y formales.
- Sin una relación clara entre ambos, puede parecer que hubo inconsistencias o errores no reconocidos.

---

## Soluciones evaluadas

Opción A: Mantener descubrimientos y ADRs separados sin relación explícita
- Los descubrimientos se conservan como notas históricas.
- Los ADRs se redactan sin referencia a ellos.

Ventajas:
- Simplicidad documental.
- No requiere esfuerzo adicional.

Desventajas:
- Puede generar confusión: ¿por qué las reglas iniciales no coinciden con las decisiones finales?
- Exhibición débil: parece falta de trazabilidad.

---

Opción B: Referenciar descubrimientos en los ADRs
- Cada ADR incluye una nota breve sobre descubrimientos previos: cuáles fueron descartados, refinados o reemplazados.
- Los descubrimientos se mantienen como evidencia del proceso de aprendizaje.

Ventajas:
- Transparencia: se reconoce la evolución del pensamiento.
- Exhibible: muestra madurez y capacidad de aprendizaje.
- Refuerza trazabilidad entre exploración y decisión.

Desventajas:
- Requiere esfuerzo adicional en redacción.
- Puede alargar los ADRs.

---

Opción C: Usar estados en ADRs para marcar relación con descubrimientos
- ADRs pueden tener estados como Proposed, Accepted, Superseded, Deprecated.
- Se marcan explícitamente las decisiones que reemplazan descubrimientos previos.

Ventajas:
- Formaliza la evolución.
- Exhibible: muestra disciplina en gestión de decisiones.
- Evita confusión sobre qué está vigente.

Desventajas:
- Requiere mantener estados actualizados.
- Puede ser más complejo para lectores no técnicos.

---

## Decisión
Se adopta una combinación de Opción B y Opción C:
- Los ADRs referenciarán descubrimientos previos cuando sea relevante, aclarando qué fue descartado o refinado.
- Se usarán estados en ADRs (Superseded, Recorded after implementation) para marcar la relación con descubrimientos.
- Los descubrimientos se conservarán como evidencia del proceso de aprendizaje, pero no representan el estado actual del sistema.

---

Consecuencias
- Positivas:
    - Transparencia y trazabilidad entre exploración y decisión.
    - Exhibición profesional: muestra madurez y capacidad de aprendizaje.
    - Claridad para reclutadores y colegas: se entiende la evolución del sistema.
- Negativas:
    - Mayor esfuerzo documental.
    - Necesidad de disciplina para mantener estados y referencias actualizadas.

---
