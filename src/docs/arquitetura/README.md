# Arquitectura del Sistema Odontológico

Este directorio documenta la arquitectura del sistema odontológico, siguiendo el modelo C4.  
El objetivo es mostrar cómo evoluciona la visión arquitectónica desde el **contexto general** hasta los **componentes internos**, junto con las decisiones que guiaron su diseño.

## Estructura

- **Contexto (`contexto/`)**  
  Diagrama de alto nivel que muestra el sistema en relación con actores externos: pacientes, odontólogos, personal administrativo, proveedores de pago y servicios de notificación.  
  Responde a la pregunta: *¿quién interactúa con el sistema y con qué propósito?*

- **Contenedores (`contenedores/`)**  
  Diagrama que descompone el sistema en aplicaciones y servicios principales:
    - Módulo de citas
    - Módulo de pagos y facturación
    - Módulo de contabilidad
    - Módulo de notificaciones
    - Módulo de permisos y seguridad  
      Responde a la pregunta: *¿qué piezas grandes conforman el sistema y cómo se comunican?*

- **Componentes (`componentes/`)**  
  Diagrama que detalla los principales componentes dentro de cada módulo.  
  Ejemplo: en el módulo de citas se distinguen componentes como *Gestor de Agenda*, *Validación de Disponibilidad* y *Generador de Recordatorios*.  
  Responde a la pregunta: *¿cómo se organizan las responsabilidades internas de cada aplicación?*

- **Decisiones (`adr/`)**  
  Conjunto de *Architecture Decision Records* en formato Markdown.  
  Cada ADR documenta una decisión clave, su contexto, alternativas y consecuencias.  
  Ejemplo: separación del módulo de pagos de la contabilidad, o elección de un sistema de notificaciones desacoplado.  
  Responde a la pregunta: *¿por qué se tomó esta decisión y qué impacto tiene?*

## Cómo leer esta sección

1. **Empieza por el diagrama de contexto** para entender el alcance del sistema odontológico.
2. **Avanza al diagrama de contenedores** para ver la arquitectura lógica y tecnológica.
3. **Revisa los componentes** para comprender la organización interna de cada módulo.
4. **Consulta los ADR** para conocer las decisiones que guiaron la evolución del diseño.

## Convenciones

- Diagramas generados con [PlantUML](https://plantuml.com/).
- Cada carpeta contiene un archivo de imagen (`.png`) y su fuente (`.puml`).
- Los ADR siguen la plantilla estándar: *Contexto, Decisión, Consecuencias, Estado*.  