package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.model.*;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.AgeRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ServiceDetailsFactoryTest {

    @Test
    @DisplayName("Crear detalles de ortodoncia")
    void createOrthodontic() {
        ServiceDetails details = ServiceDetailsFactory.createOrthodontic(
                "METAL_BRACKETS", 24, true
        );
        assertThat(details).isInstanceOf(OrthodonticDetails.class);
        assertThat(details.serviceType()).isEqualTo(ServiceType.ORTHODONTIC);
    }

    @Test
    @DisplayName("Crear detalles de cirugía")
    void createSurgical() {
        ServiceDetails details = ServiceDetailsFactory.createSurgical(
                "Extracción", "LOW", false, false
        );
        assertThat(details).isInstanceOf(SurgicalDetails.class);
    }

    @Test
    @DisplayName("Crear detalles estéticos")
    void createAesthetic() {
        ServiceDetails details = ServiceDetailsFactory.createAesthetic(
                "WHITENING", "Peróxido", "Dientes más blancos"
        );
        assertThat(details).isInstanceOf(AestheticDetails.class);
    }

    @Test
    @DisplayName("Crear detalles de implantología")
    void createImplantology() {
        ServiceDetails details = ServiceDetailsFactory.createImplantology(
                6, "Titanio", "Maxilar", false
        );
        assertThat(details).isInstanceOf(ImplantologyDetails.class);
    }

    @Test
    @DisplayName("Crear detalles pediátricos")
    void createPediatric() {
        AgeRange range = AgeRange.of(3, 12);
        ServiceDetails details = ServiceDetailsFactory.createPediatric(
                range, "Hablar", "Materiales coloridos"
        );
        assertThat(details).isInstanceOf(PediatricDetails.class);
    }

    @Test
    @DisplayName("Crear detalles protésicos")
    void createProsthetic() {
        ServiceDetails details = ServiceDetailsFactory.createProsthetic(
                "FIXED", "Cerámica", "Corona", 1
        );
        assertThat(details).isInstanceOf(ProstheticDetails.class);
    }

    @Test
    @DisplayName("Crear desde mapa para ortodoncia")
    void fromMap_orthodontic() {
        Map<String, Object> fields = Map.of(
                "applianceType", "METAL_BRACKETS",
                "treatmentDurationMonths", 24,
                "requiresFollowup", true
        );
        ServiceDetails details = ServiceDetailsFactory.fromMap(ServiceType.ORTHODONTIC, fields);
        assertThat(details).isInstanceOf(OrthodonticDetails.class);
    }

    @Test
    @DisplayName("Crear desde mapa para cirugía")
    void fromMap_surgical() {
        Map<String, Object> fields = Map.of(
                "surgeryType", "Extracción",
                "complexityLevel", "LOW",
                "requiresAnesthesia", false,
                "operatingRoomNeeded", false
        );
        ServiceDetails details = ServiceDetailsFactory.fromMap(ServiceType.SURGERY, fields);
        assertThat(details).isInstanceOf(SurgicalDetails.class);
    }
}
