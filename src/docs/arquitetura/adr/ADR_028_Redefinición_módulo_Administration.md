# ADR: Redefinición del módulo Administration

- **Fecha:** 2025-10-11
- **Estado:** Aprobado

## Contexto
En la versión inicial del sistema, el módulo **Administration** agrupaba múltiples responsabilidades: servicios, facturación, pagos, contratos, gastos y roles administrativos.  
Tras las decisiones anteriores:
- **Servicios** pasó a ser un módulo independiente con modelo híbrido.
- **Facturación** y **Pagos** también se separaron en módulos propios.

Esto deja a **Administration** con un alcance más claro y enfocado en la **gestión administrativa general**, sin mezclarlo con procesos clínicos o financieros.

## Decisión
El módulo **Administration** se redefinirá para cubrir únicamente las funciones administrativas transversales, que no pertenecen directamente a servicios clínicos, facturación o pagos.  
Las entidades principales serán:
- **Contrato / Convenio**: acuerdos con EPS, aseguradoras, proveedores o aliados estratégicos.
- **Gasto**: registro de costos operativos y administrativos (insumos, mantenimiento, nómina externa).
- **Rol del Usuario / Staff**: gestión de personal administrativo, permisos y jerarquías.
- **Reportes administrativos**: generación de informes de gestión, indicadores de desempeño, conciliaciones no financieras.

## Consecuencias
- **Positivas:**
    - Claridad en la separación de dominios: lo clínico (Servicios), lo financiero (Facturación y Pagos) y lo administrativo puro (Administration).
    - Modularidad: cada módulo puede evolucionar de forma independiente.
    - Escalabilidad: Administration puede crecer hacia un ERP ligero (gestión de contratos, proveedores, gastos) sin afectar la lógica clínica o financiera.

- **Negativas:**
    - Requiere definir interfaces claras con Facturación y Pagos (ej. conciliación de contratos con EPS).
    - Puede percibirse como un módulo “menos central” frente a los demás, aunque sigue siendo clave para trazabilidad.

## Próximos pasos
1. Definir atributos mínimos de `Contrato`, `Gasto` y `RolUsuario`.
2. Establecer relaciones con Facturación (ej. facturas asociadas a convenios EPS).
3. Diseñar reportes administrativos básicos (ej. gastos vs ingresos, convenios activos).
4. Documentar reglas de negocio para trazabilidad y auditoría administrativa.  