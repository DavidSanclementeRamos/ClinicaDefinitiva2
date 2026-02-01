package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Set;

public final class TypeGuardian {

    // Códigos válidos reconocidos
    private static final Set<String> VALID_CODES = Set.of(
            "MAMA", "PAPA", "HERMANO", "HERMANA",
            "ABUELO", "ABUELA", "TIO", "TIA",
            "PRIMO", "PRIMA", "TUTOR_LEGAL", "OTRO"
    );

    private final String code;
    private final String description;

    public TypeGuardian(String code, String description) {
        // Validar código
        if (code == null) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_NULL, VOContext.TYPE_GUARDIAN);
        }

        String normalizedCode = code.trim().toUpperCase();
        if (normalizedCode.isEmpty()) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_BLANK, VOContext.TYPE_GUARDIAN);
        }

        if (!VALID_CODES.contains(normalizedCode)) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_INVALID,VOContext.TYPE_GUARDIAN);
        }

        // Validar descripción
        if (description == null || description.trim().isEmpty()) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_DESCRIPTION_BLANK, VOContext.TYPE_GUARDIAN);
        }

        this.code = normalizedCode;
        this.description = description.trim();
    }

    // Fábrica pública con validación
    public static TypeGuardian of(String code, String description) {
        return new TypeGuardian(code, description);
    }

    // Instancias estáticas predefinidas (familia nuclear)
    public static final TypeGuardian MAMA = new TypeGuardian("MAMA", "Madre");
    public static final TypeGuardian PAPA = new TypeGuardian("PAPA", "Padre");
    public static final TypeGuardian HERMANO = new TypeGuardian("HERMANO", "Hermano");
    public static final TypeGuardian HERMANA = new TypeGuardian("HERMANA", "Hermana");
    public static final TypeGuardian ABUELO = new TypeGuardian("ABUELO", "Abuelo");
    public static final TypeGuardian ABUELA = new TypeGuardian("ABUELA", "Abuela");
    public static final TypeGuardian TIO = new TypeGuardian("TIO", "Tío");
    public static final TypeGuardian TIA = new TypeGuardian("TIA", "Tía");
    public static final TypeGuardian PRIMO = new TypeGuardian("PRIMO", "Primo");
    public static final TypeGuardian PRIMA = new TypeGuardian("PRIMA", "Prima");
    public static final TypeGuardian TUTOR_LEGAL = new TypeGuardian("TUTOR_LEGAL", "Tutor Legal");
    public static final TypeGuardian OTRO = new TypeGuardian("OTRO", "Otro");

    // Métodos semánticos
    public boolean isParent() {
        return code.equals("MAMA") || code.equals("PAPA");
    }

    public boolean isGrandparent() {
        return code.equals("ABUELO") || code.equals("ABUELA");
    }

    public boolean isSibling() {
        return code.equals("HERMANO") || code.equals("HERMANA");
    }

    public boolean isLegalGuardian() {
        return code.equals("TUTOR_LEGAL");
    }

    public boolean isDirectFamily() {
        return isParent() || isSibling() || isGrandparent();
    }

    public int getLegalPriority() {
        // Prioridad legal en decisiones médicas
        return switch (code) {
            case "MAMA", "PAPA" -> 1;           // Máxima prioridad
            case "TUTOR_LEGAL" -> 2;            // Segunda prioridad
            case "ABUELO", "ABUELA" -> 3;       // Tercera prioridad
            case "HERMANO", "HERMANA" -> 4;     // Cuarta prioridad (si son mayores)
            default -> 5;                        // Menor prioridad
        };
    }

    // Getters
    public String getCode() { return code; }
    public String getDescription() { return description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeGuardian)) return false;
        return code.equals(((TypeGuardian) o).code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return description + " (" + code + ")";
    }
}