package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.billing.BuildInvoiceRequest;
import com.example.ClinicaDefinitiva.application.dto.billing.InvoiceDto;
import com.example.ClinicaDefinitiva.application.dto.billing.UpdateInvoiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillingUseCase {
   // save
    InvoiceDto buildInvoice(BuildInvoiceRequest request);
    InvoiceDto findId(Long invoiceId);
    InvoiceDto update(UpdateInvoiceRequest invoiceRequest);
    Page<InvoiceDto> findAll(Pageable pageable);
    void deleById(Long id);
    // otras búsquedas????


    /**
     * ejemplo de lo que se hace en caso nde uso

     HorarioDto findId(long horarioId);

     HorarioDto save( HorarioDto horarioDto);

     HorarioDto update(long horarioId, HorarioDto horarioDto);

     Page<HorarioDto> findAll(Pageable pageable);

     List<HorarioDto> findByOdontologo_Id(long idOdontologo);

     List<HorarioDto> findByDiaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(DayOfWeek dia, LocalTime desde, LocalTime hasta);

     List<HorarioDto> findByFechaBetween(LocalDate desde, LocalDate hasta);

     void deleById(long id);
     */
}
