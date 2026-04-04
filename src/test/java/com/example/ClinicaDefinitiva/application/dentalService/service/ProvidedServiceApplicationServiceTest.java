package com.example.ClinicaDefinitiva.application.dentalService.service;

import com.example.ClinicaDefinitiva.application.dentalService.dto.*;
import com.example.ClinicaDefinitiva.application.dentalService.mapper.ProvidedServiceReadMapper;
import com.example.ClinicaDefinitiva.application.dentalService.mapper.ProvidedServiceWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.dentalService.ProvidedServiceNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceRatePolicy;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCatalog;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCode;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDescription;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceName;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.util.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProvidedServiceApplicationServiceTest {

    @Mock
    private ProvidedServiceRepository serviceRepository;
    @Mock
    private ProvidedServiceReadMapper readMapper;
    @Mock
    private ProvidedServiceWriteMapper writeMapper;
    @Mock
    private ServiceRatePolicy ratePolicy;
    @Mock
    private AuthorizationHelper authorizationHelper;
    @Mock
    private ServiceDeactivationValidator deactivationValidator;

    @InjectMocks
    private ProvidedServiceApplicationService service;

    // ... más pruebas

    @Test
    @DisplayName("Crear servicio exitosamente")
    void create_shouldReturnDto() {
        CreateServiceDto dto = mock(CreateServiceDto.class);
        ProvidedService serviceEntity = mock(ProvidedService.class);
        ProvidedService saved = mock(ProvidedService.class);
        ReadServiceDto resultDto = mock(ReadServiceDto.class);

        when(writeMapper.toServiceName(dto)).thenReturn(ServiceName.custom("Limpieza"));
        when(writeMapper.toServiceCategory(dto)).thenReturn(ServiceCatalog.of(ServiceId.of(1L), ServiceName.custom("Limpieza"), "General"));
        when(writeMapper.toServiceCode(dto)).thenReturn(ServiceCode.of("GEN-001"));
        when(writeMapper.toBaseRate(dto)).thenReturn(Price.of(50_000, Currency.getInstance("COP")));
        when(writeMapper.toDuration(dto)).thenReturn(ServiceDuration.of(30));
        when(writeMapper.toDescription(dto)).thenReturn(ServiceDescription.of("Limpieza dental"));
        when(writeMapper.toRequiresAuthorization(dto)).thenReturn(false);
        when(serviceRepository.save(any(ProvidedService.class))).thenReturn(saved);
        when(readMapper.toReadDto(saved)).thenReturn(resultDto);

        ReadServiceDto result = service.create(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(resultDto);
        verify(serviceRepository).save(any());
    }

  @Test
@DisplayName("Actualizar tarifa con validación de política")
void updateRate_shouldValidateWithPolicy() {
    ServiceId id = ServiceId.of(1L);
    UpdateServiceRateDto dto = mock(UpdateServiceRateDto.class);
    ProvidedService serviceEntity = mock(ProvidedService.class);
    when(serviceRepository.findById(id)).thenReturn(Optional.of(serviceEntity));
    when(writeMapper.toRate(dto)).thenReturn(Price.of(120_000, Currency.getInstance("COP")));
    when(serviceEntity.getBaseRate()).thenReturn(Price.of(100_000, Currency.getInstance("COP")));


    service.updateRate(dto, id, mock(UserIdentityId.class), mock(RolId.class));

    verify(serviceEntity).updateRate(any(), any());
    verify(serviceRepository).save(serviceEntity);
}

    @Test
    @DisplayName("Desactivar servicio valida ausencia de citas/facturas")
    void deactivate_shouldValidateNoAppointments() {
        ServiceId id = ServiceId.of(1L);
        ProvidedService serviceEntity = mock(ProvidedService.class);
        when(serviceRepository.findById(id)).thenReturn(Optional.of(serviceEntity));
        doNothing().when(deactivationValidator).validateNoAppointments(id);

        service.deactivate(id, "Razón válida con más de diez caracteres", mock(UserIdentityId.class), mock(RolId.class));

        verify(serviceEntity).deactivate(any());
        verify(serviceRepository).save(serviceEntity);
    }

    @Test
    @DisplayName("Buscar servicio inexistente lanza excepción")
    void findById_notFound_shouldThrow() {
        ServiceId id = ServiceId.of(999L);
        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id, mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(ProvidedServiceNotFoundException.class);
    }
}
