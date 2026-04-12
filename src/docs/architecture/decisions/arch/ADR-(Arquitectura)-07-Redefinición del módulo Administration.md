# ADR-07 (Arquitectura): Redefinición del módulo Administration

- **Estado:** Aprobado
- **Fecha**: 2025-10-11
- **Autor**: David Stiven Sanclemente

## Contexto
En la versión inicial del sistema, el módulo Administration agrupaba múltiples responsabilidades: servicios, facturación, pagos, contratos, gastos y roles administrativos.  
Tras las decisiones anteriores:
- Servicios pasó a ser un módulo independiente con modelo híbrido.
- Facturación y Pagos también se separaron en módulos propios.

Esto deja a Administration con un alcance más claro y enfocado en la gestión administrativa general, sin mezclarlo con procesos clínicos o financieros.

## Decisión
El módulo Administration se redefinirá para cubrir únicamente las funciones administrativas transversales, que no pertenecen directamente a servicios clínicos, facturación o pagos.

Entidades principales
- Contrato / Convenio: acuerdos con EPS, aseguradoras, proveedores o aliados estratégicos.
- Gasto: registro de costos operativos y administrativos (insumos, mantenimiento, nómina externa).
- RolUsuario / Staff: gestión de personal administrativo, permisos y jerarquías.
- Reportes administrativos: generación de informes de gestión, indicadores de desempeño, conciliaciones no financieras.

## Consecuencias
Positivas
- Claridad en la separación de dominios: lo clínico (Servicios), lo financiero (Facturación y Pagos) y lo administrativo puro (Administration).
- Modularidad: cada módulo puede evolucionar de forma independiente.
- Escalabilidad: Administration puede crecer hacia un ERP ligero sin afectar la lógica clínica o financiera.

Negativas
- Requiere definir interfaces claras con Facturación y Pagos (ej. conciliación de contratos con EPS).
- Puede percibirse como un módulo “menos central”, aunque sigue siendo clave para trazabilidad.

## Plan de implementación
1. Definir atributos mínimos de Contrato, Gasto y RolUsuario.
2. Establecer relaciones con Facturación (ej. facturas asociadas a convenios EPS).
3. Diseñar reportes administrativos básicos (ej. gastos vs ingresos, convenios activos).
4. Documentar reglas de negocio para trazabilidad y auditoría administrativa.

## Ejemplo
```java
public class Contrato {
private final String id;
private final String entidad;
private final LocalDate fechaInicio;
private final LocalDate fechaFin;
private final String tipoConvenio;

    // Relación con facturación
    private List<Invoice> facturasAsociadas;
}
```

## Relación con otros ADR
- [ADR-(Arquitectura)-05-Creación de un módulo independiente para Servicios.md](ADR-%28Arquitectura%29-05-Creaci%C3%B3n%20de%20un%20m%C3%B3dulo%20independiente%20para%20Servicios.md)
- [ADR-(Arquitectura)-06-Separación de Facturación y Pagos en módulos independientes.md](ADR-%28Arquitectura%29-06-Separaci%C3%B3n%20de%20Facturaci%C3%B3n%20y%20Pagos%20en%20m%C3%B3dulos%20independientes.md)
- [ADR-(Arquitectura)-01-Migración progresiva a arquitectura hexagonal.md](ADR-%28Arquitectura%29-01-Migraci%C3%B3n%20progresiva%20a%20arquitectura%20hexagonal.md)
  