package com.example.ClinicaDefinitiva.application.payment.mapper;

import com.example.ClinicaDefinitiva.application.payment.dto.CreatePaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PayerRequestDto;
import com.example.ClinicaDefinitiva.application.payment.dto.RefundPaymentDto;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.Payer;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class PaymentAppWriteMapper {

    public InvoiceId toInvoiceId(CreatePaymentDto dto) {
        return InvoiceId.of(dto.invoiceId());
    }

    public Price toAmount(CreatePaymentDto dto) {
        return Price.of(dto.amount(), Currency.getInstance(dto.currency()));
    }
    
    public Price toAmount(RefundPaymentDto dto) {
        return Price.of(dto.refundAmount(), Currency.getInstance(dto.currency()));
    }

    public PaymentMethod toPaymentMethod(CreatePaymentDto dto) {
        return PaymentMethod.fromString(dto.paymentMethod());
    }

    public Payer toPayer(CreatePaymentDto dto) {
        PayerRequestDto payerDto = dto.payer();
        Payer.PayerType type = Payer.PayerType.valueOf(payerDto.type());
        return Payer.of(type, payerDto.identifier(), payerDto.name());
    }
}
