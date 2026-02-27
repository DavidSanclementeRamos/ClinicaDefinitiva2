
package com.example.ClinicaDefinitiva.domain.dentalService.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.ClinicaDefinitiva.domain.dental.care.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProstheticDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.SurgicalDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ProvidedServiceError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.util.Currency;


import static org.assertj.core.api.Assertions.*;

class ProvidedServiceTest {

    private ProvidedService createActiveService() {
        ServiceCatalog catalog = ServiceCatalog.Defaults.SURG_WISDOM_EXTRACTION.get(); 
        SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
        return ProvidedService.create(
                 ServiceId.of(4L),
                 ServiceName.custom("Cirugía dental"),
                 catalog,
                 ServiceCode.of("C001"),
                Price.of(200, Currency.getInstance("COP")),
                 ServiceDuration.of(60),
                 ServiceDescription.of("Extracción de muela"),
                details,
                true
        );
    }

    @Nested
    @DisplayName("Creación")
    class CreationTests {
        @Test
        @DisplayName("crear servicio activo válido")
        void create_validService() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            assertThat(service.isActive()).isTrue();
            assertThat(service.getDetails()).isPresent();
            assertThat(service.canBeScheduled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Update Information")
    class UpdateInformationTests {
        @Test
        @DisplayName("actualizar nombre y descripción en servicio activo")
        void updateInformation_valid() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            service.updateInformation(
                    ServiceName.custom("Cirugía avanzada"),
                    null,
                    null,
                    null,
                     ServiceDescription.of("Nueva descripción")
            );

            assertThat(service.getName().getValue()).isEqualTo("Cirugía avanzada");
            assertThat(service.getDescription().getValue()).isEqualTo("Nueva descripción");
        }

        @Test
        @DisplayName("actualizar categoría inconsistente con detalles -> excepción")
        void updateInformation_categoryMismatch_throws() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            assertThatThrownBy(() -> service.updateInformation(
                    null,
                     ServiceCatalog.of(
                               ServiceId.of(1L),
                    ServiceName.custom("Laser Whitening"),
                    "Aesthetics"),
                    null,
                    null,
                    null
            )).isInstanceOf(BusinessRuleViolationException.class)
              .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                      .isEqualTo(ProvidedServiceError.ERR_SERVICE_CATEGORY_MISMATCH));
        }
    }

    @Nested
    @DisplayName("Update Rate")
    class UpdateRateTests {
        @Test
        @DisplayName("cambiar tarifa sin justificación -> excepción")
        void updateRate_withoutJustification_throws() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            assertThatThrownBy(() -> service.updateRate(Price.of(200, Currency.getInstance("COP")), ""))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(ProvidedServiceError.ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION));
        }
    }

    @Nested
    @DisplayName("Update Details")
    class UpdateDetailsTests {
        @Test
        @DisplayName("cambiar tipo de servicio -> excepción")
        void updateDetails_serviceTypeImmutable_throws() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            ProstheticDetails newDetails = new ProstheticDetails(
                    "FIXED",
                    "Porcelain",
                    "Crown",
                    3
            );

            assertThatThrownBy(() -> service.updateDetails(newDetails))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(ProvidedServiceError.ERR_SERVICE_TYPE_IMMUTABLE));
        }
    }

    @Nested
    @DisplayName("Deactivate / Reactivate")
    class DeactivationTests {
        @Test
        @DisplayName("desactivar sin motivo suficiente -> excepción")
        void deactivate_withoutReason_throws() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            assertThatThrownBy(() -> service.deactivate("Muy corto"))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(ProvidedServiceError.ERR_SERVICE_DEACTIVATION_REASON_REQUIRED));
        }

        @Test
        @DisplayName("desactivar y reactivar servicio")
        void deactivate_and_reactivate() {
            SurgicalDetails details = new SurgicalDetails("Extraction", "MEDIUM", true, false);
            ProvidedService service = createActiveService();

            service.deactivate("Motivo válido para desactivación");
            assertThat(service.isActive()).isFalse();

            service.reactivate();
            assertThat(service.isActive()).isTrue();
        }
    }
}

