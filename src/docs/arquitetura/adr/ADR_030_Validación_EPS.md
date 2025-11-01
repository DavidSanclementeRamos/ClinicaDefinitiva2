# ADR-007: Estrategia de validación de EPS del paciente

**Estado:** Aceptado  
**Fecha:** 2025-10-14

---

## Contexto
- **Opciones evaluadas:**
    - **Integración en tiempo real:** Con servicios oficiales de EPS/Ministerio para validar afiliación automáticamente. Pros: precisión y automatización. Contras: alta complejidad, acuerdos legales y dependencia de APIs.
    - **Sincronización periódica (BDUA):** Cargar y cruzar bases oficiales. Pros: alineación de datos. Contras: requiere procesos ETL, gobernanza y actualizaciones regulares.
    - **Registro manual con trazabilidad:** Personal administrativo registra la EPS y adjunta soportes. Pros: implementación rápida y simple. Contras: más carga operativa y riesgo de error humano.

## Decisión
- **Elegimos la opción 3: Registro manual con trazabilidad.**
- **Reglas clave:**
    - **Registro administrativo:** La clínica registra la EPS del paciente en el sistema.
    - **Soportes obligatorios:** Adjuntar o referenciar documentos (certificado EPS, identificación, formularios).
    - **Bloqueo de facturación:** No se permite generar factura sin EPS válida registrada y vinculada al paciente.

## Consecuencias
- **Positivas:**
    - **Implementación rápida:** Baja complejidad técnica y disponibilidad inmediata.
    - **Trazabilidad:** Evidencia documental para auditorías y control antifraude.
- **Negativas:**
    - **Carga operativa:** Mayor trabajo para el personal administrativo.
    - **Riesgo humano:** Posibles errores en el registro y verificación manual.
    - **Menor automatización:** Comparado con integración o sincronización oficial.

## Consideraciones futuras
- **Evolución prevista:** Revaluar integración en tiempo real o sincronización BDUA cuando existan recursos, acuerdos y estabilidad técnica.
- **Revisión del ADR:** Actualizar esta decisión al planificar automatizaciones de afiliación y mejoras de gobernanza de datos.

## Impacto en módulos
- **Administración:** Gestión de EPS/Contracts, almacenamiento de soportes y políticas de vigencia.
- **Facturación:** Validación previa de EPS; bloqueo si falta o está vencida; trazabilidad en la factura al contrato y documentos.
- **Integraciones (futuro):** Conectores a servicios oficiales y/o procesos ETL para sincronización de afiliaciones.
