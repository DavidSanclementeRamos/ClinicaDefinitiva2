# ADR-17 (Arquitectura): Manejo de Plan de Cuentas y Asientos Contables en el Sistema Clínico

- Estado: Aprobado
- Fecha: 2025-11-29
- Autor: David

## Contexto
Estamos diseñando un sistema de gestión clínica con módulo contable.  
En el proceso surgieron dudas sobre:
- Cómo representar el plan de cuentas (PUC colombiano).
- Cómo registrar asientos contables con Debe y Haber.
- Si era necesario mantener LedgerAccount en el dominio.
- Cómo automatizar la creación de asientos a partir de facturación.

## Decisiones tomadas

Representación del Plan de Cuentas
- El PUC colombiano se cargará desde un archivo JSON como semilla inicial en la base de datos.
- La clase LedgerAccount se mantiene en el dominio porque:
    - Representa la estructura del catálogo contable.
    - Encapsula reglas de negocio (ej. requiere tercero, requiere documento, activo/inactivo).
    - Da semántica y validación a los movimientos.

 Modelado de JournalEntry
- JournalEntry representa un asiento contable.
- Se modela como una lista de líneas (JournalEntryLine) que referencian cuentas (LedgerAccount) y montos.
- Esto soporta tanto casos simples (una cuenta Debe y una Haber) como complejos (varias cuentas en Debe y varias en Haber).

 Automatización de Debe/Haber
- Se implementará un método estático de fábrica en JournalEntry (createFromFactura) que:
    - Recibe una factura con su tipo (ej. servicio paciente, compra medicamentos).
    - Aplica reglas contables predefinidas.
    - Genera automáticamente las líneas Debe/Haber.
- El sistema no “adivina” los asientos: usa reglas configuradas basadas en el PUC y la naturaleza de la factura.

 Dudas importantes y respuestas
- ¿Por qué mantener LedgerAccount si ya tengo el JSON?  
  → Porque el JSON es solo semilla de datos; el dominio necesita objetos ricos para aplicar reglas y validar movimientos.

- ¿Debe/Haber se registran manual o automático?  
  → En sistemas modernos, se generan automáticamente a partir de operaciones (facturas, pagos, compras) usando reglas contables. El contador solo valida.

- ¿Cómo se relaciona facturación con asientos?  
  → La factura tiene un atributo tipoFactura. Según ese tipo, el sistema aplica reglas y genera el asiento con Debe/Haber.

- ¿Es suficiente tener atributos debe y haber en JournalEntry?  
  → No. Es mejor modelar una lista de líneas (JournalEntryLine) porque un asiento puede afectar múltiples cuentas.

## Consecuencias
Positivas
- Modelo más fiel a la contabilidad real.
- Escalabilidad: soporta asientos complejos.
- Automatización: menos errores humanos.

Negativas
- Mayor complejidad en el diseño.
- Necesidad de configurar reglas contables iniciales.

## Plan de implementación
1. Definir estructura JSON del PUC colombiano y cargarlo en la BD.
2. Implementar LedgerAccount como catálogo con validaciones.
3. Rediseñar JournalEntry para usar JournalEntryLine.
4. Crear método estático createFromFactura con reglas contables básicas (servicios, compras, nómina).
5. Documentar ejemplos clínicos de asientos (consulta, cirugía, compra de medicamentos, pago de nómina).

## Ejemplo
```java
JournalEntry entry = JournalEntry.createFromFactura(factura);
entry.getLines().forEach(line -> {
    System.out.println(line.getLedgerAccount().getCode() + " " +
                       line.getDebit() + " / " + line.getCredit());
});
```

## Relación con otros ADR
- [ADR-09 (Arquitectura): Nuevos agregados en el módulo Administration.](ADR-09-Nuevos%20agregados%20en%20el%20módulo%20Administration.md)
- [ADR-11 (Arquitectura): Implementación inicial del módulo contable.](ADR-11-Implementación-inicial-de-módulo-contable.md)  
  

