package com.example.ClinicaDefinitiva.domain.billing.doiman;

import java.time.LocalDateTime;

public class Rate {
    //Define la tarifa aplicable a un servicio, según convenio o EPS

    private final String id;                   // Identificador único
    private final String service_id;           // Referencia al servicio
    private final String payer_type;           // Tipo de pagador (private, EPS, insurer)
    private final String contract_id;          // Referencia a contrato/convenio (opcional)
    private final float amount;             // Valor de la tarifa
    private final String currency;             // Moneda (ej. COP, USD)
    private final LocalDateTime valid_from;          // Vigencia inicial
    private final LocalDateTime valid_to;            // Vigencia final
    private final boolean is_active;           // Estado de la tarifa

    public Rate(float amount, String id, String service_id, String payer_type, String contract_id, String currency, LocalDateTime valid_from, LocalDateTime valid_to, boolean is_active) {
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

    public float getAmount() {return amount;}
    public String getContract_id() {return contract_id;}
    public String getCurrency() {return currency;}
    public String getId() {return id;}
    public boolean isIs_active() {return is_active;}
    public String getPayer_type() {return payer_type;}
    public String getService_id() {return service_id;}
    public LocalDateTime getValid_from() {return valid_from;}
    public LocalDateTime getValid_to() {return valid_to;}
}
