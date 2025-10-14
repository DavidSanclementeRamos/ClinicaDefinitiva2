# ADR: Estrategia de Integraciones

- **Fecha:** 2025-10-11
- **Estado:** Aprobado

## Contexto
El sistema ha sido dividido en módulos independientes: **Servicios**, **Facturación**, **Pagos** y **Administration**.  
Para operar de forma robusta, se requieren integraciones externas:
- **Stripe**: para la gestión de pagos electrónicos en el módulo de Pagos.
- **DIAN (Colombia)**: para la facturación electrónica y reportes tributarios.
- **EPS / aseguradoras**: para validar y facturar servicios cubiertos por convenios de salud.

Sin una estrategia clara, estas integraciones podrían acoplarse directamente a los módulos internos, generando dependencia y dificultando pruebas, cambios de proveedor o evolución tecnológica.

## Decisión
Se adoptará una política de **integraciones desacopladas mediante APIs y adaptadores**, siguiendo principios de alta cohesión y bajo acoplamiento:
- Ningún módulo interno (Servicios, Facturación, Pagos, Administration) invocará directamente servicios externos.
- Se definirán **interfaces de integración** (ej. `StripeService`, `DIANReport`, `EPSIntegration`) que actuarán como contratos.
- Se implementará un **External Service Gateway** para centralizar la comunicación con APIs externas.
- Se aplicará el patrón **Anti-Corruption Layer (ACL)** para convertir datos entre formatos internos y externos, evitando contaminar el dominio central.
- Las integraciones críticas (ej. DIAN) se implementarán con bajo acoplamiento y capacidad de pruebas unitarias/mocks.

## Consecuencias
- **Positivas:**
    - Independencia de implementaciones externas: cambios en Stripe, DIAN o EPS no afectan el core.
    - Facilidad de pruebas: los adaptadores pueden ser simulados (mocks).
    - Escalabilidad: posibilidad de cambiar de proveedor (ej. Stripe → MercadoPago) modificando solo el adaptador.
    - Se mantiene el principio de alta cohesión y bajo acoplamiento.

- **Negativas:**
    - Mayor necesidad de documentar contratos de integración.
    - Incremento en la lógica de mapeo y conversión dentro de los adaptadores.

## Próximos pasos
1. Definir contratos de API para `StripeService`, `DIANReport` y `EPSIntegration`.
2. Diseñar y desarrollar adaptadores siguiendo principios de **Anti-Corruption Layer**.
3. Implementar pruebas de integración con mocks para simular comportamiento de servicios externos.
4. Documentar flujos de datos y manejo de errores en la capa de integración.  