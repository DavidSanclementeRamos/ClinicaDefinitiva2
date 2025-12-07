# ADR-05 (Arquitectura): Creación de un módulo independiente para Servicios

- Estado: Aprobado
- Fecha: 2025-10-11
- Autor: David

## Contexto
El sistema cuenta actualmente con un módulo Administration que agrupa operaciones administrativas: facturación, pagos, contratos, gastos y servicios odontológicos.  
Se identificó que los Servicios representan un dominio distinto, con necesidades propias de trazabilidad clínica, categorización y relación directa con agenda y facturación.  
Mantenerlos dentro de Administration genera acoplamiento excesivo y dificulta la escalabilidad.

Se evaluaron tres enfoques:
1. Mantener todos los servicios como una sola entidad genérica dentro de Administration.
2. Dividir cada especialidad en entidades separadas.
3. Crear un módulo independiente para Servicios con un modelo híbrido.

## Decisión
Se creará un módulo independiente para Servicios, separado de Administration, con un modelo híbrido:
- Clase base Servicio con atributos comunes:
  - id, nombre, categoría, código estandarizado, tarifa base, duración, requiereAutorización.
- Subentidades para categorías críticas con atributos específicos (ej. Ortodoncia, Cirugía Oral, Odontopediatría).
- Integración con:
  - Facturación → generación de InvoiceItem.
  - Agenda (Scheduled) → definición de duración y recursos.
  - Pagos → a través de facturas liquidadas.

## Servicios contemplados inicialmente
- Prevención y diagnóstico: consultas, limpiezas, flúor, sellantes, radiografías.
- Odontología general y restauradora: obturaciones, reconstrucciones, endodoncia, extracciones simples.
- Periodoncia: tratamientos de encías, raspados, cirugía periodontal.
- Ortodoncia: brackets, alineadores, controles.
- Implantología y prótesis: implantes, coronas, prótesis fijas/removibles.
- Cirugía oral y maxilofacial: extracciones quirúrgicas, frenillo, injertos.
- Odontopediatría: atención infantil, sellantes, restauraciones pediátricas.
- Estética dental: blanqueamiento, carillas, contorneado.

## Consecuencias
Positivas
- Mayor modularidad y trazabilidad.
- Escalabilidad: nuevas categorías sin romper el modelo.
- Integración clara con facturación y agenda.
- Posibilidad de mapear servicios a estándares (ej. CUPS en Colombia).

Negativas
- Incremento en la complejidad inicial del diseño.
- Necesidad de definir reglas claras para decidir cuándo un servicio se mantiene genérico y cuándo se convierte en subentidad.

## Plan de implementación
1. Crear módulo Servicios en com.clinica.domain.servicios.
2. Definir clase base Servicio con atributos mínimos.
3. Identificar categorías críticas que requieren subentidades (OrtodonciaServicio, CirugiaServicio).
4. Diseñar relaciones con Tarifa, InvoiceItem y Scheduled.
5. Documentar reglas de negocio en docs/dominio/servicios.md.

## Ejemplo
```java
public class Servicio {
private final String id;
private final String nombre;
private final String categoria;
private final String codigoEstandarizado;
private final BigDecimal tarifaBase;
private final Duration duracion;
private final boolean requiereAutorizacion;
}

public class OrtodonciaServicio extends Servicio {
private final int numeroControles;
// atributos específicos de ortodoncia
}
```

## Relación con otros ADR
- [ADR-01 (Arquitectura): Migración a arquitectura hexagona](ADR-01-Migración%20progresiva%20a%20arquitectura%20hexagonal.md)
- [ADR-02 (Arquitectura): Catálogo de errores clínicos por operación](ADR-02-Catálogo%20de%20errores%20clínicos%20por%20operación.md)
- [ADR-03 (Arquitectura): Jerarquía global de excepciones y excepciones para Value Objects de Persona](ADR-03-Jerarquía%20global%20de%20excepciones%20y%20excepciones%20para%20Value%20Objects%20de%20Persona.md) 
  

