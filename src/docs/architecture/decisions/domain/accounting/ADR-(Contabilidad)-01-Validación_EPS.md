# ADR-01 (Contabilidad): Estrategia de validación de EPS del paciente

- Estado: Aprobado
- Fecha: 2025-10-14
- Autor: David Stiven Sanclemente

## Contexto
Se evaluaron tres opciones para validar la EPS del paciente:
1. Integración en tiempo real: con servicios oficiales de EPS/Ministerio.
2. Sincronización periódica (BDUA): cargar y cruzar bases oficiales.
3. Registro manual con trazabilidad: personal administrativo registra la EPS y adjunta soportes.

## Decisión
Se elige la opción 3: Registro manual con trazabilidad.

Reglas clave
- Registro administrativo: la clínica registra la EPS del paciente en el sistema.
- Soportes obligatorios: adjuntar o referenciar documentos (certificado EPS, identificación, formularios).
- Bloqueo de facturación: no se permite generar factura sin EPS válida registrada y vinculada al paciente.

## Consecuencias
Positivas
- Implementación rápida y baja complejidad técnica.
- Evidencia documental para auditorías y control antifraude.

Negativas
- Mayor carga operativa para personal administrativo.
- Riesgo de error humano en el registro.
- Menor automatización comparado con integración oficial.

Consideraciones futuras
- Revaluar integración en tiempo real o sincronización BDUA cuando existan recursos y acuerdos.
- Revisar este ADR al planificar automatizaciones de afiliación y mejoras de gobernanza de datos.

## Impacto en módulos
- Administración: gestión de EPS/Contracts, almacenamiento de soportes y políticas de vigencia.
- Facturación: validación previa de EPS; bloqueo si falta o está vencida; trazabilidad en la factura al contrato y documentos.
- Integraciones (futuro): conectores a servicios oficiales y/o procesos ETL para sincronización de afiliaciones.

## Plan de implementación
1. Crear entidad EPS en módulo Administration con atributos: id, nombre, vigencia, soportes.
2. Implementar validación en Facturación: Factura.generar() debe verificar EPS válida.
3. Documentar reglas en docs/dominio/reglas-de-negocio/eps.md.
4. Añadir pruebas unitarias:
  - Factura sin EPS → excepción.
  - Factura con EPS vencida → excepción.
  - Factura con EPS válida → éxito.

## Ejemplo
```java
public void generarFactura(Paciente paciente, List<Servicio> servicios) {
    if (paciente.getEps() == null || !paciente.getEps().isVigente()) {
        throw new EpsNoValidaException("EPS no registrada o vencida");
    }
    // continuar con generación de factura
}
```

## Relación con otros ADR
- [ADR-(Arquitectura)-07-Redefinición del módulo Administration.md](../../arch/ADR-%28Arquitectura%29-07-Redefinici%C3%B3n%20del%20m%C3%B3dulo%20Administration.md)
- [ADR-(Arquitectura)-08-Estrategia de Integraciones.md](../../arch/ADR-%28Arquitectura%29-08-Estrategia%20de%20Integraciones.md)
  

