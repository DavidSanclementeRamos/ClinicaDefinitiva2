# ADR: Funcionalidad pedagógica de los mappers

## Contexto
En arquitecturas limpias y DDD, los **DTOs** transportan datos entre capas, mientras que el **dominio** trabaja con entidades y Value Objects (VOs).  
Para conectar ambos mundos usamos **mappers/assemblers**, pero es común confundirse sobre qué hace cada uno y en qué dirección.

## Decisión
Separar los mappers según el **sentido de la operación**:

### 1. Mapper de lectura (Domain → Read DTO)
- **Función:** Convierte entidades y VOs del dominio en DTOs simples y serializables.
- **Uso:** Exponer datos hacia afuera (REST, UI, reportes).
- **Ejemplo:** `Dentist → ReadDentistDto`.

### 2. Assembler de escritura (Write DTO → Domain)
- **Función:** Convierte DTOs de entrada (con primitivos) en VOs y entidades del dominio.
- **Uso:** Operaciones de creación o actualización.
- **Ejemplo:** `UpdateDentistSensitiveDto → VOs → Dentist.updateSensitiveData`.

### 3. Mapper por operación
- Cada operación importante (crear, actualizar datos sensibles, actualizar contacto, actualizar estado, listar con paginación) debe tener su propio mapper/assembler.
- No usar un mapper genérico para todo, porque cada DTO tiene estructura distinta.

## Justificación
- **Claridad:** Cada mapper refleja una operación concreta, evitando confusión.
- **Separación de responsabilidades:** Los DTOs no conocen el dominio; los VOs no dependen de DTOs.
- **Mantenibilidad:** Si cambia un DTO o VO, solo se ajusta el mapper correspondiente.
- **Trazabilidad:** Cada operación queda documentada con su DTO y su mapper.

## Consecuencias
- Habrá varios mappers pequeños y específicos.
- La capa de aplicación se mantiene limpia: orquesta mappers y repositorios, sin lógica de construcción.
- El dominio recibe siempre VOs, preservando reglas de negocio.

## Ejemplo de estructura
```
dentist/
application/
dto/
ReadDentistDto.java
UpdateDentistSensitiveDto.java
UpdateDentistContactDto.java
CreateDentistDto.java
mapper/
DentistReadMapper.java
DentistUpdateSensitiveMapper.java
DentistUpdateContactMapper.java
DentistCreateMapper.java
```

# Chuleta visual: Mappers y DTOs en DDD

## 📤 Lectura (Domain → Read DTO)
```
Dominio (Entidades + VOs) ──► Mapper de salida ──► Read DTO (primitivos
```
- **Función:** exponer datos hacia afuera (REST, UI, reportes).
- **Ejemplo:** `Dentist → ReadDentistDto`.

---

## 📥 Escritura (Write DTO → Domain)

```
Write DTO (primitivos) ──► Assembler de entrada ──► Dominio (Entidades + VOs)
```
- **Función:** convertir datos de entrada en VOs y entidades.
- **Ejemplo:** `UpdateDentistSensitiveDto → VOs → Dentist.updateSensitiveData`.

---

## 📌 Mapper por operación
Cada operación importante tiene su propio DTO y su propio mapper/assembler:

```
CreateDentistDto ──► DentistCreateMapper ──► Dentist
UpdateDentistSensitiveDto ──► DentistUpdateSensitiveMapper ──► Dentist
UpdateDentistContactDto ──► DentistUpdateContactMapper ──► Dentist
UpdateDentistStatusDto ──► DentistUpdateStatusMapper ──► Dentist
Dentist ──► DentistReadMapper ──► ReadDentistDto
```


---

## 🎯 Reglas rápidas
- **Read DTO → Mapper de salida** (dominio a primitivos).
- **Write DTO → Assembler de entrada** (primitivos a VOs).
- **Un mapper por operación**: no mezclar responsabilidades.
- **DTOs son simples, VOs son ricos**: el mapper es el puente.

---

## 🧠 Recordatorio mental
- **Salida = Mapper**
- **Entrada = Assembler**
- **Operación = Mapper específico**  

```
Entrada (DTO) → Assembler → Dominio
Dominio → Mapper → Salida (DTO)
```


## Estado
Aceptado. Este patrón se aplicará en todos los módulos clínicos para mantener consistencia y claridad en el uso de mappers.
