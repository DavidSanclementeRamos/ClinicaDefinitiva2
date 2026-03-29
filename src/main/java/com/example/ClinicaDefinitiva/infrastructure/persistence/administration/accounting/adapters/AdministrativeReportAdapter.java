package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ReportRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.AdministrativeReportEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.AdministrativeReportJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.AdministrativeReport.AdministrativeReportReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.AdministrativeReport.AdministrativeReportWriteEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Transactional
public class AdministrativeReportAdapter implements ReportRepository {

    private final AdministrativeReportJpaRepository reportJpaRepository;
    private final UserIdentityJpaRepository userIdentityJpaRepository;
    private final AdministrativeReportReadEntityMapper readMapper;
    private final AdministrativeReportWriteEntityMapper writeMapper;

    public AdministrativeReportAdapter(AdministrativeReportJpaRepository reportJpaRepository,
                                       UserIdentityJpaRepository userIdentityJpaRepository,
                                       AdministrativeReportReadEntityMapper readMapper,
                                       AdministrativeReportWriteEntityMapper writeMapper) {
        this.reportJpaRepository = reportJpaRepository;
        this.userIdentityJpaRepository = userIdentityJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public AdministrativeReport save(AdministrativeReport report) {
        if (report == null) return null;

        AdministrativeReportEntity entity = writeMapper.toEntity(report);

        if (report.getCreatedBy() != null && report.getCreatedBy().value() != null) {
            userIdentityJpaRepository.findById(report.getCreatedBy().value())
                    .ifPresent(entity::setCreatedBy);
        }

        if (report.getApprovedBy() != null && report.getApprovedBy().value() != null) {
            userIdentityJpaRepository.findById(report.getApprovedBy().value())
                    .ifPresent(entity::setApprovedBy);
        }

        AdministrativeReportEntity savedEntity = reportJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdministrativeReport> findById(AdministrativeReportId id) {
        if (id == null || id.value() == null) {
            return Optional.empty();
        }
        return reportJpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministrativeReport> findByPeriod(LocalDate start, LocalDate end, Pageable pageable) {
        return reportJpaRepository.findByPeriod(start, end, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministrativeReport> findByCreator(UserIdentityId createdBy, Pageable pageable) {
        if (createdBy == null || createdBy.value() == null) {
            return Page.empty();
        }
        return reportJpaRepository.findByCreatedBy(createdBy.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministrativeReport> findByStatus(String status, Pageable pageable) {
        return reportJpaRepository.findByStatus(status, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministrativeReport> findPublishedReports() {
        return reportJpaRepository.findPublishedReports(Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministrativeReport> findDraftReports() {
        return reportJpaRepository.findDraftReports(Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministrativeReport> findAll(Pageable pageable) {
        return reportJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }
}