# ADR-06 (Arquitectura): Separación de Facturación y Pagos en módulos independientes

- Estado: Aprobado
- Fecha: 2025-10-11
- Autor: David

## Contexto
Inicialmente, el módulo Administration agrupaba múltiples responsabilidades: servicios, facturación, pagos, contratos, gastos y roles administrativos.  
Tras la decisión de extraer Servicios a un módulo independiente, se evaluó la conveniencia de mantener Facturación y Pagos dentro de Administration o separarlos.  
Se identificó que facturación y pagos son dominios con reglas de negocio propias, alta complejidad y necesidad de integraciones externas (ej. Stripe, DIAN, EPS).  
Mantenerlos acoplados a otros procesos administrativos dificultaría la escalabilidad y la trazabilidad.

## Decisión
Se crearán dos módulos separados:

Facturación (Billing)
- Entidades: Invoice, InvoiceItem, Rate.
- Funcionalidad: generación de facturas, aplicación de tarifas, integración con servicios prestados.
- Relación: recibe insumos del módulo Servicios (para generar ítems de factura) y se conecta con Pagos.

Pagos (Payments)
- Entidades: Payment, PaymentMethod.
- Funcionalidad: registrar pagos, soportar múltiples métodos (efectivo, tarjeta, transferencia, Stripe API).
- Relación: liquida facturas generadas en Facturación.

El módulo Administration quedará enfocado en contratos, gastos, roles administrativos y convenios, sin incluir servicios, facturación ni pagos.

## Consecuencias
Positivas
- Mayor modularidad y separación de responsabilidades.
- Escalabilidad: nuevas pasarelas de pago o reglas de facturación sin afectar otros módulos.
- Integración clara con sistemas externos (Stripe, DIAN, EPS).
- Trazabilidad: cada factura y pago queda documentado y auditable.

Negativas
- Incremento en el número de módulos a mantener.
- Necesidad de definir interfaces claras entre módulos (ej. cómo un Servicio se convierte en InvoiceItem).

## Plan de implementación
1. Crear módulo Billing en com.clinica.domain.billing.
2. Crear módulo Payments en com.clinica.domain.payments.
3. Definir atributos mínimos de Invoice, InvoiceItem, Rate, Payment y PaymentMethod.
4. Diseñar flujos de integración:
  - Servicio prestado → Factura → Pago.
  - Factura → Reporte contable / DIAN.
5. Documentar reglas de negocio para conciliación de pagos y control de cartera.
6. Establecer interfaces de integración con Stripe y otros sistemas externos.

## Ejemplo
```java
// Conversión de Servicio a InvoiceItem
Servicio servicio = new Servicio("CONSULTA", "Consulta general", BigDecimal.valueOf(50000));
InvoiceItem item = new InvoiceItem(servicio.getCodigo(), servicio.getNombre(), servicio.getTarifaBase(), 1);
Invoice invoice = new Invoice(patientId, List.of(item));
```

## Relación con otros ADR
- [ ADR-01 (Arquitectura): Migración a arquitectura hexagonal](ADR-01-Migración%20progresiva%20a%20arquitectura%20hexagonal.md)
- [ADR-02 (Arquitectura): Catálogo de errores clínicos por operación](ADR-02-Catálogo%20de%20errores%20clínicos%20por%20operación.md)  
  

