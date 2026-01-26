ADR: Duplicación vs Semántica en DTOs y VO

Contexto

En el módulo de actor se diseñó un VO global (PersonData) que encapsula atributos comunes (edad, dirección, teléfono, etc.) y se reutiliza en varios agregados. Esta decisión buscaba evitar duplicación y centralizar validaciones. Sin embargo, al usarlo en métodos de actualización, la semántica se volvió poco clara:

Los métodos esperan un objeto Person completo, aunque solo se actualicen algunos campos.

Los DTOs de la capa de aplicación contienen datos primitivos, lo que obliga a construir objetos artificiales.

La legibilidad de las operaciones se ve afectada, pues no es evidente qué campos cambian.

Decisión

Se prioriza legibilidad y semántica clara por encima de la eliminación total de duplicación. Se adoptan DTOs específicos por operación (ej. UpdateDentistSensitiveDto, UpdateDentistContactDto) y métodos de actualización con parámetros explícitos en el agregado. El VO global PersonData se mantiene como núcleo de validaciones, pero no se expone directamente en las operaciones de actualización.

Justificación

Legibilidad: Cada operación refleja de manera explícita qué atributos se modifican.

Semántica clara: Los métodos del agregado muestran la intención sin necesidad de armar objetos artificiales.

Trazabilidad: Los DTOs específicos facilitan la documentación y el mapeo entre capa de aplicación y dominio.

Mantenibilidad: Futuros colaboradores entenderán la intención de cada operación sin ambigüedad.

Consecuencias

Positivas:

Mayor claridad en el código y en la documentación.

Operaciones más expresivas y fáciles de auditar.

Refuerzo de la semántica en dominios sensibles (ej. clínicos).

Negativas:

Mayor duplicación de campos en distintos DTOs.

Incremento en el número de clases y métodos.

Alternativas consideradas

VO global en todas las operaciones: Menos duplicación, pero semántica confusa y DTOs artificiales.

Híbrido: Mantener VO global y definir métodos con parámetros explícitos. Requiere más código, pero mejora la claridad.

Estado

Aceptado. Se implementará la estrategia de DTOs específicos por operación y métodos de actualización con parámetros explícitos, manteniendo PersonData como núcleo de validaciones internas.

Lecciones

En dominios sensibles, la claridad semántica y la legibilidad pesan más que la reducción de duplicación. La duplicación controlada es un costo aceptable frente a la confusión que puede generar un diseño demasiado genérico.