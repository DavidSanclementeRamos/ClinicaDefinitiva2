
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceStatusTest {

    @Test
    @DisplayName("crear con estado válido")
    void create_valid() {
        ServiceStatus status = ServiceStatus.of(ServiceStatus.State.ACTIVE);

        assertThat(status.getValue()).isEqualTo(ServiceStatus.State.ACTIVE);
        assertThat(status.getDescription()).isEqualTo("Activo");
        assertThat(status.isActive()).isTrue();
        assertThat(status.toString()).contains("ACTIVE").contains("Activo");
    }

    @Test
    @DisplayName("crear con estado nulo -> excepción con catálogo correcto")
    void create_null_throws() {
        assertThatThrownBy(() -> ServiceStatus.of(null))
                .isInstanceOf(ValueObjectValidationException.class)
                .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                        .isEqualTo(ServiceVOError.ERR_SERVICE_STATUS_NULL));
    }

    @Test
    @DisplayName("equals y hashCode funcionan correctamente")
    void equals_and_hashCode() {
        ServiceStatus s1 = ServiceStatus.of(ServiceStatus.State.INACTIVE);
        ServiceStatus s2 = ServiceStatus.of(ServiceStatus.State.INACTIVE);
        ServiceStatus s3 = ServiceStatus.of(ServiceStatus.State.DEPRECATED);

        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
        assertThat(s1).isNotEqualTo(s3);
    }
}
