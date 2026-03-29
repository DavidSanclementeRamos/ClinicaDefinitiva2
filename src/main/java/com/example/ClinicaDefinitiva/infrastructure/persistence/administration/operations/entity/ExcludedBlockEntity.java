package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "turno_bloque_excluido")
public class ExcludedBlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_turno", nullable = false)
    private ShiftEntity shift;

    @Column(name = "hora_inicio_bloque", nullable = false)
    private LocalTime blockStartTime;

    @Column(name = "hora_fin_bloque", nullable = false)
    private LocalTime blockEndTime;

    @Column(name = "motivo", nullable = false, length = 255)
    private String reason;

    public ExcludedBlockEntity() {}

    public Long getId()                  { return id; }
    public ShiftEntity getShift()         { return shift; }
    public LocalTime getBlockStartTime()  { return blockStartTime; }
    public LocalTime getBlockEndTime()    { return blockEndTime; }
    public String getReason()             { return reason; }

    public void setShift(ShiftEntity shift)                 { this.shift = shift; }
    public void setBlockStartTime(LocalTime blockStartTime) { this.blockStartTime = blockStartTime; }
    public void setBlockEndTime(LocalTime blockEndTime)     { this.blockEndTime = blockEndTime; }
    public void setReason(String reason)                     { this.reason = reason; }
}
