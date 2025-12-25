# ADR-22 (Arquitectura): Estrategia de Numeración de Catálogos de Error

**Estado:** ✅ Aceptado  
**Fecha:** Diciembre 24, 2024  
**Contexto:** Gestión de ciclo de vida de catálogos de error en el dominio  
**Autor:** David Stiven Sanclemente  
**Afecta a:** Todos los módulos del dominio

---

## Contexto y Problema

Durante la refactorización del Módulo Actor (ADR-036), surgió la necesidad de **eliminar catálogos de error** que eran:
- Redundantes (validaciones duplicadas)
- Mal ubicados (responsabilidad de otro componente)
- Obsoletos (reemplazados por mejor diseño)

Esto planteó una pregunta crítica:

> **¿Qué hacer con la numeración de los catálogos eliminados?**

### Opciones consideradas:

#### **Opción A: Renumerar todo secuencialmente** ❌
```java
// Antes
ERR_DENTIST_001 → Aplicada
ERR_DENTIST_002 → Aplicada
ERR_DENTIST_003 → ELIMINADA
ERR_DENTIST_004 → Aplicada
ERR_DENTIST_005 → ELIMINADA
ERR_DENTIST_006 → Aplicada

// Después (renumerado)
ERR_DENTIST_001 → Aplicada
ERR_DENTIST_002 → Aplicada
ERR_DENTIST_003 → Aplicada (era 004)
ERR_DENTIST_004 → Aplicada (era 006)
```

**Problemas:**
- ❌ Rompe trazabilidad histórica en Git
- ❌ Invalida referencias en issues/tickets
- ❌ Confunde logs históricos
- ❌ Documentación externa queda desincronizada
- ❌ No es práctica estándar en la industria

#### **Opción B: Preservar numeración, documentar eliminados** ✅
```java
// Antes y Después (sin cambios)
ERR_DENTIST_001 → Aplicada
ERR_DENTIST_002 → Aplicada
// ERR_DENTIST_003: ELIMINADA (ver ADR-038)
ERR_DENTIST_004 → Aplicada
// ERR_DENTIST_005: ELIMINADA (ver ADR-038)
ERR_DENTIST_006 → Aplicada
ERR_DENTIST_007 → Nueva (continúa secuencia)
```

**Ventajas:**
- ✅ Preserva trazabilidad completa
- ✅ Referencias históricas siguen válidas
- ✅ Práctica estándar en APIs públicas
- ✅ Facilita auditorías y debugging
- ✅ Documenta evolución del sistema

---

## Decisión

**Se adopta la Opción B: Preservar numeración histórica.**

### Principios de Numeración

1. **Inmutabilidad de Códigos**
    - Una vez asignado, un código **NUNCA** se reutiliza
    - Los códigos eliminados se marcan como obsoletos
    - La numeración actúa como "timestamp lógico" de evolución

2. **Continuidad Secuencial**
    - Nuevos catálogos continúan la secuencia donde quedó
    - Ejemplo: si RN-DENTIST-010 existe, el siguiente es RN-DENTIST-011
    - NO se "rellenan huecos" dejados por eliminados

3. **Documentación In-Code**
    - Catálogos eliminados se documentan con comentarios en el enum
    - Comentario incluye: motivo, fecha, reemplazo

4. **Registro Histórico**
    - Archivo `ADR-038` mantiene registro completo de eliminados
    - Cada entrada incluye justificación técnica

---

## Implementación

### Estructura de Enum con Catálogos Eliminados

```java
public enum DentistError implements ErrorCatalog {
    
    ERR_DENTIST_AGE_INSUFFICIENT(
        "RN-DENTIST-001",
        "error.dentist.age",
        "El odontólogo debe tener al menos 25 años"
    ),
    ERR_DENTIST_MISSING_AVAILABILITY(
        "RN-DENTIST-002",
        "error.dentist.availability.missing",
        "El odontólogo debe registrar disponibilidad inicial"
    ),
    
    // RN-DENTIST-003: ELIMINADA (2024-12-24)
    // Motivo: Delegada a UserAccessError.ERR_USER_INACTIVE
    // Original: "Solo puede editarse si está activo"
    // Ver: ADR-038 para detalles completos
    
    ERR_DENTIST_ACTIVE_APPOINTMENTS(
        "RN-DENTIST-004",
        "error.dentist.deactivate.appointments",
        "No puede desactivarse si tiene citas activas"
    ),
    
    // RN-DENTIST-005: ELIMINADA (2024-12-24)
    // Motivo: Dividida en catálogos específicos de ValueObject
    // Original: "Debe tener nombre y documento válidos"
    // Reemplazo: ValueObjectError.ERR_FULLNAME_BLANK, ERR_DOCUMENT_INVALID_FORMAT
    
    ERR_DENTIST_TIME_CONFLICT(
        "RN-DENTIST-006",
        "error.dentist.schedule.conflict",
        "El odontólogo ya tiene una cita en este horario"
    ),
    
    // Nuevos catálogos continúan secuencia
    ERR_DENTIST_OUT_OF_WORKING_HOURS(
        "RN-DENTIST-007",
        "error.dentist.working.hours",
        "El horario está fuera de las horas laborales"
    );
    
    // ... resto del código ...
}
```

