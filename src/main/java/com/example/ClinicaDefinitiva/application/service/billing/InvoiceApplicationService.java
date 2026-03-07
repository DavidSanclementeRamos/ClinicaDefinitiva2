package com.example.ClinicaDefinitiva.application.service.billing;


import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.InvoiceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.ProvidedServiceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.RateNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.billing.invoice.InvoiceReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.billing.invoice.InvoiceWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.billing.InvoiceUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceDomainService;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceItemFactoryService;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * InvoiceApplicationService refactorizado.
 *
 * POLÍTICAS:
 * - SectorBasedPolicy: Receptionist por sector
 * - OwnershipPolicy: Paciente ve sus propias facturas
 * - AssignmentPolicy: Dentista ve facturas donde está asignado
 */
@Service
@Transactional
public class InvoiceApplicationService implements InvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final ReceptionRepository receptionRepository;
    private final InvoiceDomainService invoiceDomainService;
    private final InvoiceItemFactoryService invoiceItemFactoryService;
    private final InvoiceReadMapper readMapper;
    private final InvoiceWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;
    private final InvoiceNumberGenerator invoiceNumberGenerator;
    private final RateRepository rateRepository;
    private final ProvidedServiceRepository providedServiceRepository;

    public InvoiceApplicationService(InvoiceRepository invoiceRepository, PatientRepository patientRepository, DentistRepository dentistRepository, ReceptionRepository receptionRepository, InvoiceDomainService invoiceDomainService, InvoiceItemFactoryService invoiceItemFactoryService, InvoiceReadMapper readMapper, InvoiceWriteMapper writeMapper, AuthorizationHelper authorizationHelper, InvoiceNumberGenerator invoiceNumberGenerator, RateRepository rateRepository, ProvidedServiceRepository providedServiceRepository) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.receptionRepository = receptionRepository;
        this.invoiceDomainService = invoiceDomainService;
        this.invoiceItemFactoryService = invoiceItemFactoryService;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
        this.rateRepository = rateRepository;
        this.providedServiceRepository = providedServiceRepository;
    }


    

  

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public ReadInvoiceDto findById(InvoiceId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {



        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not found"));

        // Obtener patient y dentist para ownership/assignment
        Patient patient = invoice.getPatientId() != null ?
                patientRepository.findById(invoice.getPatientId())
                        .orElseThrow(() -> new PatientNotFoundException("Not found")) : null;

        Dentist dentist = invoice.getDentistId() != null ?
                dentistRepository.findById(invoice.getDentistId())
                        .orElseThrow(() -> new DentistNotFoundException("Not found")) : null;

        // Ownership + Assignment
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .withOwnership(patient != null ? patient.getUser() : null) // ← Paciente ve sus facturas
                        .withAssignedDentist(dentist != null ? dentist.getUserId() : null) // ← Dentista ve sus facturas
                        .build()
        );
          
        return readMapper.toDto(invoice);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .build()
        );


        return invoiceRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByPatient(PatientId patientId,
                                              Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Not found"));

        // OwnershipPolicy: Paciente solo ve sus propias facturas
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withOwnership(patient.getUser())
                        .build()
        );



        return invoiceRepository.findByPatient(patientId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByDentist(DentistId dentistId,
                                              Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // AssignmentPolicy: Dentista solo ve facturas donde está asignado
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withAssignedDentist(dentist.getUserId())
                        .build()
        );

        return invoiceRepository.findByDentist(dentistId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByStatus(InvoiceStatus.Status status,
                                             Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .build()
               );
        return invoiceRepository.findByStatus(status, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public ReadInvoiceDto findByNumber(String invoiceNumber,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {
        
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .build()
               );

        Invoice invoice = invoiceRepository.findByNumber(invoiceNumber)
                .orElseThrow(() -> new InvoiceNotFoundException("Not found"));

        return findById(invoice.getId(), requesterId, requesterRolId);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByDateRange(LocalDate startDate,
                                                LocalDate endDate,
                                                Pageable pageable,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .build()
               );

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return invoiceRepository.findByDateRange(start, end, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadInvoiceDto createParticular(CreateParticularInvoiceDto dto,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

        // Solo receptionist puede crear (sector-based)
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );
        Invoice invoice = Invoice.createParticular(
            writeMapper.toPatientId(dto),
            writeMapper.toProviderId(dto),
            writeMapper.toDentistId(dto),
            writeMapper.toCurrency(dto),
            writeMapper.toNotes(dto),
            writeMapper.toDueDate(dto)
        );
        
        invoiceDomainService.validateRates(invoice, dto.dueDate());
        Invoice saved = invoiceRepository.save(invoice);

        return readMapper.toDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadInvoiceDto createInstitutional(CreateInstitutionalInvoiceDto dto,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder()
                        .build()
               );


        Invoice invoice = Invoice.createInstitutional(
                writeMapper.toContractId(dto),
            writeMapper.toProviderId(dto),
            writeMapper.toDentistId(dto),
            writeMapper.toCurrency(dto),
            writeMapper.toNotes(dto),
            writeMapper.toDueDate(dto)
        );

        // RN-INVOICE-007: Validate institutional contract
        invoiceDomainService.validateInstitutionalContract(invoice);
        invoiceDomainService.validateRates(invoice, dto.dueDate());
        

        Invoice saved = invoiceRepository.save(invoice);

        return readMapper.toDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadInvoiceDto addItem(InvoiceId invoiceId,
                                  AddInvoiceItemDto dto,
                                  UserIdentityId requesterId,
                                  RolId requesterRolId) {

        

         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                       .withResourceId(invoiceId.getValue())

                        .build()
               );
        

    Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new InvoiceNotFoundException("Not found"));

    // Buscar el ProvidedService y Rate desde sus repositorios
    ProvidedService service = providedServiceRepository.findById(writeMapper.toServiceId(dto))
            .orElseThrow(() -> new ProvidedServiceNotFoundException("Not found"));

    Rate rate = rateRepository.findById(writeMapper.toRateId(dto))
            .orElseThrow(() -> new RateNotFoundException("Not fount"));

    // Crear el InvoiceItem usando el domain service
    InvoiceItem item = invoiceItemFactoryService.createFromRateSnapshot(
            service,
            rate,
            writeMapper.toQuantity(dto),
            writeMapper.toPerformedAt(dto)
    );
            invoiceDomainService.validanteService(service.getId());


    invoice.addItem(item);

    Invoice updated = invoiceRepository.save(invoice);
    return readMapper.toDto(updated);

    }   

      

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadInvoiceDto emit(InvoiceId id,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
               );
       
       
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not fount"));


        // RN-INVOICE-003: Validate rates
        invoiceDomainService.validateRates(invoice, LocalDateTime.now());

        // RN-INVOICE-001, RN-INVOICE-002: Validated in domain
        invoice.emit(invoiceNumberGenerator);

        Invoice emitted = invoiceRepository.save(invoice);
        return readMapper.toDto(emitted);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadInvoiceDto cancel(InvoiceId id,
                                 String reason,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

               authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.INVOICE,
                ActionCatalog.BasicAction.CANCEL,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
               );
       

                Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not found"));


        // RN-INVOICE-005, RN-INVOICE-009: Validated in domain
        invoice.cancel(reason);

        Invoice cancelled = invoiceRepository.save(invoice);
        return readMapper.toDto(cancelled);
    }

    

    
}
