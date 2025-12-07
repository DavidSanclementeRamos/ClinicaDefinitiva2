# Documentación del Sistema Odontológico

Este directorio contiene toda la documentación estratégica y táctica del sistema odontológico.  
El objetivo es guiar al lector desde la visión arquitectónica general hasta los detalles del dominio clínico y los diagramas técnicos.

## Estructura General

- **Arquitectura (`arquitectura/`)**  
  Documenta las decisiones arquitectónicas y las vistas del sistema según el modelo C4.
    - `contexto/`: muestra actores externos (pacientes, odontólogos, proveedores de pago, servicios de notificación).
    - `contenedores/`: descompone el sistema en módulos principales (citas, pagos, facturación, contabilidad, permisos).
    - `componentes/`: detalla responsabilidades internas de cada módulo.
    - `adr/`: catálogo de *Architecture Decision Records* que justifican las decisiones clave.

- **Dominio (`dominio/`)**  
  Documenta las reglas de negocio, excepciones y conceptos semánticos del sistema clínico.
    - `reglas-de-negocio/`: catálogo vigente de reglas que gobiernan los agregados.
    - `exploraciones/`: histórico de descubrimientos y aprendizajes durante el modelado.
    - `catalogo-de-error/`: justificación de excepciones del dominio con contexto clínico.
    - `glosario-semantico/`: diferenciación de conceptos clínicos similares (ej. factura vs recibo).
    - `vo/`: documentación de Value Objects con propósito, reglas encapsuladas y proyección.

- **PlantUML (`plantUml/`)**  
  Código fuente de los diagramas de contexto, contenedores y componentes.  
  Permite regenerar las imágenes arquitectónicas de forma reproducible.

## Cómo navegar la documentación

1. **Empieza por `arquitectura/`** para entender la visión general del sistema.
2. **Explora `dominio/`** para comprender las reglas clínicas y las justificaciones semánticas.
3. **Consulta `plantUml/`** si deseas regenerar o modificar los diagramas.
4. Usa los **ADR** como referencia para entender las decisiones estratégicas y su impacto.

## Convenciones

- Archivos en formato Markdown (`.md`) para decisiones y reglas.
- Diagramas generados con [PlantUML](https://plantuml.com/).
- Cada carpeta incluye un `README.md` que explica su propósito y cómo leerla.  