### Template de Comentario para Eliminados

```java
// RN-<AGREGADO>-<NNN>: ELIMINADA (YYYY-MM-DD)
// Motivo: <RAZÓN_BREVE>
// Original: "<MENSAJE_ORIGINAL>"
// Reemplazo: <NUEVO_CATÁLOGO> (si aplica)
// Ver: ADR-038 para detalles completos
```

---

## Casos de Uso

### 1. Developer busca por código en logs

**Escenario:**
```
[ERROR] BusinessRuleViolationException: RN-DENTIST-003
```

**Búsqueda en código:**
```java
// Encuentra comentario:
// RN-DENTIST-003: ELIMINADA (2024-12-24)
// Reemplazo: UserAccessError.ERR_USER_INACTIVE
```

**Resultado:** ✅ Developer sabe que el error cambió y dónde buscar el nuevo

### 2. Auditoría de cumplimiento

**Pregunta:** ¿Cuándo se introdujo la validación de edad mínima para odontólogos?

**Respuesta:**
- Código `RN-DENTIST-001` creado en commit inicial (Dic 2024)
- Nunca ha sido eliminado ni modificado
- Trazabilidad completa en Git blame

### 3. Migración de sistema legacy

**Necesidad:** Sistema externo referencia `RN-DENTIST-003`

**Solución:**
1. Buscar en comentarios del código
2. Ver que fue reemplazado por `ERR_USER_INACTIVE`
3. Mapear en capa de integración:
```java
// Migration mapping
if (errorCode.equals("RN-DENTIST-003")) {
    return UserAccessError.ERR_USER_INACTIVE;
}
```

---

## Beneficios

### 1. **Trazabilidad Completa**
```
Git History:
2024-12-01: feat: Add ERR_DENTIST_003 (initial)
2024-12-15: refactor: Deprecate ERR_DENTIST_003, use ERR_USER_INACTIVE
2024-12-24: docs: Document ERR_DENTIST_003 removal in ADR-038

→ Toda la evolución es rastreable
```

### 2. **Debugging Facilitado**
```
Log antiguo (Prod):   [ERROR] RN-DENTIST-003
Código actual:        // RN-DENTIST-003: ELIMINADA → ERR_USER_INACTIVE
Acción:               Buscar ERR_USER_INACTIVE en logs recientes
```

### 3. **Documentación Viva**
El código fuente actúa como documentación histórica:
- Qué existió
- Cuándo se eliminó
- Por qué se eliminó
- Qué lo reemplazó

---

## Comparación con Estándares de la Industria

### HTTP Status Codes
- **418 I'm a teapot** - Definido en 1998, NUNCA reutilizado
- **402 Payment Required** - Reservado desde 1999, aún no implementado
- Códigos obsoletos se marcan como "deprecated", no se eliminan

### Error Codes en APIs Públicas
- **AWS:** Códigos de error nunca cambian, se deprecan
- **Google Cloud:** Catálogos de error son inmutables
- **Stripe:** Error codes son parte del contrato de API

### Práctica en DDD
- **Eric Evans (DDD):** "El lenguaje ubicuo debe ser estable"
- **Vaughn Vernon:** "Los identificadores en el dominio son inmutables"

---

## Excepciones a la Regla

### ¿Cuándo SÍ renumerar?

En proyectos **productivos comerciales**, podría considerarse renumerar únicamente si:
1. El catálogo NUNCA fue deployado a producción
2. El código está en rama de desarrollo local
3. El equipo completo está de acuerdo

**Sin embargo, en ESTE proyecto NO aplicamos esta excepción.**

### Contexto Especial: Proyecto de Exhibición

Este es un **proyecto de exhibición profesional** cuyo objetivo es demostrar:
- Evolución de conocimientos en DDD
- Madurez arquitectónica adquirida
- Proceso de aprendizaje y refinamiento técnico

Por lo tanto, **aunque técnicamente podríamos renumerar** (cumplimos las 3 condiciones: no hay producción, código en desarrollo local, equipo de 1 persona), **DECIDIMOS NO HACERLO** porque:

✅ **Valor pedagógico:** La numeración con "huecos" documenta visualmente la evolución del pensamiento arquitectónico.

✅ **Evidencia de aprendizaje:** Los comentarios de códigos eliminados muestran errores cometidos y lecciones aprendidas.

