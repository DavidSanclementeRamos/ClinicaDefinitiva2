# [Título del documento]

## Contexto
Describe brevemente el problema semántico, la ambigüedad o la necesidad de clarificación que motivó este documento.  
Ejemplos:
- Confusión entre dos objetos del dominio que parecen cumplir el mismo rol.
- Dificultad para ubicar una regla de negocio en el objeto correcto.
- Necesidad de justificar la existencia de un agregado o VO.

## Decisión
Explica la decisión tomada para resolver el problema semántico.  
Incluye:
- Separación de responsabilidades.
- Reubicación de métodos o atributos.
- Introducción de nuevos conceptos o propiedades.
- Justificación ética, estructural o funcional.

## Consecuencias
Enumera los efectos de la decisión:
- Qué objetos se simplifican.
- Qué reglas se vuelven más trazables.
- Qué ambigüedades se eliminan.
- Qué artefactos ganan legitimidad.

## Ejemplo de Código
Incluye fragmentos de código que ejemplifiquen la decisión tomada.  
Puedes mostrar:
- Antes y después de la reubicación.
- Integración entre objetos.
- Validación de reglas semánticas.

## Resumen
Una frase clara que sintetice la decisión y su impacto.  
Ejemplo:
> “Separar WorkingHours de WeeklyAvailability permite validar éticamente la jornada laboral sin contaminar la agenda con reglas contractuales.”