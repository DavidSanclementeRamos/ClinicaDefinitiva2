package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class TypeGuardian {

    // Códigos válidos reconocidos
    private static final Set<String> VALID_CODES = Set.of(
            "MAMA", "PAPA", "HERMANO", "HERMANA",
            "ABUELO", "ABUELA", "TIO", "TIA",
            "PRIMO", "PRIMA", "TUTOR_LEGAL", "OTRO"
    );

    // Mapa para cachear instancias y permitir reconstrucción por código
    private static final Map<String, TypeGuardian> INSTANCES = new ConcurrentHashMap<>();

    private final String code;
    private final String description;

    private TypeGuardian(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // Fábrica pública con validación completa
    public static TypeGuardian of(String code, String description) {
        // Validar código
        if (code == null) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_NULL, VOContext.ACTORS);
        }

        String normalizedCode = code.trim().toUpperCase();
        if (normalizedCode.isEmpty()) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_BLANK, VOContext.ACTORS);
        }

        if (!VALID_CODES.contains(normalizedCode)) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_INVALID, VOContext.ACTORS);
        }

        // Validar descripción
        if (description == null || description.trim().isEmpty()) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_DESCRIPTION_BLANK, VOContext.ACTORS);
        }

        // Crear y cachear la instancia
        return INSTANCES.computeIfAbsent(normalizedCode, 
            k -> new TypeGuardian(normalizedCode, description.trim()));
    }

    /**
     * Reconstruye un TypeGuardian solo con el código.
     * Útil para reconstrucción desde persistencia donde solo se almacena el código.
     */
    public static TypeGuardian fromCode(String code) {
        if (code == null) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_NULL, VOContext.ACTORS);
        }

        String normalizedCode = code.trim().toUpperCase();
        if (normalizedCode.isEmpty()) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_BLANK, VOContext.ACTORS);
        }

        if (!VALID_CODES.contains(normalizedCode)) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_TYPE_GUARDIAN_CODE_INVALID, VOContext.ACTORS);
        }

        // Buscar en el mapa de instancias o crear una nueva con descripción por defecto
        return INSTANCES.computeIfAbsent(normalizedCode, k -> {
            String defaultDescription = switch (normalizedCode) {
                case "MAMA" -> "Madre";
                case "PAPA" -> "Padre";
                case "HERMANO" -> "Hermano";
                case "HERMANA" -> "Hermana";
                case "ABUELO" -> "Abuelo";
                case "ABUELA" -> "Abuela";
                case "TIO" -> "Tío";
                case "TIA" -> "Tía";
                case "PRIMO" -> "Primo";
                case "PRIMA" -> "Prima";
                case "TUTOR_LEGAL" -> "Tutor Legal";
                case "OTRO" -> "Otro";
                default -> throw new IllegalArgumentException("Unknown code: " + normalizedCode);
            };
            return new TypeGuardian(normalizedCode, defaultDescription);
        });
    }

    // Instancias estáticas predefinidas (familia nuclear)
    public static final TypeGuardian MAMA = TypeGuardian.fromCode("MAMA");
    public static final TypeGuardian PAPA = TypeGuardian.fromCode("PAPA");
    public static final TypeGuardian HERMANO = TypeGuardian.fromCode("HERMANO");
    public static final TypeGuardian HERMANA = TypeGuardian.fromCode("HERMANA");
    public static final TypeGuardian ABUELO = TypeGuardian.fromCode("ABUELO");
    public static final TypeGuardian ABUELA = TypeGuardian.fromCode("ABUELA");
    public static final TypeGuardian TIO = TypeGuardian.fromCode("TIO");
    public static final TypeGuardian TIA = TypeGuardian.fromCode("TIA");
    public static final TypeGuardian PRIMO = TypeGuardian.fromCode("PRIMO");
    public static final TypeGuardian PRIMA = TypeGuardian.fromCode("PRIMA");
    public static final TypeGuardian TUTOR_LEGAL = TypeGuardian.fromCode("TUTOR_LEGAL");
    public static final TypeGuardian OTRO = TypeGuardian.fromCode("OTRO");

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