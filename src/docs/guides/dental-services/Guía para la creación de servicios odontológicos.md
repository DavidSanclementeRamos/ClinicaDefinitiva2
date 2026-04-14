

# 📘 Guía para la creación de servicios odontológicos

Este documento explica cómo funciona el módulo de **servicios odontológicos** de la clínica, qué datos debes enviar al crear un servicio y cómo extender el sistema para soportar nuevos tipos de servicios con detalles específicos.

## 1. Visión general

Un servicio odontológico puede ser de dos naturalezas:

- **General** – No requiere atributos especiales (ej. limpieza, consulta, aplicación de flúor).
- **Especializado** – Requiere un conjunto de atributos propios de la especialidad (ortodoncia, cirugía, estética, implantología, pediatría, prótesis).

El diseño sigue el patrón **“tabla por subclase”**:

- Una tabla principal `servicio_odontologico` guarda los atributos comunes (nombre, código, tarifa, duración, etc.).
- Seis tablas adicionales almacenan los detalles específicos de cada especialidad.

## 2. Crear un servicio vía API

**Endpoint:** `POST /api/v1/services`

**Headers:**  
`Authorization: Bearer <token>` (usuario autenticado con rol `ADMINISTRATOR` o `RECEPTIONIST` con sector `ADMINISTRATION`)

El cuerpo de la petición es un JSON que debe contener los campos obligatorios y, opcionalmente, un objeto `details` con los atributos específicos según el `serviceType`.

### 2.1 Campos comunes (siempre requeridos)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `name` | string | Nombre del servicio (mínimo 3 caracteres) |
| `categoryId` | entero | ID del catálogo (puede ser cualquier número, se usa para relación) |
| `categoryName` | string | Nombre de la categoría (ej. "ORTODONCIA") |
| `categoryType` | string | Tipo de categoría (debe coincidir con el `serviceType` según validación) |
| `code` | string | Código único del servicio (ej. "ORT-001") |
| `baseRateAmount` | decimal | Tarifa base del servicio |
| `currency` | string | Código de moneda (ej. "COP", "USD") |
| `durationMinutes` | entero | Duración en minutos (entre 15 y 480) |
| `requiresAuthorization` | booleano | Si requiere autorización previa |
| `description` | string | Descripción del servicio (máximo 500 caracteres) |
| `serviceType` | string | Tipo de servicio (`GENERAL`, `ORTHODONTIC`, `SURGERY`, `AESTHETICS`, `IMPLANTOLOGY`, `PEDIATRICS`, `PROSTHETICS`) |
| `details` | objeto | **Opcional** – Atributos específicos según el `serviceType`. |

### 2.2 Ejemplos de JSON por tipo de servicio

#### a) Servicio general (`serviceType: "GENERAL"`)

No requiere `details` (puede omitirse o enviarse vacío).

```json
{
  "name": "Profilaxis y Limpieza",
  "categoryId": 2,
  "categoryName": "Profilaxis",
  "categoryType": "GENERAL",
  "code": "SERV-001",
  "baseRateAmount": 80.00,
  "currency": "COP",
  "durationMinutes": 40,
  "requiresAuthorization": false,
  "description": "Eliminación de placa bacteriana y sarro.",
  "serviceType": "GENERAL"
}
```

#### b) Ortodoncia (`serviceType: "ORTHODONTIC"`)

```json
{
  "name": "Ortodoncia con Brackets Metálicos",
  "categoryId": 3,
  "categoryName": "Ortodoncia",
  "categoryType": "ORTHODONTIC",
  "code": "ORT-001",
  "baseRateAmount": 1500000.00,
  "currency": "COP",
  "durationMinutes": 60,
  "requiresAuthorization": true,
  "description": "Tratamiento de ortodoncia con brackets metálicos.",
  "serviceType": "ORTHODONTIC",
  "details": {
    "applianceType": "METAL_BRACKETS",
    "treatmentDurationMonths": 24,
    "requiresFollowup": true
  }
}
```

| Campo en `details` | Tipo | Descripción | Valores permitidos |
|--------------------|------|-------------|--------------------|
| `applianceType` | string | Tipo de aparato | `METAL_BRACKETS`, `CERAMIC_BRACKETS`, `LINGUAL_BRACKETS`, `CLEAR_ALIGNERS`, `REMOVABLE_APPLIANCES`, `FUNCTIONAL_APPLIANCES` |
| `treatmentDurationMonths` | entero | Duración en meses | Entre 6 y 48 |
| `requiresFollowup` | booleano | ¿Requiere seguimiento? | `true` o `false` |

#### c) Cirugía (`serviceType: "SURGERY"`)

