# ADR 1 (Servicio): ServiceRendered como input de dominio

Estado:
Aprobado

Fecha:
2025-10-26

## Contexto
- El sistema mantiene un catálogo de servicios odontológicos representado por ProvidedService y tarifas por Rate.
- El proceso de facturación recibe información sobre servicios efectivamente prestados y debe crear InvoiceItem respetando invariantes, auditoría y trazabilidad.
- Actualmente se reciben DTOs desde la capa de entrada, pero se requiere un objeto de dominio que represente la ocurrencia concreta del servicio prestado, separado del catálogo estático.

## Decisión
- Introducir ServiceRendered como Value Object/Entity del dominio que represente la prestación concreta de un servicio y sirva como input para factories y servicios de dominio.
- Definición mínima de ServiceRendered:
  - Campos obligatorios: ServiceId serviceId, String serviceCode, int quantity, LocalDateTime performedAt, DentistId providerId.
  - Campos opcionales: String description, Money snapshotBaseRate (para auditoría).
  - Validaciones en constructor: serviceId y serviceCode no nulos; quantity > 0; performedAt no nulo; providerId no nulo.
- Mantener ProvidedService como catálogo independiente con reglas estáticas (baseRate, duration, requiresAuthorization).
- Mapper convierte ServiceRenderedDto (transporte) a ServiceRendered (dominio), validando formatos de IDs y delegando invariantes al constructor.
- El flujo de facturación usa ServiceRendered para buscar Rate (por serviceCode y contractId), validar vigencia y construir InvoiceItem con snapshot de tarifa y moneda.

## Justificación
- Separación de responsabilidades: evita acoplar catálogo con eventos de prestación.
- Captura de contexto temporal y actor: crucial para validar tarifas vigentes y auditoría confiable.
- Concentración de invariantes en el dominio: evita lógica dispersa en mappers o controllers.
- Facilidad de testing y trazabilidad: objeto pequeño, fácil de instanciar en pruebas y suficiente para reproducir facturación.

## Consecuencias
- Cambios requeridos:
  - Añadir clase ServiceRendered con constructor y validaciones.
  - Extender mapper con conversión String -> ServiceId y String -> DentistId.
  - Mantener RateRepository y lógica de pricing en Application Service / Domain Factory.
- Beneficios:
  - Menor acoplamiento entre catálogo y facturación.
  - Garantías de invariantes en creación de facturas.
  - Auditoría más clara (serviceCode y snapshot de tarifa).
- Riesgos:
  - Mayor número de artefactos a mantener.
  - Posible duplicación de campos (serviceCode en ProvidedService y ServiceRendered), justificada por trazabilidad.

## Plan de implementación
1. Añadir ServiceRendered VO con campos y validaciones.
2. Crear ServiceRenderedDto con providerId y serviceId como String.
3. Extender mapper para convertir DTO → VO usando ServiceId.fromString y DentistId.fromString.
4. Actualizar BillingApplicationService y InvoiceFactoryDomainService para consumir ServiceRendered y aplicar búsqueda de Rate y validaciones.
5. Añadir tests unitarios:
  - Mapeo DTO → VO.
  - Creación inválida (quantity = 0).
  - Facturación con rate vencida.
  - Facturación con rate vigente.
6. Documentar la decisión en el repositorio de ADRs y referenciarla en ADRs de Facturación y Rates.

