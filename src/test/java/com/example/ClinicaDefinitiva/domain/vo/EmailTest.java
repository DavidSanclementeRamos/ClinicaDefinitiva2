
package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Nested
    @DisplayName("Creación válida")
    class ValidCreationTests {

        @Test
        @DisplayName("email válido devuelve Outcome.ok con valor normalizado")
        void validEmail_ok() {
            Outcome<Email> outcome = Email.of("User@Example.COM");

            assertThat(outcome.isSuccess()).isTrue();
            assertThat(outcome.getValue()).isPresent();
            assertThat(outcome.getValue().get().value()).isEqualTo("User@example.com");
        }
    }

    @Nested
    @DisplayName("Validaciones de nulos y vacíos")
    class NullEmptyTests {

        @Test
        @DisplayName("email nulo devuelve Outcome.fail con ERR_EMAIL_NULL")
        void nullEmail_fail() {
            Outcome<Email> outcome = Email.of(null);

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_NULL);
        }

        @Test
        @DisplayName("email vacío devuelve Outcome.fail con ERR_EMAIL_EMPTY")
        void emptyEmail_fail() {
            Outcome<Email> outcome = Email.of("   ");

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_EMPTY);
        }
    }

    @Nested
    @DisplayName("Validaciones de formato")
    class FormatTests {

        @Test
        @DisplayName("email sin @ devuelve ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN")
        void missingAt_fail() {
            Outcome<Email> outcome = Email.of("invalidEmail.com");

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN);
        }

        @Test
        @DisplayName("email con formato inválido devuelve ERR_EMAIL_INVALID_FORMAT")
        void invalidFormat_fail() {
            Outcome<Email> outcome = Email.of("user@@example.com");

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_INVALID_FORMAT);
        }
    }

    @Nested
    @DisplayName("Validaciones de longitud")
    class LengthTests {

        @Test
        @DisplayName("email demasiado largo devuelve ERR_EMAIL_LENGTH_EXCEEDED")
        void lengthExceeded_fail() {
            String local = "a".repeat(64);
            String domain = "a".repeat(240) + ".com";
            String longEmail = local + "@" + domain;

            Outcome<Email> outcome = Email.of(longEmail);

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_LENGTH_EXCEEDED);
        }

        @Test
        @DisplayName("parte local demasiado larga devuelve ERR_EMAIL_LOCAL_LENGTH_EXCEEDED")
        void localLengthExceeded_fail() {
            String local = "a".repeat(65);
            String email = local + "@example.com";

            Outcome<Email> outcome = Email.of(email);

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_LOCAL_LENGTH_EXCEEDED);
        }

        @Test
        @DisplayName("parte dominio demasiado larga devuelve ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED")
        void domainLengthExceeded_fail() {
            String domain = "a".repeat(254);
            String email = "user@" + domain;

            Outcome<Email> outcome = Email.of(email);

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED);
        }
    }

    @Nested
    @DisplayName("Validaciones adicionales de dominio")
    class DomainTests {

        @Test
        @DisplayName("dominio empieza con guion devuelve ERR_EMAIL_DOMAIN_INVALID_DASH")
        void domainStartsWithDash_fail() {
            Outcome<Email> outcome = Email.of("user@-example.com");

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_DOMAIN_INVALID_DASH);
        }

        @Test
        @DisplayName("dominio contiene '..' devuelve ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS")
        void domainConsecutiveDots_fail() {
            Outcome<Email> outcome = Email.of("user@exa..mple.com");

            assertThat(outcome.isFailure()).isTrue();
            assertThat(outcome.getDetalles().get(0).getCode())
                    .isEqualTo(VoAccesError.ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS);
        }
    }
}