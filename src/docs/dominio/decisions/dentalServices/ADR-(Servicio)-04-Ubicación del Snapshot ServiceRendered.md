

# 📄 ADR 04 (Servicio): Ubicación del Snapshot ServiceRendered

Título: Ubicación del Snapshot ServiceRendered  
Estado: Recorded after implementation  
Fecha: (retroactivo, se documenta después de la implementación)

---

## Contexto
El módulo dental.care.service define el agregado Service, que representa un procedimiento odontológico (ej. limpieza, extracción).  
El módulo de facturación necesita información de los servicios para generar facturas, pero no debe recibir el agregado completo (por razones de encapsulación y separación de responsabilidades).

Para resolver esto, se creó un Snapshot llamado ServiceRendered, que contiene los datos mínimos necesarios para facturación (ej. identificador del servicio, nombre, precio base, fecha de realización).  
El inconveniente es que este Snapshot fue ubicado en la capa de aplicación, cuando en realidad pertenece al dominio del módulo dental.care.service.

---

## Soluciones evaluadas

Opción A: Mantener ServiceRendered en la capa de aplicación
- Snapshot definido como DTO de aplicación.
- Usado para transferir datos hacia facturación.

Ventajas:
- Fácil de implementar.
- Claridad inmediata: “es un objeto de transferencia”.

Desventajas:
- Semánticamente incorrecto: ServiceRendered representa un concepto del dominio, no de la aplicación.
- Riesgo de degradar la separación de capas.
- Exhibición débil: parece un DTO técnico, no un concepto de negocio.

---

Opción B: Ubicar ServiceRendered en el dominio de dental.care.service
- Snapshot definido como Domain Event / Domain Snapshot dentro del módulo odontológico.
- La capa de aplicación solo lo expone, pero no lo define.

Ventajas:
- Semánticamente correcto: ServiceRendered es parte del lenguaje ubicuo del dominio.
- Refuerza la separación de capas: aplicación no define conceptos de negocio.
- Exhibible: muestra cómo se encapsula el agregado y se expone solo lo necesario.

Desventajas:
- Requiere refactorizar ubicación y dependencias.
- Puede implicar mover código ya usado en facturación.

---

Opción C: Definir ServiceRendered en un módulo compartido
- Snapshot definido en shared-kernel.
- Usado tanto por dental.care.service como por facturación.

Ventajas:
- Neutralidad y reutilización.
- Exhibible: muestra diseño modular.

Desventajas:
- Riesgo de inflar el shared-kernel con conceptos que no son realmente transversales.
- ServiceRendered es específico del dominio odontológico, no de todos los módulos.

---

## Decisión
Se adopta la Opción B:
- ServiceRendered se mueve al dominio del módulo dental.care.service.
- La capa de aplicación solo lo expone, pero no lo define.
- Facturación consume este Snapshot como un contrato de datos mínimos.

---

## Consecuencias
- Positivas:
    - Semántica correcta: ServiceRendered es parte del dominio odontológico.
    - Refuerza separación de capas.
    - Exhibible: muestra cómo se encapsula un agregado y se expone solo lo necesario.
- Negativas:
    - Requiere refactorizar ubicación y dependencias.
    - Ajuste retrospectivo en documentación y código.

---
