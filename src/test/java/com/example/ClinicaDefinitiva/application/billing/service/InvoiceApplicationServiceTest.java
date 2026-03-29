package com.example.ClinicaDefinitiva.application.billing.service;

import com.example.ClinicaDefinitiva.application.billing.dto.invoice.*;
import com.example.ClinicaDefinitiva.application.billing.mapper.invoice.InvoiceReadMapper;
import com.example.ClinicaDefinitiva.application.billing.mapper.invoice.InvoiceWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.billing.InvoiceNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceDomainService;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.domain.billing.vo.ProviderId;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
class InvoiceApplicationServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceDomainService domainService;
    @Mock
    private InvoiceReadMapper readMapper;
    @Mock
    private InvoiceWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;
    @Mock
    private InvoiceNumberGenerator invoiceNumberGenerator;

    @InjectMocks
    private InvoiceApplicationService service;

    @Test
    @DisplayName("Crear factura particular exitosamente")
    void createParticular_shouldReturnDto() {
        CreateParticularInvoiceDto dto = new CreateParticularInvoiceDto(
                1L, 2L, 3L, "COP", LocalDateTime.now().plusDays(30), "Notas"
        );
        Invoice invoiceMock = mock(Invoice.class);
        Invoice savedMock = mock(Invoice.class);
        ReadInvoiceDto resultDto = new ReadInvoiceDto(1L,"FAC-0001",1L,1L,1L,1L,"PENDING","COP",BigDecimal.ZERO,           
            BigDecimal.ZERO,           // tax
            BigDecimal.ZERO,           // total
            LocalDateTime.now().plusDays(30), // dueDate
            LocalDateTime.now(),       // createdAt
            LocalDateTime.now(),       // updatedAt
            "",                        // notes
            List.of() );

        when(writeMapper.toPatientId(dto)).thenReturn(PatientId.of(1L));
        when(writeMapper.toProviderId(dto)).thenReturn(ProviderId.of(2L));
        when(writeMapper.toDentistId(dto)).thenReturn(DentistId.of(3L));
        when(writeMapper.toCurrency(dto)).thenReturn(CurrencyCode.of("COP"));
        when(writeMapper.toNotes(dto)).thenReturn(Notes.of("Notas"));
        when(writeMapper.toDueDate(dto)).thenReturn(dto.dueDate());

        // Usamos spy para simular la creación estática (pero Invoice.createParticular no es mockeable fácilmente)
        // En su lugar, podríamos usar un builder o un factory. Para el test, puedes crear una instancia real.
        // Como es unitario, podemos usar un objeto real con datos mínimos.
        Invoice realInvoice = Invoice.createParticular(
                PatientId.of(1L), ProviderId.of(2L), DentistId.of(3L),
                CurrencyCode.of("COP"), Notes.of("Notas"), dto.dueDate()
        );
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(realInvoice);
        when(readMapper.toDto(any(Invoice.class))).thenReturn(resultDto);

        ReadInvoiceDto result = service.createParticular(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(resultDto);
        verify(domainService).validateRates(any(), any());
        verify(invoiceRepository).save(any());
    }

    @Test
    @DisplayName("Emitir factura existente")
    void emit_shouldCallDomainAndSave() {
        InvoiceId id = InvoiceId.of(1L);
        Invoice invoice = mock(Invoice.class);
        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        ReadInvoiceDto dto = mock(ReadInvoiceDto.class);
        when(readMapper.toDto(invoice)).thenReturn(dto);

        ReadInvoiceDto result = service.emit(id, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(dto);
        verify(invoice).emit(any());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    @DisplayName("Emitir factura inexistente lanza excepción")
    void emit_invoiceNotFound_shouldThrow() {
        InvoiceId id = InvoiceId.of(999L);
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.emit(id, mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(InvoiceNotFoundException.class);
    }
}