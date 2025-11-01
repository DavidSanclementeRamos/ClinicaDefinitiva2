**Creando ADR para ServiceRendered**

Hoy, 2025-10-26, debo producir un ADR en español para "ServiceRendered". Seguiré la estructura solicitada: usaré encabezados jerárquicos que solo indiquen el tema. No pondré paréntesis en los encabezados, solo reflejaré el tema. Serán seis secciones, con mínimo texto introductorio en cada una: **Estado**, **Contexto**, **Decisión**, **Consecuencias**, y otros si es necesario. Me aseguraré de usar negritas en texto clave y listas donde sean necesarias. Luego, agregaré el divisor horizontal para la presentación.
### ServiceRendered as domain input

---

### Fecha
2025-10-26

---

### Contexto
- El sistema mantiene un catálogo de servicios odontológicos representado por ProvidedService y tarifas por Rate.
- El proceso de facturación recibe información sobre servicios efectivamente prestados y debe crear InvoiceItem respetando invariantes, auditoría y trazabilidad.
- Actualmente se reciben DTOs desde la capa de entrada y hay necesidad de un objeto de dominio que represente la ocurrencia concreta del servicio prestado, separado del catálogo estático.

---

### Decisión
- Introducir ServiceRendered como un Value Object/Entity del dominio que represente la prestación concreta de un servicio y sirva como input para factories y servicios de dominio.
- Definición mínima de ServiceRendered:
    - Campos obligatorios: ServiceId serviceId; String serviceCode; int quantity; LocalDateTime performedAt; DentistId providerId.
    - Campos opcionales pero recomendados: String description; Money snapshotBaseRate (solo si se necesita guardar snapshot para auditoría).
    - Validaciones en constructor: serviceId y serviceCode no nulos; quantity > 0; performedAt no nulo; providerId no nulo.
- Mantener ProvidedService como catálogo independiente que contiene reglas estáticas (baseRate, duration, requiresAuthorization). No mezclar responsabilidades.
- Mapper realiza la conversión desde ServiceRenderedDto (transporte) a ServiceRendered (dominio) validando formatos de IDs y delegando invariantes al constructor del dominio.
- El flujo de facturación usa ServiceRendered para buscar Rate (por serviceCode y contractId), validar vigencia y construir InvoiceItem con snapshot de tarifa y moneda.

---

### Justificación
- Separación de responsabilidades evita acoplar catálogo con eventos de prestación.
- ServiceRendered captura el contexto temporal y actor de la prestación, que es crucial para validar tarifas vigentes y producir auditoría confiable.
- Evita lógica de negocio dispersa en mappers o controllers; las invariantes se concentran en el dominio.
- Facilita testing y trazabilidad: ServiceRendered es pequeño, fácil de crear en tests y registra lo necesario para reproducir la facturación.

---

### Consecuencias
- Cambios requeridos:
    - Añadir clase ServiceRendered con constructor y validaciones.
    - Extender mapper con conversión String -> ServiceId y String -> DentistId (fromString).
    - Mantener RateRepository y lógica de pricing en el Application Service / Domain Factory.
- Beneficios:
    - Menor acoplamiento entre catálogo y facturación.
    - Mejores garantías de invariantes en tiempo de creación de facturas.
    - Datos de auditoría más claros (serviceCode y snapshot de tarifa si se opta por ello).
- Riesgos:
    - Aumento de artefactos a mantener (nuevo VO).
    - Posible duplicación de algunos campos (serviceCode presente en ProvidedService y en ServiceRendered) pero justificada por trazabilidad.

---

### Plan de implementación
1. Añadir ServiceRendered VO con campos y validaciones según la decisión.
2. Añadir ServiceRenderedDto simple con providerId y serviceId como String.
3. Extender mapper para convertir DTO -> VO usando ServiceId.fromString y DentistId.fromString.
4. Actualizar BillingApplicationService y InvoiceFactoryDomainService para consumir ServiceRendered y aplicar Rate lookup y validations.
5. Añadir tests unitarios: mapeo DTO->VO, creación inválida (quantity 0), facturación con rate vencida, facturación con rate vigente.
6. Documentar la decisión en el repositorio de ADRs y referenciarla en los ADRs de Facturación y Rates.

---
