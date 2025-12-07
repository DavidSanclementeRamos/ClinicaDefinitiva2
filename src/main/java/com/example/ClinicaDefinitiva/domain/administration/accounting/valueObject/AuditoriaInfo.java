package com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject;

import java.time.LocalDateTime;

public record AuditoriaInfo (String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
}
