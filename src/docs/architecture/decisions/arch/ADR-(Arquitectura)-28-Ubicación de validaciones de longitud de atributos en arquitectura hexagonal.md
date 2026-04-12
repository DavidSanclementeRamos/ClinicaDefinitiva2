

# ADR-28 (Arquitectura): Ubicación de validaciones de longitud de atributos en arquitectura hexagonal

- Estado: Aprobado
- Fecha: 2026-01-05
- Autor: David Stiven Sanclemente



## Contexto
En el diseño de sistemas bajo arquitectura hexagonal, surge la duda sobre dónde deben residir las validaciones de longitud de atributos (ejemplo: nombre con mínimo de caracteres).  
Existen dos posibles ubicaciones:

- **Dominio (VO/Entidad)**: cuando la longitud mínima o máxima es parte de una regla de negocio o invariante semántica.
- **Aplicación/DTO**: cuando la longitud responde a restricciones técnicas (ej. límite de base de datos, formato de entrada) y puede validarse con bibliotecas externas (ej. Bean Validation).

## Decisión
- Las **validaciones de longitud que representan reglas de negocio** (ejemplo: “El nombre de un servicio debe tener al menos 3 caracteres para ser clínicamente válido”) se implementarán en el **dominio**, dentro de VO o entidades, lanzando excepciones de negocio (`BusinessRuleViolationException`).
- Las **validaciones de longitud que representan restricciones técnicas o de infraestructura** (ejemplo: “El campo no debe exceder 255 caracteres porque la columna es VARCHAR(255)”) se implementarán en la **capa de aplicación**, típicamente en DTOs o adaptadores, usando bibliotecas externas de validación.

## Justificación
- **Dominio**: garantiza que el modelo nunca pueda existir en un estado inválido desde la perspectiva del negocio, sin importar el origen de la creación (API, batch, persistencia).
- **Aplicación/DTO**: evita sobrecargar el dominio con reglas técnicas y permite aprovechar librerías estándar para validaciones de formato, longitud máxima o restricciones externas.
- **Separación de responsabilidades**: se mantiene la claridad entre invariantes de negocio y restricciones técnicas, alineado con los principios de arquitectura hexagonal.

## Consecuencias
- **Positivas**:
    - Mayor coherencia semántica en el dominio.
    - Claridad en la documentación de reglas de negocio vs. restricciones técnicas.
    - Flexibilidad para usar validadores externos en DTOs sin afectar el modelo.
- **Negativas**:
    - Requiere disciplina para distinguir entre regla de negocio y restricción técnica.
    - Posible duplicación de validaciones si no se documenta adecuadamente.

