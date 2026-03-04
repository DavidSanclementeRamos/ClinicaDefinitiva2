
package com.example.ClinicaDefinitiva.domain.payment.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

/**
 * Value Object: Payer
 * 
 * Representa quién realiza el pago:
 * - Paciente directo
 * - EPS
 * - Aseguradora
 * - Empresa (convenio corporativo)
 */
public final class Payer {
    
    public enum PayerType {
        PATIENT("Paciente"),
        EPS("EPS"),
        INSURANCE("Aseguradora"),
        COMPANY("Empresa"),
        OTHER("Otro");
        
        private final String displayName;
        
        PayerType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private final PayerType type;
    private final String identifier; // NIT, documento, código
    private final String name;
    
    private Payer(PayerType type, String identifier, String name) {
        if (type == null) {
            throw new ValueObjectValidationException(
                PaymentVoError.ERR_PAYMENT_PAYER_TYPE_NULL,
                VOContext.PAYMENT
            );
        }
        
        if (name == null || name.isBlank()) {
            throw new ValueObjectValidationException(
                PaymentVoError.ERR_PAYMENT_PAYER_NAME_NULL,
                VOContext.PAYMENT
            );
        }
        
        this.type = type;
        this.identifier = identifier != null ? identifier.trim() : null;
        this.name = name.trim();
    }
    
    public static Payer of(PayerType type, String identifier, String name) {
        return new Payer(type, identifier, name);
    }
    
    public static Payer patient(String patientName) {
        return new Payer(PayerType.PATIENT, null, patientName);
    }
    
    public static Payer eps(String epsName, String nit) {
        return new Payer(PayerType.EPS, nit, epsName);
    }
    
    public static Payer insurance(String insuranceName, String nit) {
        return new Payer(PayerType.INSURANCE, nit, insuranceName);
    }
    
    public static Payer company(String companyName, String nit) {
        return new Payer(PayerType.COMPANY, nit, companyName);
    }
    
    // Consultas semánticas
    public boolean isPatient() { return type == PayerType.PATIENT; }
    public boolean isEPS() { return type == PayerType.EPS; }
    public boolean isInsurance() { return type == PayerType.INSURANCE; }
    public boolean isCompany() { return type == PayerType.COMPANY; }
    public boolean isInstitutional() { return !isPatient(); }
    
    // Getters
    public PayerType getType() { return type; }
    public String getIdentifier() { return identifier; }
    public String getName() { return name; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payer)) return false;
        Payer payer = (Payer) o;
        return type == payer.type && 
               Objects.equals(identifier, payer.identifier) && 
               Objects.equals(name, payer.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, identifier, name);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s (%s)", type.getDisplayName(), name, 
            identifier != null ? identifier : "N/A");
    }
}
