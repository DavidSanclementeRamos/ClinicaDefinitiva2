
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceIdTest {

    @Test
    @DisplayName("crear ServiceId válido")
    void create_valid() {
        ServiceId id = ServiceId.of(123L);

        assertThat(id).isNotNull();
        assertThat(id.getId()).isEqualTo(123L);
    }

    @Test
    @DisplayName("crear ServiceId con null -> excepción")
    void create_null_throws() {
        assertThatThrownBy(() -> ServiceId.of(null))
                .isInstanceOf(ValueObjectValidationException.class)
                .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                        .isEqualTo(ServiceVOError.ERR_SERVICE_ID_NULL));
    }

    @Test
    @DisplayName("equals y hashCode funcionan correctamente")
    void equals_and_hashCode() {
        ServiceId id1 = ServiceId.of(10L);
        ServiceId id2 = ServiceId.of(10L);
        ServiceId id3 = ServiceId.of(20L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        assertThat(id1).isNotEqualTo(id3);
    }

    @Test
    @DisplayName("toString incluye valor del id")
    void toString_includesId() {
        ServiceId id = ServiceId.of(99L);
        assertThat(id.toString()).contains("99");
    }
}
