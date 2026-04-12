

#  ADR 3 (Servicio): Manejo de precios base en el agregado Service

- Estado: Aprobado  
- Fecha: (retroactivo, se documenta después de la implementación)
- Autor: David Stiven Sanclemente

---

## Contexto
En el módulo dental.care.service, los servicios clínicos (ej. limpieza, extracción, ortodoncia) deben tener un precio asociado para garantizar consistencia en la facturación.  
El módulo Rate (facturación) calcula tarifas según convenios (EPS, aseguradoras, acuerdos corporativos). Sin embargo, surgió un problema:
- Si un paciente no tiene convenio, el agregado Rate no puede calcular un precio, dejando al servicio sin valor económico.
- Esto rompe la consistencia del dominio, ya que un servicio clínico no puede existir sin un precio mínimo.

Este ADR se redacta retrospectivamente para formalizar una decisión que ya fue aplicada en el código, pero que no estaba documentada.

---

## Soluciones evaluadas

Opción A: Precio base en Service
- Cada servicio define un basePrice.
- Rate aplica convenios sobre ese precio.
- Pacientes sin convenio pagan directamente el precio base.

Ventajas:
- Garantiza que ningún servicio carezca de precio.
- Semánticamente claro: el servicio tiene un valor intrínseco.
- Exhibible: muestra cómo el dominio clínico reconoce su valor económico.

Desventajas:
- Service asume una mínima responsabilidad económica.
- Riesgo de mezclar dominio clínico con administrativo si no se documenta bien.

---

Opción B: Precio base en Rate con convenio “particular”
- Rate define tarifas estándar para pacientes sin convenio.
- Service no maneja precios.

Ventajas:
- Centralización de lógica económica en facturación.
- Service se mantiene clínicamente puro.

Desventajas:
- Necesidad de un convenio artificial (“particular”).
- Semánticamente forzado: un paciente particular no es un convenio.
- Riesgo de confusión en documentación y exhibición.

---

Opción C: Precio como VO compartido
- Definir un Price VO en un módulo compartido.
- Service usa Price para su basePrice.
- Rate usa Price para calcular tarifas derivadas.

Ventajas:
- Balance entre claridad y separación.
- Reutilizable en otros contextos (ej. insumos, gastos).
- Exhibible: muestra diseño modular y extensible.

Desventajas:
- Requiere disciplina para que el módulo compartido no se convierta en un “dumping ground”.

---

## Decisión
Se adopta la Opción C:
- El agregado Service define un basePrice usando el VO Price.
- El agregado Rate aplica convenios sobre ese precio.
- El VO Price vive en un módulo compartido (shared-kernel).

---

Consecuencias
- Positivas:
    - Consistencia: ningún servicio sin precio.
    - Claridad semántica: el servicio tiene valor intrínseco.
    - Extensibilidad: Price puede usarse en otros módulos.
- Negativas:
    - Service asume mínima responsabilidad económica.
    - Necesidad de disciplina para mantener limpio el módulo compartido.

---

