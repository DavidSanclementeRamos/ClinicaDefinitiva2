# ADR-11 (Arquitectura): Implementación inicial del módulo contable

- Estado: Aprobado
- Fecha: 2025-11-16
- Autor: David

## Contexto
El módulo administrativo abarca múltiples áreas (nómina, logística, finanzas, etc.), pero la contabilidad es uno de los núcleos más complejos y críticos.  
Modelar correctamente reglas contables —asientos, conciliaciones, impuestos, costos y reportes financieros— requiere conocimiento profundo del dominio y tiempo de aprendizaje.  
Existe la necesidad de mostrar un avance funcional antes de finalizar el año.

## Decisión
Se implementará un CRUD básico para el módulo contable, limitado inicialmente a operaciones simples:
- Registro de transacciones.
- Categorías de gastos/ingresos.
- Validación mínima de balance entre débitos y créditos.

Este prototipo servirá como punto de partida para evolucionar hacia un sistema contable completo.

## Justificación
- Permite exhibir un avance tangible en corto plazo.
- Facilita validar la arquitectura y separación de responsabilidades.
- Documenta explícitamente que el CRUD es una decisión temporal, no la visión final.
- Refuerza la narrativa pública: “esto es lo que muchos llaman sistema administrativo, pero aquí se usa solo como base para mostrar cómo se transforma en uno real”.

## Consecuencias
- Corto plazo: módulo contable limitado a operaciones CRUD sin reflejar toda la complejidad normativa.
- Mediano plazo: refactorización para incorporar reglas contables reales (asientos dobles, conciliaciones bancarias, impuestos).
- Largo plazo: arquitectura preparada para evolucionar hacia microservicios especializados en contabilidad financiera, costos y analítica.

## Plan de implementación
1. Crear entidades mínimas: Transaccion, CategoriaContable.
2. Implementar validación de balance: suma de débitos = suma de créditos.
3. Exponer CRUD básico vía Application Service.
4. Documentar reglas en docs/arquitectura/contabilidad.md.
5. Añadir pruebas unitarias:
    - Transacción válida (balance correcto).
    - Transacción inválida (desbalance).
6. Preparar esquema para futura integración con MovimientoContable y CuentaContable.

## Ejemplo
```java
public class Transaccion {
private List<MovimientoContable> movimientos;

    public void validarBalance() {
        BigDecimal totalDebitos = movimientos.stream()
            .map(MovimientoContable::getDebito)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCreditos = movimientos.stream()
            .map(MovimientoContable::getCredito)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!totalDebitos.equals(totalCreditos)) {
            throw new BalanceInvalidoException("La transacción no está balanceada");
        }
    }
}
```

## Relación con otros ADR
- [ADR-09 (Arquitectura): Nuevos agregados en el módulo Administration.](ADR-09-Nuevos%20agregados%20en%20el%20módulo%20Administration.md)
- [ADR-07 (Arquitectura): Redefinición del módulo Administration.](ADR-07-Redefinición%20del%20módulo%20Administration.md) 
  

