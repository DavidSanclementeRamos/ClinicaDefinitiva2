Perfecto, David 🙌. Vamos a documentar las **reglas de negocio** de cada módulo clave de tu sistema. Esto te servirá como base para ADRs, validaciones y pruebas de aceptación.

---

# 📜 Reglas de negocio por módulo

## 🦷 **Módulo Servicios**
- Todo servicio debe tener un **código único** (ej. CUPS en Colombia) para trazabilidad.
- Un servicio puede estar **activo o inactivo**, pero nunca se elimina (auditoría histórica).
- Los servicios pueden ser **genéricos** (clase base) o **especializados** (subclases con atributos propios).
- La **duración del servicio** debe estar alineada con la agenda (Scheduled).
- Si `requires_authorization = True`, el sistema debe validar autorización antes de facturar.
- Los servicios deben poder **mapearse a convenios/contratos** para aplicar tarifas diferenciadas.

---

## 💰 **Módulo Facturación (Billing)**
- Una factura debe tener al menos **un `InvoiceItem`**.
- El **subtotal** es la suma de los `InvoiceItem.total_price`.
- El **total** = subtotal + impuestos (si aplica).
- Una factura puede estar en estados: **Draft → Pending → Paid → Cancelled**.
- Una factura no puede marcarse como **Paid** si no tiene pagos confirmados que cubran el total.
- Cada factura debe estar asociada a un **paciente** y a un **proveedor** (profesional o clínica).
- Si la factura corresponde a EPS, debe incluir el **contrato/convenio** aplicable.
- La factura debe ser **inmutable** una vez emitida; solo se permiten notas crédito/débito para ajustes.

---

## 💳 **Módulo Pagos (Payments)**
- Un pago siempre referencia a una **factura existente**.
- Una factura puede tener **múltiples pagos** (parciales o combinados).
- El estado de un pago puede ser: **Pending, Confirmed, Failed, Refunded**.
- El **monto total de pagos confirmados** no puede superar el total de la factura.
- Los pagos deben registrar el **método de pago** (efectivo, tarjeta, transferencia, EPS, Stripe).
- Cada pago debe tener una **referencia de transacción** única (voucher, ID de pasarela, etc.).
- Si el pago es de EPS, debe estar conciliado con el contrato/convenio correspondiente.

---

## 📑 **Módulo Administration (puro)**
- Todo **contrato/convenio** debe tener vigencia (`valid_from`, `valid_to`).
- Los contratos pueden definir **tarifas especiales** que sobrescriben las tarifas base de los servicios.
- Los **gastos** deben registrarse con categoría, monto, fecha y responsable.
- Los **roles de usuario** definen permisos de acceso a módulos (ej. un administrativo no puede modificar servicios clínicos).
- Los contratos con EPS deben estar vinculados a **facturas emitidas** para trazabilidad.
- Ningún contrato puede eliminarse; solo marcarse como inactivo para mantener historial.

---

## 🌐 **Módulo Integraciones**
- Ningún módulo interno debe invocar directamente servicios externos (se hace vía adaptadores).
- Cada integración debe tener un **contrato de API** documentado (`StripeService`, `DIANReport`, `EPSIntegration`).
- Los datos enviados a DIAN deben cumplir con el **formato oficial de facturación electrónica**.
- Los pagos vía Stripe deben registrar el **ID de transacción** para conciliación.
- Las integraciones con EPS deben validar **autorizaciones previas** antes de facturar.
- Si un servicio externo falla, el sistema debe registrar el error y permitir **reintentos controlados**.
- Se debe mantener un **log de auditoría** de todas las interacciones externas.

---

## 🔗 Resumen visual de reglas críticas
| Módulo          | Regla clave |
|-----------------|-------------|
| Servicios       | No se eliminan, solo se inactivan; requieren autorización si aplica |
| Facturación     | Factura inmutable; estados controlados; requiere al menos un ítem |
| Pagos           | No puede exceder el total de la factura; múltiples pagos permitidos |
| Administration  | Contratos con vigencia; roles definen permisos; gastos auditables |
| Integraciones   | Siempre vía adaptadores; logs de auditoría; validación de estándares externos |

---

👉 Con esto ya tienes un **set de reglas de negocio base** para cada módulo.  
¿Quieres que el siguiente paso sea que te ayude a convertir estas reglas en **criterios de aceptación estilo BDD (Given-When-Then)** para que sirvan directamente en pruebas automatizadas?
