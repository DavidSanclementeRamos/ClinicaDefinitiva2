ADR: Estrategia de Null en Mappers Clínicos
## 25/01/2026
Contexto

En el diseño de mappers entre objetos de dominio y entidades JPA, surge la decisión de cómo manejar valores null. En particular, se comparan dos enfoques:

Mapper defensivo: valida null en cada campo antes de asignar.

Mapper estricto: asume que el dominio garantiza invariantes y no valida null.

Decisión

Se adopta el mapper estricto como estrategia principal.

Justificación

Dominio fuerte (DDD): Los agregados y value objects clínicos deben garantizar consistencia. Ejemplo: un Fullname nunca debe existir con null en FirstName.

Transparencia ética: Si un campo obligatorio llega null, debe considerarse una violación clínica/operacional y registrarse como error, no ignorarse silenciosamente.

Trazabilidad: Los errores por null inesperados se documentan en el catálogo de errores, reforzando la claridad y la responsabilidad.

Mantenibilidad: El código del mapper es más limpio y menos repetitivo.

Consecuencias

Positivas:

Código más simple y directo.

Se detectan rápidamente violaciones de invariantes.

Refuerza la disciplina de validación en el dominio.

Negativas:

Menor tolerancia a datos incompletos.

Requiere que el dominio esté bien diseñado y validado antes de persistir.

Alternativas consideradas

Mapper defensivo: útil en sistemas legacy o cuando los datos externos son poco confiables. Sin embargo, puede ocultar errores clínicos y generar entidades incompletas.

Estado

Aceptado.

Referencias

Catálogo de errores clínicos.

ADR sobre validación de invariantes en agregados.

Documentación de mappers en módulos de Aesthetic e Implantology.