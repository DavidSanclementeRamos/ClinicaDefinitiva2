📄 ADR 2: Ubicación del VO Price

Título: Ubicación del Value Object Price  
Estado: Recorded after implementation  
Fecha: (retroactivo, se documenta después de la implementación)

---

Contexto
El VO Price encapsula valor monetario, moneda y reglas básicas (ej. no negativos).  
La duda: ¿en qué módulo debe vivir para mantener coherencia y evitar acoplamientos innecesarios?

Este ADR se redacta retrospectivamente para formalizar una decisión ya aplicada en el código.

---

Soluciones evaluadas

Opción A: En Service
- Price vive en el módulo odontológico.
- Rate lo consume desde allí.

Ventajas:
- Semánticamente claro: el servicio define su precio.
- Exhibible: muestra que el dominio clínico reconoce su valor económico.

Desventajas:
- Rate depende de un VO externo.
- Riesgo de acoplamiento entre módulos.

---

Opción B: En Rate
- Price vive en facturación.
- Service solo expone identificador.

Ventajas:
- Centralización de lógica económica.
- Service se mantiene clínicamente puro.

Desventajas:
- Necesidad de convenio artificial para pacientes particulares.
- Service queda “ciego” respecto a su valor estándar.

---

Opción C: En módulo compartido (shared-kernel)
- Price vive en un módulo común.
- Service lo usa para basePrice.
- Rate lo usa para cálculos.

Ventajas:
- Neutralidad y reutilización.
- Exhibible: diseño modular y extensible.
- Balance entre claridad y separación.

Desventajas:
- Riesgo de que el módulo compartido se convierta en un “dumping ground”.

---

Decisión
Se adopta la Opción C:
- Price vive en un módulo compartido (shared-kernel).
- Service lo usa para definir basePrice.
- Rate lo usa para aplicar convenios.

---

Consecuencias
- Positivas:
    - Neutralidad y reutilización.
    - Balance entre claridad y separación.
    - Exhibible y profesional.
- Negativas:
    - Necesidad de disciplina para mantener limpio el módulo compartido.

---