# Temas de estudio para comprender el modelo administrativo

- Fecha: 2025-11-17
- Autor: David

## Contexto
Para diseñar un módulo administrativo exhibible y profesional, es necesario comprender tanto los fundamentos técnicos de control de acceso como la gestión organizativa en sistemas empresariales.

## Decisión
Se recomienda estudiar los siguientes temas:

1. RBAC (Role-Based Access Control)
    - Concepto de roles como agrupación de permisos.
    - Diferencia entre roles técnicos y roles organizativos.
    - Casos prácticos en sistemas corporativos.

2. ABAC (Attribute-Based Access Control)
    - Uso de atributos y contexto (sector, sucursal, fecha) en decisiones de autorización.
    - Comparación con RBAC y escenarios híbridos.

3. Gestión organizativa en sistemas administrativos
    - Roles como posiciones dentro de la organización (ej. paciente, odontólogo, recepcionista).
    - Cómo se asignan y vigilan responsabilidades.

4. Formalización de reglas de negocio
    - Documentación de políticas como artefactos del dominio.
    - Ejemplos de reglas: límites de aprobación, vigencia de roles, excepciones.

5. Casos ERP/CRM
    - Cómo sistemas empresariales gestionan roles y permisos.
    - Auditoría, personalización y revisiones periódicas de acceso.

## Consecuencias
Positivas
- Mayor comprensión del modelo administrativo.
- Capacidad de justificar decisiones arquitectónicas.
- Preparación para exhibir el sistema como referencia profesional.

Negativas
- Requiere tiempo de estudio y práctica.
- Puede generar debate en comunidades que simplifican el concepto de administración.

## Plan de implementación
1. Estudiar RBAC y ABAC en profundidad, con ejemplos prácticos.
2. Documentar roles organizativos y su relación con permisos en docs/dominio/administracion.md.
3. Analizar casos ERP/CRM y extraer patrones aplicables.
4. Formalizar reglas de negocio como artefactos del dominio (PermissionPolicy).
5. Validar aprendizajes con expertos en administración clínica y sistemas empresariales.

## Ejemplo
```java
// Ejemplo de RBAC aplicado en el dominio
Role dentist = new Role("DENTIST");
PermissionPolicy policy = new PermissionPolicy(dentist, Set.of("READPATIENT", "UPDATETREATMENT"));
```

Relación con otros ADR
- ADR-17 (Arquitectura): Separación de Identity y Administración.
- ADR-15 (Arquitectura): Revocación de permisos en el sistema de autorización.
- ADR-16 (Arquitectura): Permisos de menú en el sistema.  
  