```json
{
  "name": "Extracción de Muela del Juicio",
  "categoryId": 5,
  "categoryName": "Cirugía Oral",
  "categoryType": "SURGERY",
  "code": "CIR-001",
  "baseRateAmount": 350000.00,
  "currency": "COP",
  "durationMinutes": 90,
  "requiresAuthorization": true,
  "description": "Extracción quirúrgica de terceros molares.",
  "serviceType": "SURGERY",
  "details": {
    "surgeryType": "EXTRACTION",
    "complexityLevel": "MEDIUM",
    "requiresAnesthesia": true,
    "operatingRoomNeeded": true
  }
}
```

| Campo en `details` | Tipo | Descripción | Valores permitidos |
|--------------------|------|-------------|--------------------|
| `surgeryType` | string | Tipo de cirugía | Texto libre (mínimo 3 caracteres) |
| `complexityLevel` | string | Nivel de complejidad | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| `requiresAnesthesia` | booleano | Requiere anestesia | `true` o `false` |
| `operatingRoomNeeded` | booleano | Requiere quirófano | `true` o `false` |

#### d) Estética (`serviceType: "AESTHETICS"`)

```json
{
  "name": "Blanqueamiento Dental",
  "categoryId": 9,
  "categoryName": "Estética",
  "categoryType": "AESTHETICS",
  "code": "EST-001",
  "baseRateAmount": 400000.00,
  "currency": "COP",
  "durationMinutes": 60,
  "requiresAuthorization": false,
  "description": "Blanqueamiento dental con peróxido de hidrógeno.",
  "serviceType": "AESTHETICS",
  "details": {
    "aestheticType": "WHITENING",
    "materialUsed": "Peróxido de hidrógeno",
    "expectedResult": "Dientes más blancos en 3 tonos"
  }
}
```

| Campo en `details` | Tipo | Descripción | Valores permitidos |
|--------------------|------|-------------|--------------------|
| `aestheticType` | string | Tipo de procedimiento | `WHITENING`, `VENEER`, `BONDING`, `CONTOURING`, `GUM_RESHAPING`, `SMILE_DESIGN`, `COMPOSITE_RESTORATION`, `INLAY_ONLAY` |
| `materialUsed` | string | Material utilizado | Texto libre |
| `expectedResult` | string | Resultado esperado | Mínimo 10 caracteres |

#### e) Implantología (`serviceType: "IMPLANTOLOGY"`)

```json
{
  "name": "Implante Dental Unitario",
  "categoryId": 11,
  "categoryName": "Implantología",
  "categoryType": "IMPLANTOLOGY",
  "code": "IMP-001",
  "baseRateAmount": 2800000.00,
  "currency": "COP",
  "durationMinutes": 120,
  "requiresAuthorization": true,
  "description": "Colocación de implante dental de titanio.",
  "serviceType": "IMPLANTOLOGY",
  "details": {
    "healingTimeMonths": 6,
    "implantType": "TITANIO",
    "placementSite": "Maxilar superior",
    "requiresBoneGraft": false
  }
}
```

| Campo en `details` | Tipo | Descripción | Restricciones |
|--------------------|------|-------------|---------------|
| `healingTimeMonths` | entero | Meses de cicatrización | Entre 2 y 12; si `requiresBoneGraft = true`, mínimo 4 |
| `implantType` | string | Tipo de implante | Texto libre |
| `placementSite` | string | Sitio de colocación | Mínimo 2 caracteres |
| `requiresBoneGraft` | booleano | Requiere injerto óseo | `true` o `false` |

#### f) Pediatría (`serviceType: "PEDIATRICS"`)

```json
{
  "name": "Aplicación de Sellantes",
  "categoryId": 7,
  "categoryName": "Odontopediatría",
  "categoryType": "PEDIATRICS",
  "code": "PED-001",
  "baseRateAmount": 120000.00,
  "currency": "COP",
  "durationMinutes": 30,
  "requiresAuthorization": false,
  "description": "Sellantes dentales en niños.",
  "serviceType": "PEDIATRICS",
  "details": {
    "ageRange": { "minAge": 3, "maxAge": 12 },
    "behaviorManagement": "Hablar y enseñar",
    "pediatricMaterials": "Sellante a base de resina"
  }
}
```

| Campo en `details` | Tipo | Descripción | Restricciones |
|--------------------|------|-------------|---------------|
| `ageRange` | objeto | Rango de edad | `minAge` ≥ 0, `maxAge` > `minAge` |
| `behaviorManagement` | string | Manejo conductual | Texto libre |
| `pediatricMaterials` | string | Materiales | Mínimo 5 caracteres |

#### g) Prótesis (`serviceType: "PROSTHETICS"`)

