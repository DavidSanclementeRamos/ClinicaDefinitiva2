package com.example.ClinicaDefinitiva.persistence.dto;

import com.example.ClinicaDefinitiva.Enum.Estado;

public class RolesDto {
    private Estado status;

    public Estado getStatus() {
        return status;
    }

    public void setStatus(Estado status) {
        this.status = status;
    }
}
