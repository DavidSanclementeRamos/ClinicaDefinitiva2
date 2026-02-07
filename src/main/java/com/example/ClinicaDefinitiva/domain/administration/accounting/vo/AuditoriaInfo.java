package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.time.LocalDateTime;

public record AuditoriaInfo (String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
}
