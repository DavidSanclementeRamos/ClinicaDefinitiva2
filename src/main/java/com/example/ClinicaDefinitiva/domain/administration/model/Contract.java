package com.example.ClinicaDefinitiva.domain.administration.model;

import com.example.ClinicaDefinitiva.domain.administration.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.valueObject.ContractStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Contract {
    //convenio con EPS

    private final ContractId contractId;
    private final String name;          // Nombre del convenio
    private final String description;   // Descripción general
    private final String origin;        // Documento o entidad que lo respalda
    private final LocalDate startDate;  // Vigencia desde
    private final LocalDate endDate;    // Vigencia hasta
    private final String coverageType;  // Tipo de cobertura (EPS, private, insurance)
    private final Double coverageRate;  // Porcentaje de cobertura (ej. 0.8 = 80%)
    private final ContractStatus status;

    public Contract(ContractId contractId, Double coverageRate, String coverageType, String description, LocalDate endDate, String name, String origin, LocalDate startDate, ContractStatus status) {
        this.contractId = contractId;
        this.coverageRate = coverageRate;
        this.coverageType = coverageType;
        this.description = description;
        this.endDate = endDate;
        this.name = name;
        this.origin = origin;
        this.startDate = startDate;
        this.status = status;
    }

    public boolean isActiveAt(LocalDateTime when) {
        if (status != ContractStatus.Active) return false;

        LocalDate date = when.toLocalDate();
        if (startDate != null && date.isBefore(startDate)) return false;
        if (endDate != null && date.isAfter(endDate)) return false;
        return true;
    }

    // Regla de negocio: un convenio vencido no es aplicable
    public boolean isExpiredAt(LocalDateTime when) {
        if (endDate == null) return false;
        return when.toLocalDate().isAfter(endDate);
    }

    /**
     * Devuelve el número de días para plazo de pago si el contrato lo define.
     * Retorna null si no hay una política explícita.
     * Ajusta la fuente si guardas esto en BD (campo adicional paymentTermsDays).
     */
    public Integer getPaymentTermsDays() {
        // Si no tienes este dato en el constructor/BD devuelve null.
        // Puedes cambiar a return 30; si quieres forzar 30 días por contrato.
        return null;
    }



    public ContractStatus getStatus() {
        return status;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public Double getCoverageRate() {
        return coverageRate;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
