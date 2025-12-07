# ADR-09 (Arquitectura): Nuevos agregados en el módulo Administration

- Estado: Aprobado
- Fecha: 2025-11-16
- Autor: David

## Contexto
Durante la redefinición del módulo Administration, se identificó la necesidad de ampliar el alcance hacia funcionalidades administrativas y contables típicas de sistemas de gestión empresarial.  
Los agregados iniciales (Contrac, Expense, UserRole) cubrían convenios, gastos y roles administrativos, pero faltaban piezas clave para soportar trazabilidad contable, multi-empresa y gestión de terceros.

## Decisión
Se incorporan los siguientes agregados:

1. Empresa
   - Representa la entidad jurídica que opera el sistema (clínica, IPS, consultorio).
   - Punto de referencia para todos los demás agregados.
   - Permite soportar multi-empresa en el futuro.

2. Tercero
   - Generaliza la noción de proveedor.
   - Incluye EPS, aseguradoras, pacientes, empleados y cualquier entidad con relación económica.
   - Reemplaza Supplier como tipo específico de tercero.

3. CuentaContable
   - Catálogo contable basado en el Plan Único de Cuentas (PUC) colombiano.
   - Clasifica activos, pasivos, ingresos, gastos y patrimonio.
   - Base para reportes contables (mayor, balance).

4. SaldoInicial
   - Registra el estado contable inicial por cuenta y tercero.
   - Necesario para cuadrar balances y generar reportes correctos desde el inicio.
   - Facilita migración desde sistemas anteriores.

5. MovimientoContable
   - Representa un asiento contable (débito/crédito).
   - Se genera a partir de gastos, facturas, pagos u operaciones administrativas.
   - Permite construir libro mayor y balance de comprobación.

## Justificación
- Coherencia contable: operaciones alineadas con principios de partida doble y normativa colombiana.
- Flexibilidad: múltiples tipos de terceros y documentos.
- Escalabilidad: evolución hacia un ERP ligero sin perder modularidad.
- Trazabilidad: auditoría completa de operaciones administrativas y contables.
- Integración: relación natural con Contrac y Expense.

## Ventajas
- Claridad en separación de responsabilidades.
- Cumplimiento normativo (PUC, facturación electrónica).
- Reportes confiables: libro mayor, balance, conciliaciones.
- Preparación para multi-empresa y multi-tercero.
- Extensibilidad hacia módulos financieros y clínicos.

## Consideraciones
- Requiere conocimiento contable para definir reglas de negocio.
- Necesita integración con Facturación y Pagos para generar movimientos automáticamente.
- Mayor complejidad en persistencia y consultas para reportes contables.

## Consecuencias
- Modelo administrativo más robusto y completo.
- Reportes contables y administrativos confiables.
- Necesidad de capacitación en contabilidad básica y normativa colombiana para el equipo de desarrollo.

## Plan de implementación
1. Implementar agregados Empresa, Tercero, CuentaContable, SaldoInicial, MovimientoContable.
2. Integrar Contrac y Expense con estos nuevos agregados.
3. Importar el PUC colombiano como catálogo inicial de cuentas contables.
4. Diseñar reportes básicos: libro mayor, balance, gastos por cuenta y tercero.
5. Validar el modelo con un contador o experto en administración de clínicas.

## Ejemplo
```java
public class MovimientoContable {
private final String id;
private final CuentaContable cuenta;
private final Tercero tercero;
private final BigDecimal debito;
private final BigDecimal credito;
private final LocalDate fecha;

    public MovimientoContable(CuentaContable cuenta, Tercero tercero, BigDecimal debito, BigDecimal credito) {
        this.id = UUID.randomUUID().toString();
        this.cuenta = cuenta;
        this.tercero = tercero;
        this.debito = debito;
        this.credito = credito;
        this.fecha = LocalDate.now();
    }
}
```

## Relación con otros ADR
- [ADR-07 (Arquitectura): Redefinición del módulo Administration.](ADR-07-Redefinición%20del%20módulo%20Administration.md)
- [ADR-06 (Arquitectura): Separación de Facturación y Pagos en módulos independientes.](ADR-06-Separación%20de%20Facturación%20y%20Pagos%20en%20módulos%20independientes.md)
  

