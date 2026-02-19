package com.example.ClinicaDefinitiva.application.portsInput;

import com.example.ClinicaDefinitiva.application.dto.billing.invoice.ReadInvoiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillingUseCase {
   // save
    ReadInvoiceDto buildInvoice(BuildInvoiceRequest request);
    ReadInvoiceDto findId(Long invoiceId);
    ReadInvoiceDto update(UpdateInvoiceRequest invoiceRequest);
    Page<ReadInvoiceDto> findAll(Pageable pageable);
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
