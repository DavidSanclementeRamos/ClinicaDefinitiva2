

# ADR-48 (Arquitectura): Obtención de `UserId` y `RolId` en casos de uso

- **Fecha**: 2026-02-04
- **Estado**: Aprobado (Revisado)
- **Categoría**: Arquitectura
- **Autor:** David Stiven Sanclemente

---

## Problema

Los casos de uso requieren conocer la identidad del usuario (`UserId`) y su rol activo (`RolId`) para validar autorización.  
El dilema es cómo obtener y propagar estos valores:
- ¿Usar utilidades estáticas (`SecurityUtils`) dentro del caso de uso?
- ¿Pasarlos como parámetros explícitos desde el controller?
- ¿Agruparlos en un wrapper (`RequesterContext`)?
- ¿Codificarlos en el JWT como claims?
- ¿Resolver autorización con anotaciones (`@PreAuthorize`, `hasRole`) en la capa de seguridad?

La decisión impacta directamente en la pureza hexagonal, la testabilidad y la exhibibilidad del proyecto.

---

## Decisión

**Pasar `UserId` y `RolId` como parámetros explícitos** desde el controller a los casos de uso.

**Regla:**
- El controller adapta infraestructura (Spring Security) a objetos de dominio (`UserId`, `RolId`).
- El caso de uso recibe estos parámetros como parte de su contrato y valida autorización con ellos.

---

## Alternativas consideradas y descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| **Usar `SecurityUtils` dentro del caso de uso** | Acopla dominio a infraestructura, requiere mocking estático en tests, rompe hexagonal y dificulta migración de framework. |
| **Agrupar en un wrapper (`RequesterContext`)** | Solo son 2 parámetros simples, no justifican una clase extra. Señaliza sobre-ingeniería, reduce flexibilidad (obliga a pasar rol aunque no se use). |
| **Codificar `UserId` y `RolId` en el JWT como claims** | Inconsistente si roles cambian dinámicamente (clonación, revocación). Requiere revocación de tokens o listas negras, rompe filosofía stateless. |
| **Resolver con `hasRole()` en filter chain o `@PreAuthorize`** | Solo valida rol, no atributos de negocio complejos. Acopla autorización al framework, difícil de testear en aislamiento. |
| **No pasar identidad ni rol explícitamente** | Autorización incompleta, casos de uso sin contexto de seguridad, riesgo de accesos indebidos. |

---

## Implementación

Ejemplo en un controller:

```java
@GetMapping("/{id}")
public ResponseEntity<Dto> find(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails user) {
    
    return useCase.execute(id, user.getUserId(), user.getPrimaryRole().getId())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

---

## Consecuencias

### Ganamos
- Arquitectura hexagonal preservada.
- Casos de uso puros y testables sin infraestructura.
- Framework-agnostic: fácil migrar a Quarkus/Micronaut.
- Firmas honestas: muestran explícitamente qué necesita el caso de uso.

### Perdemos
- Controllers con 2–3 líneas adicionales para extraer contexto de seguridad (aceptable).
- Firmas de casos de uso más largas (pero claras y exhibibles).



