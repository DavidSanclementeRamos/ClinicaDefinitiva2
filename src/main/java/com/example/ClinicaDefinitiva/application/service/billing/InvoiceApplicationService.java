package com.example.ClinicaDefinitiva.application.service.billing;


import com.example.ClinicaDefinitiva.application.dto.billing.*;
import com.example.ClinicaDefinitiva.application.dto.billing.invoice.*;
import com.example.ClinicaDefinitiva.application.exceptions.InvoiceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.billing.invoice.InvoiceReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.billing.invoice.InvoiceWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.billing.InvoiceUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceDomainService;
import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceItemFactoryService;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.portsOutput.InvoiceRepository;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private final AuthorizationService authorizationService;
    private final InvoiceNumberGenerator invoiceNumberGenerator;

    public InvoiceApplicationService(
            InvoiceRepository invoiceRepository,
            PatientRepository patientRepository,
            DentistRepository dentistRepository,
            ReceptionRepository receptionRepository,
            InvoiceDomainService invoiceDomainService,
            InvoiceItemFactoryService invoiceItemFactoryService,
            InvoiceReadMapper readMapper,
            InvoiceWriteMapper writeMapper,
            AuthorizationService authorizationService,
            InvoiceNumberGenerator invoiceNumberGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.receptionRepository = receptionRepository;
        this.invoiceDomainService = invoiceDomainService;
        this.invoiceItemFactoryService = invoiceItemFactoryService;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public ReadInvoiceDto findById(InvoiceId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not found"));

        // Build security context with ownership
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withResourceId(id.getValue());

        // Patient ownership
        if (invoice.getPatientId() != null) {
            Patient patient = patientRepository.findById(invoice.getPatientId())
                    .orElseThrow(() -> new PatientNotFoundException("Not found"));
            contextBuilder.withResourceOwnerId(patient.getUser());
        }

        // Dentist assignment
        if (invoice.getDentistId() != null) {
            Dentist dentist = dentistRepository.findById(invoice.getDentistId())
                    .orElseThrow(() -> new DentistNotFoundException("Not found"));
            contextBuilder.withAttribute("assignedDentistUserId",
                    dentist.getUserId());
        }

        // Receptionist sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return readMapper.toDto(invoice);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId);

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return invoiceRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByPatient(Long patientId,
                                              Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Patient patient = patientRepository.findById(PatientId.of(patientId))
                .orElseThrow(() -> new PatientNotFoundException("Not found"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withResourceOwnerId(patient.getUser());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return invoiceRepository.findByPatient(patient.getPatientId(), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByDentist(Long dentistId,
                                              Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(DentistId.of(dentistId))
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withAttribute("assignedDentistUserId", dentist.getUserId());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return invoiceRepository.findByDentist(dentist.getDentistId(), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageInvoiceDto> findByStatus(InvoiceStatus.Status status,
                                             Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId);

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return invoiceRepository.findByStatus(status, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.READ)
    public ReadInvoiceDto findByNumber(String invoiceNumber,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {

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

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId);

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Invoice invoice = writeMapper.fromCreateParticularDto(dto);
        Invoice saved = invoiceRepository.save(invoice);

        return readMapper.toDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadInvoiceDto createInstitutional(CreateInstitutionalInvoiceDto dto,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Invoice invoice = writeMapper.fromCreateInstitutionalDto(dto);

        // RN-INVOICE-007: Validate institutional contract
        invoiceDomainService.validateInstitutionalContract(invoice);

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

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Not found"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withResourceId(invoiceId.getValue());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // RN-INVOICE-004: Only editable in DRAFT status (enforced by domain)
        InvoiceItem item = writeMapper.toInvoiceItem(dto, invoiceItemFactoryService);
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

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not fount"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withResourceId(id.getValue());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not found"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withResourceId(id.getValue());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // RN-INVOICE-005, RN-INVOICE-009: Validated in domain
        invoice.cancel(reason);

        Invoice cancelled = invoiceRepository.save(invoice);
        return readMapper.toDto(cancelled);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.INVOICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadInvoiceDto markAsPaid(InvoiceId id,
                                     LocalDate paymentDate,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Not fount"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)), requesterId)
                .withResourceId(id.getValue());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // Transition to PAID status
        if (!invoice.getStatus().isPending()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_MUST_BE_PENDING_TO_PAY,
                    EntityContext.INVOICE
            );
        }

        invoice.getStatus().transitionTo(InvoiceStatus.Status.PAID);

        Invoice paid = invoiceRepository.save(invoice);
        return readMapper.toDto(paid);
    }
}
