package com.example.ClinicaDefinitiva.domain.billing.doiman.model;

import com.example.ClinicaDefinitiva.domain.administration.contable.Contract;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.Money;
import java.time.LocalDateTime;

public class Rate {
    //Define la tarifa aplicable a un servicio, según convenio o EPS

    private final RateId id;                   // Identificador único
    private final ProvidedService service_id;           // Referencia al servicio
    private final String payer_type;           // Tipo de pagador (private, EPS, insurer)
    private final Contract contract_id;          // Referencia a contrato/convenio (opcional)
    private final Money amount;             // Valor de la tarifa
    private String currency;             // Moneda (ej. COP, USD)
    private final LocalDateTime valid_from;          // Vigencia inicial
    private final LocalDateTime valid_to;            // Vigencia final
    private final boolean is_active;           // Estado de la tarifa

    public Rate(Money amount, RateId id, ProvidedService service_id, String payer_type, Contract contract_id, String currency, LocalDateTime valid_from, LocalDateTime valid_to, boolean is_active) {
        this.amount = amount;
        this.id = id;
        this.service_id = service_id;
        this.payer_type = payer_type;
        this.contract_id = contract_id;
        this.currency = currency;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.is_active = is_active;
    }
    public boolean isValidAt(LocalDateTime when) {
        if (!is_active) return false;
        if (valid_from != null && when.isBefore(valid_from)) return false;
        if (valid_to != null && when.isAfter(valid_to)) return false;
        return true;
    }

    public Money getAmount() {return amount;}
    public ProvidedService getServiceId() {
        return service_id;
    }
    public String getCurrency() {
        return currency;
    }
    public Contract getContract_id() {
        return contract_id;
    }
    public RateId getId() {return id;}
    public boolean isIs_active() {return is_active;}
    public String getPayer_type() {return payer_type;}
    public LocalDateTime getValid_from() {return valid_from;}
    public LocalDateTime getValid_to() {return valid_to;}
}
