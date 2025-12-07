# Dominio del Sistema Odontológico

Este directorio documenta las decisiones y justificaciones relacionadas con el **modelo de dominio clínico**.  
El objetivo es mostrar cómo se descubrieron, consolidaron y organizaron las reglas de negocio, excepciones y conceptos semánticos que sustentan el sistema.

## Estructura

- **Reglas de negocio (`reglas-de-negocio/`)**  
  Catálogo oficial de reglas que gobiernan los agregados del sistema (Paciente, Cita, Factura, Pago, Permiso).  
  Cada archivo describe la regla, su justificación y el impacto en el flujo clínico.  
  Ejemplo: *“Una cita no puede ser facturada si no ha sido confirmada por el paciente”*.

- **Exploraciones (`descubrimientos-de-reglas/`)**  
  Registro histórico de reglas identificadas durante el proceso de descubrimiento.  
  Muestra cómo evolucionó la comprensión del dominio antes de consolidar las reglas definitivas.  
  Ejemplo: *“Inicialmente se pensó que las citas podían ser facturadas automáticamente, luego se descubrió la necesidad de confirmación”*.

- **Catálogo de errores (`catalogo-de-error/`)**  
  Justificación de cada excepción del dominio.  
  Incluye: nombre, mensaje, descripción clínica, caso de uso y relación con el código.  
  Ejemplo: *`PacienteNoEncontradoException` → se lanza cuando se intenta agendar una cita para un paciente inexistente*.

- **Glosario semántico (`glosario-semantico/`)**  
  Diferenciación de conceptos que parecen similares pero tienen significados distintos en el contexto clínico.  
  Ejemplo: *“Factura” vs. “Recibo”* o *“Pago” vs. “Abono”*.  
  Este glosario evita ambigüedades y asegura precisión semántica.

- **Value Objects (`vo/`)**  
  Documentación de objetos de valor clave en el dominio.  
  Cada archivo describe: propósito, motivación, estructura, reglas encapsuladas, uso en el módulo, ventajas y proyección.  
  Ejemplo: *`EmailPacienteVO`* encapsula validaciones y semántica del correo electrónico del paciente.

## Cómo leer esta sección

1. **Empieza por las reglas de negocio** para entender las restricciones clínicas.
2. **Consulta las exploraciones** para ver el proceso de descubrimiento y evolución.
3. **Revisa el catálogo de errores** para comprender cómo se manejan las excepciones en el dominio.
4. **Explora el glosario semántico** para evitar confusiones conceptuales.
5. **Analiza los Value Objects** para ver cómo se encapsulan reglas y semántica en el código.

## Convenciones

- Archivos en formato Markdown (`.md`).
- Cada documento incluye contexto, justificación y ejemplos prácticos.
- Los nombres de carpetas reflejan su propósito narrativo:
    - `reglas-de-negocio` → catálogo vigente
    - `exploraciones` → histórico de descubrimientos
    - `glosario-semantico` → diferenciaciones conceptuales
    - `catalogo-de-error` → excepciones justificadas
    - `vo` → objetos de valor documentados