✅ **Trazabilidad didáctica:** Cualquier revisor puede ver el "antes y después" y entender el journey de madurez técnica.

✅ **Honestidad profesional:** Muestra capacidad de autocrítica, refactorización y mejora continua.

**Analogía:**
> Es como mostrar los bocetos iniciales de un artista junto a la obra final. Los "errores" son parte valiosa del portafolio porque demuestran crecimiento.

### Ejemplo Comparativo

**Si aplicáramos la excepción (renumerando):**
```java
// ❌ Perdería valor didáctico
ERR_DENTIST_001 → Aplicada
ERR_DENTIST_002 → Aplicada
ERR_DENTIST_003 → Aplicada (era 004)
ERR_DENTIST_004 → Aplicada (era 006)
// No hay evidencia de que existieron RN-003 y RN-005
```

**Al NO aplicarla (decisión tomada):**
```java
// ✅ Mantiene valor pedagógico
ERR_DENTIST_001 → Aplicada
ERR_DENTIST_002 → Aplicada
// RN-DENTIST-003: ELIMINADA - "Intenté personalizar por operación, error arquitectónico"
ERR_DENTIST_004 → Aplicada
// RN-DENTIST-005: ELIMINADA - "No entendía separación VO vs Agregado"
ERR_DENTIST_006 → Aplicada
// La historia completa está visible
```

### Decisión Final

**En este proyecto de exhibición:**
- ❌ NO renumeramos NUNCA, independientemente de las condiciones técnicas
- ✅ Los "huecos" son **intencionales y valiosos** - documentan el journey de aprendizaje
- ✅ Los catálogos eliminados son **evidencia de madurez**, no de "errores a ocultar"

**Si este fuera un proyecto productivo comercial:**
- ✅ SÍ podríamos considerar renumerar antes de primer deploy
- ✅ SÍ sería válido "limpiar" el código antes de release público

**Justificación contextual:**
> "Un portfolio de arquitectura es más valioso cuando muestra el proceso, no solo el resultado. Los 17 catálogos eliminados no son 'basura técnica' - son evidencia documentada de que el arquitecto sabe identificar code smells, refactorizar responsabilidades y aplicar principios SOLID correctamente."

---

## Herramientas de Soporte

### Script de Validación

```bash
#!/bin/bash
# validate-error-codes.sh
# Valida que no haya códigos duplicados

grep -r "\"RN-" src/ | sort | uniq -d
if [ $? -eq 0 ]; then
  echo "❌ ERROR: Códigos duplicados encontrados"
  exit 1
else
  echo "✅ OK: No hay códigos duplicados"
fi
```

### GitHub Action

```yaml
name: Validate Error Codes
on: [pull_request]
jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Check for duplicate codes
        run: ./scripts/validate-error-codes.sh
```

---

## Consecuencias

### Positivas ✅
1. Trazabilidad histórica completa
2. Compatibilidad con logs antiguos
3. Referencias externas permanecen válidas
4. Práctica profesional estándar
5. Facilita auditorías y cumplimiento

### Negativas ⚠️
1. Numeración no siempre secuencial (huecos visibles)
2. Requiere disciplina en documentación
3. Código fuente tiene comentarios de eliminados

### Mitigaciones 🛡️
1. **ADR-038** documenta todos los eliminados
2. Template de comentario estandarizado
3. Script de validación automatizado
4. Code review obligatorio para cambios en catálogos

---

## Decisión de Diseño: ¿Dónde Documentar Eliminados?

Se decidió **doble documentación**:

1. **En el código (comentarios):**
    - Acceso inmediato para developers
    - Visible en IDE
    - Aparece en búsquedas de código

2. **En ADR-038 (archivo formal):**
    - Registro histórico oficial
    - Justificaciones completas
    - Referencias cruzadas
    - Evidencia para auditorías

---

## Referencias

- [ADR-(Arquitectura)-20-Alcance Experimental del Módulo Actor.md](ADR-%28Arquitectura%29-20-Alcance%20Experimental%20del%20M%C3%B3dulo%20Actor.md)
- [ADR-(Arquitectura)-23-Catálogos Eliminados - Histórico del Módulo Actor.md](ADR-%28Arquitectura%29-23-Cat%C3%A1logos%20Eliminados%20-%20Hist%C3%B3rico%20del%20M%C3%B3dulo%20Actor.md)
- [RFC 7231](https://tools.ietf.org/html/rfc7231) - HTTP Status Codes (inmutabilidad)
- [Semantic Versioning](https://semver.org/) - Principios de versionado
- Eric Evans, "Domain-Driven Design" (2003)

---


**Resumen ejecutivo:** Los códigos de error son **inmutables y trazables**. La numeración actúa como "timestamp lógico" que documenta la evolución del sistema. Los catálogos eliminados se marcan con comentarios in-code y se registran formalmente en ADR-038.