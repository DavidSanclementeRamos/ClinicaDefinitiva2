package com.example.ClinicaDefinitiva.domain.authentication.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO que encapsula el hash de la contraseña y evita que se use un String crudo en el dominio.
 * Responsabilidad: garantizar que siempre se guarde un hash válido, nunca texto plano.
 * Regla: solo se puede crear con un valor ya hasheado (el hashing lo hace la infraestructura con PasswordEncoder).
 */
public final class HashedPassword implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String hash;

    private HashedPassword(String hash) {
        this.hash = hash;
    }

    /**
     * Fábrica segura: validad y crea un HashedPassword.
     *
     * @param rawHash valor ya hasheado
     * @return Outcome con HashedPassword válido o detalles de error
     */
    public static Outcome<HashedPassword> fromHash(String rawHash) {
        if (rawHash == null) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_USER_PASSWORD_HASH_NULL,
                    Severity.ERROR,
                    Category.TECNICO, VOContext.AUTHENTICATION
            ));
        }

        if (rawHash.isBlank()) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_USER_PASSWORD_HASH_EMPTY,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHENTICATION
            ));
        }

        return Outcome.ok(new HashedPassword(rawHash));
    }

    public String getHash() {
        return hash;
    }

}