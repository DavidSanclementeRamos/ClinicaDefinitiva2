package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.*;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class ProvidedServiceTest {

    private static final Currency COP = Currency.getInstance("COP");
    private Price initialRate;
    private ServiceDetails orthoDetails;
    private ServiceName serviceName;
    private ServiceCatalog category;
    private ServiceCode code;
    private ServiceDuration duration;
    private ServiceDescription description;

    @BeforeEach
    void setUp() {
        initialRate = Price.of(100_000, COP);
        orthoDetails = new OrthodonticDetails("METAL_BRACKETS", 24, true);
        serviceName = ServiceName.custom("Ortodoncia Metálica");
        category = ServiceCatalog.of(ServiceId.of(1L), serviceName, "ORTHODONTIC");
        code = ServiceCode.of("ORT-001");
        duration = ServiceDuration.of(60);
        description = ServiceDescription.of("Tratamiento de ortodoncia con brackets metálicos");
    }

    @Test
    @DisplayName("SER-UNIT-001: Crear servicio activo")
    void create_shouldBeActive() {
        ProvidedService service = ProvidedService.create(
                serviceName, category, code, initialRate, duration, description,
                orthoDetails, true
        );
        assertThat(service.isActive()).isTrue();
        assertThat(service.getCode().getValue()).isEqualTo("ORT-001");
        assertThat(service.getDetails()).contains(orthoDetails);
    }

    @Test
    @DisplayName("SER-UNIT-002: Actualizar información común")
    void updateInformation_shouldUpdateFields() {
        ProvidedService service = createActiveService();
        ServiceName newName = ServiceName.custom("Ortodoncia Avanzada");
        ServiceDuration newDuration = ServiceDuration.of(90);
        ServiceDescription newDesc = ServiceDescription.of("Nueva descripción");

        service.updateInformation(newName, null, newDuration, false, newDesc);

        assertThat(service.getName()).isEqualTo(newName);
        assertThat(service.getDuration()).isEqualTo(newDuration);
        assertThat(service.getDescription()).isEqualTo(newDesc);
        assertThat(service.isRequiresAuthorization()).isFalse();
    }

    @Test
    @DisplayName("RN-SERVICE-003: Servicio inactivo no puede ser editado")
    void inactiveService_shouldNotBeEditable() {
        ProvidedService service = createActiveService();
        service.deactivate("Razón válida con más de diez caracteres");

        assertThatThrownBy(() -> service.updateInformation(null, null, null, null, null))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("No se puede operar sobre un servicio inactivo");
    }

    @Test
    @DisplayName("RN-SERVICE-008: Cambio de tarifa requiere justificación")
    void updateRate_requiresJustification() {
        ProvidedService service = createActiveService();
        Price newRate = Price.of(120_000, COP);

        assertThatThrownBy(() -> service.updateRate(newRate, null))
                .isInstanceOf(BusinessRuleViolationException.class);
        assertThatThrownBy(() -> service.updateRate(newRate, " "))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("RN-SERVICE-011: Cambio de tarifa dentro del rango razonable")
    void updateRate_withinRange_shouldSucceed() {
        ProvidedService service = createActiveService();
        Price newRate = Price.of(110_000, COP); // 10% aumento
        service.updateRate(newRate, "Ajuste por inflación");
        assertThat(service.getBaseRate()).isEqualTo(newRate);
    }

    @Test
    @DisplayName("RN-SERVICE-011: Cambio de tarifa fuera del rango lanza excepción")
    void updateRate_outOfRange_shouldThrow() {
        ProvidedService service = createActiveService();
        Price tooLow = Price.of(70_000, COP);  // 30% menor
        Price tooHigh = Price.of(150_000, COP); // 50% mayor

        assertThatThrownBy(() -> service.updateRate(tooLow, "Justificación"))
                .isInstanceOf(BusinessRuleViolationException.class);
        assertThatThrownBy(() -> service.updateRate(tooHigh, "Justificación"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("RN-SERVICE-006: No se puede cambiar el tipo de detalles")
    void updateDetails_withDifferentType_shouldThrow() {
        ProvidedService service = createActiveService();
        ServiceDetails newDetails = new SurgicalDetails("Extracción", "LOW", false, false);

        assertThatThrownBy(() -> service.updateDetails(newDetails))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("RN-SERVICE-004: Los detalles deben coincidir con la categoría")
    void updateDetails_categoryMismatch_shouldThrow() {
        ProvidedService service = createActiveService();
        // Intentar cambiar a detalles de ortodoncia pero categoría sigue siendo Orthodontics? No, pero la categoría actual es Orthodontics.
        // Si cambiamos a detalles de ortodoncia está bien. Pero si cambiamos a cirugía, debe fallar porque la categoría no coincide.
        // El método validateCategoryMatch está en el constructor y en updateDetails.
        // Ya que el servicio fue creado con categoría Orthodontics y detalles Orthodontic, cambiar a Surgical debe fallar.
        ServiceDetails surgical = new SurgicalDetails("Extracción", "LOW", false, false);
        assertThatThrownBy(() -> service.updateDetails(surgical))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("RN-SERVICE-015: Desactivación requiere motivo detallado (mínimo 10 caracteres)")
    void deactivate_requiresLongReason() {
        ProvidedService service = createActiveService();
        assertThatThrownBy(() -> service.deactivate("Corto"))
                .isInstanceOf(BusinessRuleViolationException.class);
        service.deactivate("Razón válida con más de diez caracteres");
        assertThat(service.isActive()).isFalse();
    }

    @Test
    @DisplayName("RN-SERVICE-015: Desactivar servicio activo")
    void deactivate_shouldChangeStatus() {
        ProvidedService service = createActiveService();
        service.deactivate("Razón válida con más de diez caracteres");
        assertThat(service.isActive()).isFalse();
        assertThat(service.getStatus().getValue()).isEqualTo(ServiceStatus.State.INACTIVE);
    }

    @Test
    @DisplayName("Reactivar servicio")
    void reactivate_shouldMakeActive() {
        ProvidedService service = createActiveService();
        service.deactivate("Razón válida");
        service.reactivate();
        assertThat(service.isActive()).isTrue();
    }

    private ProvidedService createActiveService() {
        return ProvidedService.create(
                serviceName, category, code, initialRate, duration, description,
                orthoDetails, true
        );
    }
}
