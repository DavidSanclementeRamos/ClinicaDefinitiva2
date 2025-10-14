
## 📄 ADR-015: Uso meticuloso de excepciones personalizadas
- Fecha: 2025-10-05
- Estado: Aprobado
- Contexto Entidad: Sistema clínico — validación semántica y ética de Value Objects
- Autor: David

## 🎯 Contexto
En el diseño de nuestro sistema clínico, cada Value Object (VO) encapsula reglas de legitimidad que deben ser trazables, exhibibles y éticamente justificadas. Las excepciones no son simples mecanismos de control de flujo, sino artefactos semánticos que:
* 	Representan la violación de una regla explícita del dominio.
* 	Permiten auditar el origen, la intención y el contexto de cada fallo.
* 	Refuerzan la trazabilidad entre entidades, reglas clínicas y decisiones arquitectónicas.

## ✅ Decisión
Se ha decidido utilizar excepciones personalizadas de forma meticulosa, separando los tipos de violaciones según su naturaleza:
* 	Ausencia técnica (null) vs. ausencia semántica (blank, empty)
* 	Violaciones de catálogo vs. violaciones de cardinalidad
* 	Errores de VO autónomos vs. errores orquestados por entidades
Cada excepción incluye:
* 	Un nombre exhibible y semántico (EmptySpecialtySetException, InvalidSpecialtyValueException, etc.)
* 	Un ContextoEntidad que declara quién legitima la regla.
* 	Un mensaje ético que puede ser internacionalizado y auditado.
* 	Una documentación que vincula la excepción con su regla, su ADR y su catálogo de errores.

## 🧠 Justificación
Esta decisión se fundamenta en los siguientes principios:
- Trazabilidad ética: Cada excepción declara su origen y su legitimidad.
- Exhibición semántica: Los nombres y mensajes son comprensibles para auditores, clínicos y desarrolladores.
- Reparación contextual: Diferenciar null de blank permite protocolos de reparación distintos.
- Internacionalización: Los mensajes pueden adaptarse a distintas culturas y jurisdicciones.
- Auditoría técnica: El sistema puede registrar con precisión qué regla fue violada y por quién.
## ⚠️ Alternativas consideradas
* 	Usar una sola excepción genérica (IllegalArgumentException): descartado por falta de trazabilidad y exhibición.
* 	Unificar errores técnicos y semánticos: descartado por ambigüedad en la reparación y en la legitimidad.

## 📘 Implicaciones
* 	Se requiere mantener un catálogo de excepciones vinculado a reglas y entidades.
* 	Las excepciones deben ser documentadas con su ContextoEntidad, su mensaje ético y su trazabilidad.
* 	Los desarrolladores deben seguir una convención clara para crear nuevas excepciones.

## 🧾 Convenciones para la creación de excepciones

- Crear una excepción nueva cuando:
    - La regla violada tiene una semántica distinta.
    - El `ContextoEntidad` cambia.
    - El mensaje ético requiere internacionalización específica.
- Reutilizar excepciones cuando:
    - La violación es idéntica en múltiples entidades.
    - La semántica y el mensaje son universales.