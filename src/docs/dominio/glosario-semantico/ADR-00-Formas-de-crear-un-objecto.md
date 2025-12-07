# ADR-27 (Dominio): Formas de construcción de objetos (Builder vs Setters vs Atributos directos)

- Estado: Exploratorio / Aprendizaje
- Fecha: 2025-11-24
- Autor: David

## Contexto
En el desarrollo del sistema se presentan diferentes necesidades de construcción de objetos:
- Agregados con múltiples atributos y reglas de negocio (ej. Guardian).
- Entidades administrativas con datos simples (ej. Company).
- Objetos infraestructurales o técnicos (ej. Contrac).

La forma de inicializar estos objetos impacta en:
- Claridad y legibilidad del código.
- Seguridad e inmutabilidad.
- Percepción profesional al exhibir la arquitectura.

## Alternativas consideradas
1. Builder
    - Expresivo, seguro, escalable.
    - Refuerza inmutabilidad y claridad semántica.
    - Mayor verbosidad y necesidad de clases auxiliares.

2. Setters
    - Simples y flexibles.
    - Riesgo de estados inconsistentes si no se llaman todos.
    - Apariencia más “CRUD” que “modelo rico”.

3. Atributos directos
    - Máxima simplicidad y rendimiento.
    - Rompe encapsulación y reduce elegancia.
    - Útil solo en objetos internos o infraestructurales.

## Decisión
- Builder se utilizará en agregados ricos con reglas de negocio (ej. Guardian).
- Setters se reservarán para entidades administrativas o DTOs (ej. Company).
- Atributos directos se evitarán en agregados y entidades, pero pueden usarse en objetos técnicos internos donde la simplicidad prime sobre la exhibición.

## Consecuencias
Positivas
- Claridad en la intención arquitectónica.
- Exhibición profesional de buenas prácticas.
- Separación explícita entre objetos ricos y objetos simples.

Negativas
- Mayor verbosidad en Builders.
- Posible percepción de “sobre-ingeniería” en casos simples.
- Necesidad de disciplina para no abusar de setters.

## Plan de implementación
1. Definir convenciones:
    - Builder → agregados con reglas de negocio.
    - Setters → entidades administrativas y DTOs.
    - Atributos directos → objetos técnicos internos.
2. Documentar ejemplos en docs/dominio/construccion-objetos.md.
3. Añadir pruebas de consistencia para Builders (validación de invariantes).
4. Revisar periódicamente el uso de setters para evitar abuso.

## Ejemplo
```java
// Builder en Guardian
Guardian guardian = Guardian.builder()
.withId(GuardianId.generate())
.withPersonInfo(info)
.withAssignedPatients(patients)
.build();

// Setters en Company
Company company = new Company();
company.setName("Clinica OdontoSalud");
company.setNit("123456789");
```

## Nota
Este ADR se clasifica como aprendizaje: no es una decisión definitiva del sistema, sino un ejercicio de reflexión crítica sobre estilos de construcción de objetos y su impacto en la exhibición profesional.

## Relación con otros ADR
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico.
- ADR-26 (Dominio): Identificador único de agregados como String encapsulado en VO.
- ADR-23 (Dominio): Desactivación de usuario debe ser responsabilidad del agregado UserModel.  
  

