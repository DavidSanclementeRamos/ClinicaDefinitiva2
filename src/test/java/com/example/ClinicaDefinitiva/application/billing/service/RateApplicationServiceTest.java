package com.example.ClinicaDefinitiva.application.billing.service;

import com.example.ClinicaDefinitiva.application.billing.dto.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.application.billing.dto.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.billing.dto.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.application.billing.mapper.rate.RateReadMapper;
import com.example.ClinicaDefinitiva.application.billing.mapper.rate.RateWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.billing.RateNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.enu.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateApplicationServiceTest {

    @Mock
    private RateRepository rateRepository;
    @Mock
    private RateReadMapper readMapper;
    @Mock
    private RateWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private RateApplicationService rateApplicationService;

    private final RateId rateId = RateId.of(1L);
    // Usamos un mock para Rate en los tests que necesitan verify
    private final Rate rateMock = mock(Rate.class);
    private final ReadRateDto readRateDto = new ReadRateDto(1L, 10L, "EPS", 5L, BigDecimal.valueOf(100000), "COP", null, null, true, true);
    private final PageRateDto pageRateDto = new PageRateDto(1L, 10L, "EPS", BigDecimal.valueOf(100000), "COP", null, null, true);

    @Test
    @DisplayName("findById: tarifa existente retorna DTO")
    void findById_whenExists_shouldReturnDto() {
        when(rateRepository.findById(rateId)).thenReturn(Optional.of(rateMock));
        when(readMapper.toDto(rateMock)).thenReturn(readRateDto);

        ReadRateDto result = rateApplicationService.findById(rateId, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(readRateDto);
        verify(authorizationHelper).authorize(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("findById: tarifa no existente lanza excepción")
    void findById_whenNotExists_shouldThrow() {
        when(rateRepository.findById(rateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rateApplicationService.findById(rateId, mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(RateNotFoundException.class)
                .hasMessageContaining("No found");
    }

    @Test
    @DisplayName("findAll: retorna página de DTOs")
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rate> ratePage = new PageImpl<>(List.of(rateMock), pageable, 1);
        when(rateRepository.findAll(pageable)).thenReturn(ratePage);
        when(readMapper.toPageDto(rateMock)).thenReturn(pageRateDto);

        Page<PageRateDto> result = rateApplicationService.findAll(pageable, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).containsExactly(pageRateDto);
    }

    @Test
    @DisplayName("findByService: retorna página filtrada")
    void findByService_shouldReturnPage() {
        ServiceId serviceId = ServiceId.of(10L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rate> ratePage = new PageImpl<>(List.of(rateMock), pageable, 1);
        when(rateRepository.findByService(serviceId, pageable)).thenReturn(ratePage);
        when(readMapper.toPageDto(rateMock)).thenReturn(pageRateDto);

        Page<PageRateDto> result = rateApplicationService.findByService(serviceId, pageable, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result.getContent()).hasSize(1);
        verify(rateRepository).findByService(serviceId, pageable);
    }

    @Test
    @DisplayName("create: crea y retorna DTO")
    void create_shouldSaveAndReturnDto() {
        CreateRateDto dto = new CreateRateDto(10L, "EPS", 5L, BigDecimal.valueOf(100000), "COP");
        Rate newRate = Rate.builder().build(); // objeto real, pero no se verifica
        when(writeMapper.toServiceId(dto)).thenReturn(ServiceId.of(10L));
        when(writeMapper.toAmount(dto)).thenReturn(Price.of(100000, Currency.getInstance("COP")));
        when(writeMapper.toPayerType(dto)).thenReturn(PayerType.EPS);
        when(writeMapper.toContractId(dto)).thenReturn(ContractId.of(5L));
        when(rateRepository.save(any(Rate.class))).thenReturn(rateMock);
        when(readMapper.toDto(rateMock)).thenReturn(readRateDto);

        ReadRateDto result = rateApplicationService.create(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(readRateDto);
        verify(rateRepository).save(any(Rate.class));
    }

    @Test
    @DisplayName("endValidityAt: finaliza vigencia y guarda")
    void endValidityAt_shouldUpdate() {
        LocalDateTime endDate = LocalDateTime.now().plusMonths(6);
        when(rateRepository.findById(rateId)).thenReturn(Optional.of(rateMock));
        when(rateRepository.save(rateMock)).thenReturn(rateMock);
        when(readMapper.toDto(rateMock)).thenReturn(readRateDto);

        ReadRateDto result = rateApplicationService.endValidityAt(rateId, endDate, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(readRateDto);
        verify(rateMock).endValidityAt(endDate);
        verify(rateRepository).save(rateMock);
    }

    @Test
    @DisplayName("deactivate: desactiva tarifa")
    void deactivate_shouldCallDeactivate() {
        when(rateRepository.findById(rateId)).thenReturn(Optional.of(rateMock));
        rateApplicationService.deactivate(rateId, mock(UserIdentityId.class), mock(RolId.class));

        verify(rateMock).deactivate();
        verify(rateRepository).save(rateMock);
    }

    @Test
    @DisplayName("markAsReplaced: marca tarifa como reemplazada")
    void markAsReplaced_shouldCallMarkAsReplaced() {
        when(rateRepository.findById(rateId)).thenReturn(Optional.of(rateMock));
        rateApplicationService.markAsReplaced(rateId, mock(UserIdentityId.class), mock(RolId.class));

        verify(rateMock).markAsReplaced();
        verify(rateRepository).save(rateMock);
    }
}
