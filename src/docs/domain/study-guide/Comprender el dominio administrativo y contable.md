#  Conocimientos necesarios para comprender el dominio administrativo y contable

- Fecha: 2025-11-16
- Autor: David

## Contexto
Durante el rediseño del módulo Administration, surgió la necesidad de incorporar funcionalidades típicas de sistemas administrativos y contables:
- Registro de empresa
- Tipos de documento
- Terceros
- Plan de cuentas contables
- Saldos iniciales
- Movimientos contables
- Reportes como libro mayor, balance, conciliaciones

Para modelar correctamente estos conceptos y sus reglas de negocio, es necesario adquirir conocimientos específicos del dominio administrativo-contable, especialmente en el contexto colombiano y del sector salud.

## Decisión
Se establece como parte del proceso de diseño y modelado del módulo administrativo, la necesidad de estudiar y comprender los siguientes temas:

1. Contabilidad básica (Colombia)
- Principios de partida doble (débito/crédito).
- Naturaleza de cuentas (activos, pasivos, ingresos, gastos, patrimonio).
- Plan Único de Cuentas (PUC).
- Asientos contables y conciliaciones.
- Saldos iniciales y cierre contable.

2. Contabilidad para clínicas odontológicas
- Facturación a EPS y conciliación de convenios.
- Registro de copagos, deducibles y anticipos.
- Control de gastos operativos y administrativos.
- Manejo de proveedores, insumos y nómina externa.

3. Normativa colombiana relevante
- Resoluciones DIAN sobre facturación electrónica.
- NIIF para pymes (aplicable a clínicas).
- Manuales del Ministerio de Salud sobre gestión administrativa.

4. Modelado de sistemas contables
- Agregados: MovimientoContable, CuentaContable, Tercero.
- Eventos de dominio: MovimientoRegistrado, SaldoInicialCargado.
- Proyecciones: LibroMayor, Balance, FlujoDeCaja.

5. Arquitectura de integración
- Separación entre dominio administrativo y contable.
- Eventos para sincronización con módulos de Facturación y Pagos.
- CQRS para reportes contables.

6. Seguridad y auditoría
- Trazabilidad de operaciones contables.
- Roles y permisos administrativos.
- Retención de documentos y cumplimiento normativo.

## Consecuencias
- Se habilita un modelado más preciso y alineado con la realidad operativa de una clínica.
- Se requiere tiempo de estudio y validación con expertos contables.
- Se podrán generar reportes contables confiables y cumplir con normativas fiscales.

## Plan de implementación
1. Estudiar el PUC colombiano y su estructura jerárquica.
2. Modelar los agregados Empresa, Tercero, CuentaContable, MovimientoContable, SaldoInicial.
3. Integrar Expense y Contrac con estos nuevos conceptos.
4. Diseñar reportes contables básicos: libro mayor, balance, gastos por cuenta.
5. Validar el modelo con un contador o experto en administración de clínicas.
6. Documentar aprendizajes en docs/dominio/contabilidad.md.

## Ejemplo
```java
// Ejemplo de partida doble aplicada en el dominio
MovimientoContable movimiento = new MovimientoContable(
    cuentaDebe, BigDecimal.valueOf(50000),
    cuentaHaber, BigDecimal.valueOf(50000),
    LocalDate.now()
);
movimiento.validarBalance(); // asegura que Debe == Haber
```

## Relación con otros ADR
- ADR-10 (Arquitectura): Nuevos agregados en el módulo Administration.
- ADR-12 (Arquitectura): Implementación inicial del módulo contable.
- ADR-13 (Arquitectura): Manejo de Plan de Cuentas y Asientos Contables en el Sistema Clínico.  
  

