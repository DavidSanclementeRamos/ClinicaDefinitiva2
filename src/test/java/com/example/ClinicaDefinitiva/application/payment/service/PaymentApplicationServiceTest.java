package com.example.ClinicaDefinitiva.application.payment.service;

import com.example.ClinicaDefinitiva.application.payment.dto.CreatePaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PayerRequestDto;
import com.example.ClinicaDefinitiva.application.payment.dto.RefundPaymentDto;
import com.example.ClinicaDefinitiva.application.payment.mapper.PaymentAppWriteMapper;
import com.example.ClinicaDefinitiva.application.payment.mapper.PaymentReadMapper;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.service.PaymentProcessingService;
import com.example.ClinicaDefinitiva.domain.payment.service.PaymentRefundService;
import com.example.ClinicaDefinitiva.domain.payment.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentApplicationServiceTest {

    @Mock
    private PaymentProcessingService paymentProcessingService;
    @Mock
    private PaymentRefundService paymentRefundService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentReadMapper readMapper;
    @Mock
    private PaymentAppWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private PaymentApplicationService service;

    private static final Currency COP = Currency.getInstance("COP");

    @Test
    @DisplayName("PAY-APP-001: Procesar pago exitoso")
    void processPayment_shouldReturnDto() {
        CreatePaymentDto dto = new CreatePaymentDto(1L, BigDecimal.valueOf( 100000.0), "COP", "CASH",
                "email@test.com", "Juan Pérez", new PayerRequestDto("PATIENT", null, "Juan Pérez"));
        Payment payment = mock(Payment.class);
        PaymentDto expectedDto = mock(PaymentDto.class);

        when(writeMapper.toInvoiceId(dto)).thenReturn(InvoiceId.of(1L));
        when(writeMapper.toAmount(dto)).thenReturn(Price.of(100000, COP));
        when(writeMapper.toPaymentMethod(dto)).thenReturn(PaymentMethod.CASH);
        when(writeMapper.toPayer(dto)).thenReturn(Payer.patient("Juan Pérez"));
        when(paymentProcessingService.processPayment(any(), any(), any(), any(), any(), any()))
                .thenReturn(payment);
        when(readMapper.toDto(payment)).thenReturn(expectedDto);

        PaymentDto result = service.processPayment(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(expectedDto);
    }

    @Test
    @DisplayName("PAY-APP-002: Reembolsar pago exitoso")
    void refundPayment_shouldReturnDto() {
        RefundPaymentDto dto = new RefundPaymentDto(1L,  BigDecimal.valueOf(50000.0), "COP", "Razón válida");
        Payment payment = mock(Payment.class);
        PaymentDto expectedDto = mock(PaymentDto.class);

        when(writeMapper.toAmount(dto)).thenReturn(Price.of(50000, COP));
        when(paymentRefundService.refundPayment(any(), any(), any())).thenReturn(payment);
        when(readMapper.toDto(payment)).thenReturn(expectedDto);

        PaymentDto result = service.refundPayment(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(expectedDto);
    }

    @Test
    @DisplayName("PAY-APP-003: Buscar pago por ID")
    void findById_shouldReturnDto() {
        PaymentId id = PaymentId.of(1L);
        Payment payment = mock(Payment.class);
        PaymentDto expectedDto = mock(PaymentDto.class);
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(readMapper.toDto(payment)).thenReturn(expectedDto);

        PaymentDto result = service.findById(id, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(expectedDto);
    }

    @Test
    @DisplayName("PAY-APP-004: Buscar pago no existente lanza excepción")
    void findById_notFound_throws() {
        PaymentId id = PaymentId.of(999L);
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id, mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("PAY-APP-005: Buscar pagos por factura")
    void findByInvoiceId_shouldReturnList() {
        InvoiceId invoiceId = InvoiceId.of(1L);
        Payment payment = mock(Payment.class);
        PaymentDto dto = mock(PaymentDto.class);
        when(paymentRepository.findByInvoiceId(invoiceId)).thenReturn(List.of(payment));
        when(readMapper.toDto(payment)).thenReturn(dto);

        List<PaymentDto> result = service.findByInvoiceId(invoiceId, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(dto);
    }
}