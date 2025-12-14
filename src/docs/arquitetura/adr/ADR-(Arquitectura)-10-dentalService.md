# ADR-10 (Arquitectura): Estrategia de modelado y persistencia para ProvidedService con campos específicos consultables

- Estado: Aprobado
- Fecha: 2025-11-13
- Autor: David

## Contexto
El módulo dental debe representar múltiples tipos de servicios (consulta general, ortodoncia, cirugía, prótesis, etc.).  
Existen requerimientos funcionales que exigen consultas/filtrados por campos específicos de ciertos tipos (ej. treatmentdurationmonths en ortodoncia) con rendimiento aceptable.  
El dominio debe ser expresivo y extensible, y la persistencia debe permitir consultas indexadas sobre esos campos.

## Decisión
Se adopta un único agregado raíz ProvidedService con atributos comunes:
- id, name, code, catalog, baseRate, duration, requiresAuthorization, description, status, createdAt/updatedAt.

Los aspectos específicos se modelan mediante composición:
- Objeto de detalles por tipo (ServiceDetails) persistido en tablas separadas en relación one-to-one con la fila principal.
- Ejemplo: tabla providedservice (campos comunes) + tabla providedserviceorthodontic (campos ortodoncia como treatmentdurationmonths, appliancetype, requires_followup).

Se usa JPA/Hibernate y Spring Data JPA.  
Se mantiene un único repositorio ProvidedServiceRepository con consultas (JOINs) hacia las tablas de detalles para filtrados eficientes.

## Razonamiento
- Una sola entidad raíz preserva identidad única e invariantes comunes.
- Composición con tablas dedicadas permite indexar y consultar eficientemente campos tipo-específicos.
- Evita duplicación de repositorios/identidades.
- Extensibilidad controlada: añadir un nuevo tipo implica crear tabla de detalles y mapping.

## Alternativas consideradas
1. Columna JSON: flexible, pero difícil de indexar y consultar.
2. Single table con columnas opcionales: consultas directas, pero tabla ancha y difícil de mantener.
3. Repositorios separados por subtipo: separación clara, pero problemas de identidad y consistencia.

## Consecuencias
Ventajas
- Consultas eficientes por campos específicos.
- Modelado claro: datos comunes en ProvidedService, datos específicos en tablas/VOs.
- Extensibilidad controlada.

Inconvenientes
- Mayor complejidad en persistencia (más tablas, mapping one-to-one).
- Necesidad de queries/joins para búsquedas específicas.
- Migraciones por cada nuevo tipo.

## Plan de implementación
1. Crear tabla providedservice y tablas de detalles (providedservice_orthodontic, etc.).
2. Definir entidad JPA ProvidedService y entidades de detalles (OrthodonticDetailsEntity) con @OneToOne y @MapsId.
3. Implementar repositorio ProvidedServiceRepository con consultas JOIN.
4. Crear fábrica de creación que construya entidad principal y detalles según serviceType.
5. Definir DTO base y DTOs específicos con Bean Validation.
6. Implementar mappers para convertir entre DTOs y dominio.
7. Documentar checklist para añadir nuevos tipos (DDL, entidad, mapper, DTO, pruebas).

## Ejemplo
```java
@Entity
public class ProvidedService {
@Id
private Long id;
private String name;
private String code;
private BigDecimal baseRate;
private Duration duration;
private boolean requiresAuthorization;
private String description;
private String status;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL)
    private OrthodonticDetailsEntity orthodonticDetails;
}

@Entity
public class OrthodonticDetailsEntity {
@Id
private Long id;
private int treatmentDurationMonths;
private String applianceType;
private boolean requiresFollowup;

    @OneToOne
    @MapsId
    private ProvidedService providedService;
}
```

## Relación con otros ADR
- [ADR-05 (Arquitectura): Creación de un módulo independiente para Servicios.](ADR-05-Creación%20de%20un%20módulo%20independiente%20para%20Servicios.md)
- [ADR-06 (Arquitectura): Separación de Facturación y Pagos en módulos independientes.](ADR-06-Separación%20de%20Facturación%20y%20Pagos%20en%20módulos%20independientes.md)
- [ADR-08 (Arquitectura): Estrategia de Integraciones.](ADR-08-Estrategia%20de%20Integraciones.md) 
  

