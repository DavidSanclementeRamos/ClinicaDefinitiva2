

# ADR-04 (Facturación): Uso de ProviderId como emisor oficial en facturas

- **Fecha**: 2026-02-17
- **Estado**: Aprobado
- **Categoría**: Dominio
- **Autor**: David Stiven Sanclemente

---

## Problema

En el modelo inicial del agregado `Invoice`, se utilizaba `DentistId` como referencia del emisor de la factura. Sin embargo, en la práctica clínica y legal, los odontólogos no emiten facturas: la emisión corresponde a la clínica o entidad administrativa (ej. recepcionistas del área de facturación).

Esto genera una inconsistencia entre el modelo y la realidad operativa: el sistema debe reflejar tanto el profesional que prestó el servicio como la entidad que emite la factura. Si no se resuelve, se corre el riesgo de incumplir requisitos legales y perder trazabilidad administrativa.

---

## Decisión

Introducir un **Value Object `ProviderId`** como atributo obligatorio en el agregado `Invoice`, representando la clínica o entidad que emite la factura.

Regla:
- RN-INVOICE-014: Toda factura debe tener un `ProviderId` válido como emisor oficial.
- `DentistId` se mantiene como referencia clínica del profesional que prestó el servicio.
- Opcionalmente se agrega `UserId` para registrar quién emitió la factura en el sistema (ej. recepcionista).

```java
private final DentistId dentistId;   // Profesional que prestó el servicio
private final ProviderId providerId; // Clínica/emisor oficial
private final UserId issuedBy;       // Usuario administrativo que emitió la factura
```

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Usar solo `DentistId` | No refleja la realidad legal: el odontólogo no emite facturas |
| Usar `long providerId` plano | Carece de semántica y validación; difícil de extender |
| Usar solo `UserId` (recepcionista) | Pierde la referencia a la clínica/emisor oficial |

---

## Consecuencias

### Ganamos
- Modelo alineado con la realidad legal y operativa.
- Trazabilidad clínica (dentista) y administrativa (usuario emisor).
- Flexibilidad para auditar quién emitió la factura y bajo qué proveedor.
- Posibilidad de extender `ProviderId` con atributos legales (NIT, resolución DIAN).

### Perdemos
- Mayor complejidad en el agregado (más atributos).
- Necesidad de mantener consistencia entre `ProviderId` y `UserId`.
- Validaciones adicionales en la creación de facturas.

---

## Implementación

```java
public final class ProviderId {
    private final long value;

    private ProviderId(long value) {
        if (value <= 0) {
            throw new ValueObjectValidationException(
                BillingVOError.ERR_INVOICE_PROVIDER_REQUIRED,
                VOContext.BILLING_PROVIDER,
                "El identificador del proveedor debe ser mayor a 0"
            );
        }
        this.value = value;
    }

    public static ProviderId of(long value) { return new ProviderId(value); }
    public long getValue() { return value; }
}
```

---

## Notas adicionales

- `UserId` puede añadirse en el futuro para registrar al recepcionista que emitió la factura.
- `ProviderId` podrá extenderse con datos legales requeridos por la DIAN.
- Este ADR reemplaza la práctica anterior de usar `DentistId` como emisor.

mantiene DentistId como referencia clínica del profesional que prestó el servicio.
- Se documentó ADR-014 que justifica la decisión: las facturas son emitidas por la clínica, no por el odontólogo.
- Se dejó abierta la posibilidad de agregar UserId para registrar al recepcionista que emitió la factura.

Impacto:
El modelo ahora refleja tanto la realidad clínica (dentista) como la administrativa/legal (clínica/recepcionista),
asegurando trazabilidad y cumplimiento normativo en la emisión de facturas.



