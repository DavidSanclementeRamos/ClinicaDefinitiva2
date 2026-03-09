
package com.example.ClinicaDefinitiva.domain.dentalService;

import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.model.AestheticDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ImplantologyDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.model.OrthodonticDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.model.PediatricDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProstheticDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.model.SurgicalDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetailsFactory;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.AgeRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ServiceDetailsFactoryTest {

    @Nested
    @DisplayName("Creación directa")
    class DirectCreationTests {

        @Test
        @DisplayName("crear detalles de ortodoncia")
        void createOrthodontic_valid() {
            ServiceDetails details = ServiceDetailsFactory.createOrthodontic("METAL_BRACKETS", 24, true);
            assertThat(details).isInstanceOf(OrthodonticDetails.class);
            assertThat(details.serviceType()).isEqualTo(ServiceType.ORTHODONTIC);
        }

        @Test
        @DisplayName("crear detalles de cirugía")
        void createSurgical_valid() {
            ServiceDetails details = ServiceDetailsFactory.createSurgical("Extraction", "MEDIUM", true, false);
            assertThat(details).isInstanceOf(SurgicalDetails.class);
            assertThat(details.serviceType()).isEqualTo(ServiceType.SURGERY);
        }

        @Test
        @DisplayName("crear detalles de estética")
        void createAesthetic_valid() {
            ServiceDetails details = ServiceDetailsFactory.createAesthetic("Whitening", "Porcelain", "Bright smile");
            assertThat(details).isInstanceOf(AestheticDetails.class);
            assertThat(details.serviceType()).isEqualTo(ServiceType.AESTHETICS);
        }

        @Test
        @DisplayName("crear detalles de implantología")
        void createImplantology_valid() {
            ServiceDetails details = ServiceDetailsFactory.createImplantology(6, "Titanium", "Upper jaw", true);
            assertThat(details).isInstanceOf(ImplantologyDetails.class);
            assertThat(details.serviceType()).isEqualTo(ServiceType.IMPLANTOLOGY);
        }

        @Test
        @DisplayName("crear detalles pediátricos")
        void createPediatric_valid() {
            ServiceDetails details = ServiceDetailsFactory.createPediatric(AgeRange.of(6, 12), "Positive reinforcement", "Resin");
            assertThat(details).isInstanceOf(PediatricDetails.class);
            assertThat(details.serviceType()).isEqualTo(ServiceType.PEDIATRICS);
        }

        @Test
        @DisplayName("crear detalles protésicos")
        void createProsthetic_valid() {
            ServiceDetails details = ServiceDetailsFactory.createProsthetic("Fixed", "Porcelain", "Crown", 2);
            assertThat(details).isInstanceOf(ProstheticDetails.class);
            assertThat(details.serviceType()).isEqualTo(ServiceType.PROSTHETICS);
        }
    }

    @Nested
    @DisplayName("Creación desde Map")
    class FromMapTests {

        @Test
        @DisplayName("crear ortodoncia desde mapa")
        void fromMap_orthodontic() {
            Map<String, Object> fields = Map.of(
                    "applianceType", "CLEAR_ALIGNERS",
                    "treatmentDurationMonths", 18,
                    "requiresFollowup", true
            );
            ServiceDetails details = ServiceDetailsFactory.fromMap(ServiceType.ORTHODONTIC, fields);
            assertThat(details).isInstanceOf(OrthodonticDetails.class);
        }

        @Test
        @DisplayName("crear cirugía desde mapa")
        void fromMap_surgery() {
            Map<String, Object> fields = Map.of(
                    "surgeryType", "Extraction",
                    "complexityLevel", "HIGH",
                    "requiresAnesthesia", true,
                    "operatingRoomNeeded", true
            );
            ServiceDetails details = ServiceDetailsFactory.fromMap(ServiceType.SURGERY, fields);
            assertThat(details).isInstanceOf(SurgicalDetails.class);
        }

        @Test
        @DisplayName("tipo desconocido -> excepción")
        void fromMap_unknownType_throws() {
            Map<String, Object> fields = Map.of("dummy", "value");
            assertThatThrownBy(() -> ServiceDetailsFactory.fromMap(ServiceType.valueOf("UNKNOWN"), fields))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("tipo de campo incorrecto -> ClassCastException")
        void fromMap_wrongFieldType_throws() {
            Map<String, Object> fields = Map.of(
                    "applianceType", 123, // debería ser String
                    "treatmentDurationMonths", 24,
                    "requiresFollowup", true
            );
            assertThatThrownBy(() -> ServiceDetailsFactory.fromMap(ServiceType.ORTHODONTIC, fields))
                    .isInstanceOf(ClassCastException.class);
        }
    }
}

