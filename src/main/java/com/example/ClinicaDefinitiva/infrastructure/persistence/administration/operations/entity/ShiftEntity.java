package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity;

import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turno")
public class ShiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dentista", nullable = false)
    private DentistEntity dentist;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime startTime;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime endTime;

    @Column(name = "tipo", nullable = false, length = 30)
    private String type;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;

    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    private String cancellationReason;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExcludedBlockEntity> excludedBlocks = new ArrayList<>();

    public ShiftEntity() {}

    public Long getId()                         { return id; }
    public DentistEntity getDentist()            { return dentist; }
    public LocalDate getDate()                   { return date; }
    public LocalTime getStartTime()               { return startTime; }
    public LocalTime getEndTime()                 { return endTime; }
    public String getType()                       { return type; }
    public String getStatus()                     { return status; }
    public String getCancellationReason()         { return cancellationReason; }
    public long getVersion()                      { return version; }
    public List<ExcludedBlockEntity> getExcludedBlocks() { return excludedBlocks; }

    public void setDentist(DentistEntity dentist)              { this.dentist = dentist; }
    public void setDate(LocalDate date)                         { this.date = date; }
    public void setStartTime(LocalTime startTime)               { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime)                   { this.endTime = endTime; }
    public void setType(String type)                             { this.type = type; }
    public void setStatus(String status)                         { this.status = status; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    public void setVersion(long version)                         { this.version = version; }
    public void setExcludedBlocks(List<ExcludedBlockEntity> excludedBlocks) { this.excludedBlocks = excludedBlocks; }
}
