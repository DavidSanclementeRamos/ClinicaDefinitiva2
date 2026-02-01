package com.example.ClinicaDefinitiva.domain.actor.valueObject;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar un documento (cédula).
 * Inmutable y con validación en el constructor.
 */
public final class Document {

    private static final Pattern DOCUMENT_PATTERN = Pattern.compile("^\\d{6,10}$");

    private final String value;


    public Document(String raw) {
        if (raw == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_DOCUMENT_NULL, VOContext.DOCUMENT_ID);
        }
        String normalized = raw.trim();
        if (normalized.isBlank()) {
            throw new ValueObjectValidationException(VoActorError.ERR_DOCUMENT_BLANK, VOContext.DOCUMENT_ID);
        }
        if (!DOCUMENT_PATTERN.matcher(normalized).matches()) {
            throw new ValueObjectValidationException(VoActorError.ERR_DOCUMENT_INVALID_FORMAT, VOContext.DOCUMENT_ID);
        }
        this.value = normalized;
    }


    public static Document of(String raw) {
        return new Document(raw);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return value.equals(document.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
