# ADR: Separación de Facturación y Pagos en módulos independientes

- **Fecha:** 2025-10-11
- **Estado:** Aprobado

## Contexto
Inicialmente, el módulo **Administration** agrupaba múltiples responsabilidades: servicios, facturación, pagos, contratos, gastos y roles administrativos.  
Tras la decisión de extraer **Servicios** a un módulo independiente, se evaluó la conveniencia de mantener **Facturación y Pagos** dentro de Administration o separarlos.  
Se identificó que facturación y pagos son dominios con reglas de negocio propias, alta complejidad y necesidad de integraciones externas (ej. Stripe, DIAN, EPS). Mantenerlos acoplados a otros procesos administrativos dificultaría la escalabilidad y la trazabilidad.

## Decisión
Se crearán dos módulos separados:
- **Facturación (Billing)**
    - Entidades: `Invoice`, `InvoiceItem`, `Rate`.
    - Funcionalidad: generación de facturas, aplicación de tarifas, integración con servicios prestados.
    - Relación: recibe insumos del módulo **Servicios** (para generar ítems de factura) y se conecta con **Pagos**.

- **Pagos (Payments)**
    - Entidades: `Payment`, `PaymentMethod`.
    - Funcionalidad: registrar pagos, soportar múltiples métodos (efectivo, tarjeta, transferencia, Stripe API).
    - Relación: liquida facturas generadas en **Facturación**.

El módulo **Administration** quedará enfocado en **contratos, gastos, roles administrativos y convenios**, sin incluir servicios, facturación ni pagos.

## Consecuencias
- **Positivas:**
    - Mayor modularidad y separación de responsabilidades.
    - Escalabilidad: permite añadir nuevas pasarelas de pago o reglas de facturación sin afectar otros módulos.
    - Integración clara con sistemas externos (Stripe, DIAN, EPS).
    - Trazabilidad: cada factura y pago queda documentado y auditable.

- **Negativas:**
    - Incremento en el número de módulos a mantener.
    - Necesidad de definir interfaces claras entre módulos (ej. cómo un `Servicio` se convierte en `InvoiceItem`).

## Próximos pasos
1. Definir atributos mínimos de `Invoice`, `InvoiceItem`, `Rate`, `Payment` y `PaymentMethod`.
2. Diseñar flujos de integración:
    - Servicio prestado → Factura → Pago.
    - Factura → Reporte contable / DIAN.
3. Documentar reglas de negocio para conciliación de pagos y control de cartera.
4. Establecer interfaces de integración con Stripe y otros sistemas externos.  