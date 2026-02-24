package com.example.ClinicaDefinitiva.domain.vo;

import java.time.LocalDateTime;

public record AuditoriaInfo (String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
}