```json
{
  "name": "Corona de Porcelana",
  "categoryId": 12,
  "categoryName": "Prótesis",
  "categoryType": "PROSTHETICS",
  "code": "PRO-001",
  "baseRateAmount": 850000.00,
  "currency": "COP",
  "durationMinutes": 90,
  "requiresAuthorization": true,
  "description": "Corona dental de porcelana.",
  "serviceType": "PROSTHETICS",
  "details": {
    "fixedOrRemovable": "FIXED",
    "material": "Porcelana",
    "prostheticType": "CROWN",
    "units": 1
  }
}
```

| Campo en `details` | Tipo | Descripción | Restricciones |
|--------------------|------|-------------|---------------|
| `fixedOrRemovable` | string | Fija o removible | `FIXED` o `REMOVABLE` |
| `material` | string | Material | Texto libre |
| `prostheticType` | string | Tipo de prótesis | Texto libre |
| `units` | entero | Número de unidades | ≥ 0; si `fixedOrRemovable = REMOVABLE`, ≤ 14 |

## 3. Validaciones importantes (resumen)

- **Nombres** – Mínimo 3 caracteres.
- **Código** – 4 a 15 caracteres, solo mayúsculas, números y guiones; debe ser único.
- **Duración** – Entre 15 y 480 minutos.
- **Tarifa** – Monto positivo.
- **Detalles específicos** – Cada tipo tiene sus propias validaciones (rangos, listas cerradas, longitudes mínimas). Consulta la documentación de cada clase `*Details` para más detalles.

## 4. Cómo extender el sistema con un nuevo tipo de servicio detallado

Si necesitas agregar una nueva especialidad (ej. `LASER_THERAPY`), sigue estos pasos:

### 4.1 Crear la clase de detalles

Crea una clase que implemente `ServiceDetails` en el paquete `domain.dentalService.model`. Ejemplo:

```java
public final class LaserDetails implements ServiceDetails {
    private final String laserType;
    private final Integer powerWatts;
    private final boolean requiresProtection;

    public LaserDetails(String laserType, Integer powerWatts, Boolean requiresProtection) {
        // validaciones
        this.laserType = laserType;
        this.powerWatts = powerWatts;
        this.requiresProtection = Boolean.TRUE.equals(requiresProtection);
    }

    @Override
    public ServiceType serviceType() { return ServiceType.LASER_THERAPY; }

    // getters...
}
```

### 4.2 Agregar el nuevo tipo al enum `ServiceType`

```java
public enum ServiceType {
    // ... existentes
    LASER_THERAPY
}
```

### 4.3 Modificar la fábrica `ServiceDetailsFactory`

Añadir el nuevo caso en `fromMap` y un método de creación:

```java
public static ServiceDetails createLaser(String laserType, Integer powerWatts, Boolean requiresProtection) {
    return new LaserDetails(laserType, powerWatts, requiresProtection);
}

// Dentro de fromMap:
case LASER_THERAPY -> createLaser(
    (String) fields.get("laserType"),
    (Integer) fields.get("powerWatts"),
    (Boolean) fields.get("requiresProtection")
);
```

### 4.4 Crear la entidad JPA y su mapper

- Crea una tabla `servicio_detalle_laser` con columnas: `id_servicio` (FK), `laser_type`, `power_watts`, `requires_protection`.
- Crea una entidad JPA `LaserDetailsEntity`.
- Extiende los mappers (dominio ↔ entidad) para manejar la nueva relación.

### 4.5 Actualizar las validaciones de categoría

En `ProvidedService.validateCategoryMatch`, agrega la nueva categoría al mapa `allowed`:

```java
Map.of(
    // ...
    ServiceType.LASER_THERAPY, Set.of("LASER_THERAPY", "Laser")
);
```

### 4.6 Actualizar los DTOs de aplicación (opcional)

Si deseas que el DTO `CreateServiceDto` acepte los nuevos campos, no necesitas modificar nada porque `details` es un `Map<String, Object>` genérico. Solo asegúrate de que el JSON use las claves correctas (`laserType`, `powerWatts`, `requiresProtection`).

### 4.7 Prueba la creación

Envía un JSON con `serviceType: "LASER_THERAPY"` y los campos correspondientes en `details`.

## 5. Consideraciones finales para desarrolladores externos

- **Usa siempre las claves en inglés** en el objeto `details` (ej. `applianceType`, no `tipoAparato`).
- **Respeta los tipos de datos** (Integer, Boolean, String). La fábrica espera esos tipos exactos.
- **No envíes campos adicionales** dentro de `details` para un tipo que no los requiere; serán ignorados pero no causarán error.
- **El código del servicio debe ser único** en toda la base de datos.
- **La categoría (`categoryType`) debe coincidir con el tipo de servicio** según la matriz de validación (ver `validateCategoryMatch`). Si usas un valor no mapeado, la creación fallará.

---

*Documento generado para facilitar la contribución al proyecto open source. Para dudas o mejoras, abre un issue en el repositorio.*



