
package com.example.ClinicaDefinitiva.domain.billing;

public enum RateStatus {
    ACTIVE,       // tarifa vigente
    EXPIRED,      // tarifa finalizada por fecha de vigencia
    REPLACED,     // tarifa desactivada porque existe una nueva
    INACTIVE      // desactivada manualmente (ej. baja administrativa)
}