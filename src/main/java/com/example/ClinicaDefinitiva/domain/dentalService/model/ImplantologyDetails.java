package com.example.ClinicaDefinitiva.domain.dentalService.model;


import com.example.ClinicaDefinitiva.domain.dentalService.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ImplantologyError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

/**
 * Detalles de un servicio de implantología.
 *
 * Representa procedimientos de implantes dentales, incluyendo tiempo de cicatrización
 * y detalles del sitio de colocación. Incluye validaciones específicas relacionadas
 * con el tiempo de cicatrización y la necesidad de injerto óseo.
 *
 * Reglas de negocio:
 * - RN-IMPLANTOLOGY-001: El tiempo de cicatrización debe estar entre 2 y 12 meses.
 * - RN-IMPLANTOLOGY-002: Los procedimientos con injerto óseo requieren al menos 4 meses de cicatrización.
 * - RN-IMPLANTOLOGY-003: El tiempo de cicatrización no puede ser negativo.
 * - RN-IMPLANTOLOGY-007: El sitio de colocación debe tener una longitud mínima.
 */
public final class ImplantologyDetails implements ServiceDetails {

    private static final int MIN_HEALING_MONTHS = 2;
    private static final int MAX_HEALING_MONTHS = 12;
    private static final int MIN_HEALING_WITH_GRAFT = 4;
    private static final int MIN_PLACEMENT_SITE_LENGTH = 2;

    private final Integer healingTimeMonths;
    private final String implantType;
    private final String placementSite;
    private final boolean requiresBoneGraft;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param healingTimeMonths Tiempo de cicatrización en meses.
     * @param implantType Tipo de implante.
     * @param placementSite Sitio de colocación del implante.
     * @param requiresBoneGraft Indica si requiere injerto óseo.
     */
    public ImplantologyDetails(Integer healingTimeMonths, String implantType,
                               String placementSite, Boolean requiresBoneGraft) {

        // RN-IMPLANTOLOGY-003 & RN-IMPLANTOLOGY-001: Validaciones de tiempo de cicatrización
        if (healingTimeMonths != null) {
            if (healingTimeMonths < 0) {
                throw new ValueObjectValidationException(
                        ImplantologyError.ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME,
                        VOContext.DENTAL_SERVICES
                );
            }

            ValidationHelper.validateRange(
                    healingTimeMonths,
                    MIN_HEALING_MONTHS,
                    MAX_HEALING_MONTHS,
                    ImplantologyError.ERR_IMPLANTOLOGY_INVALID_HEALING_TIME,
                    VOContext.DENTAL_SERVICES
            );
        }

        boolean needsGraft = Boolean.TRUE.equals(requiresBoneGraft);

        // RN-IMPLANTOLOGY-002: Injerto óseo requiere mayor tiempo de cicatrización
        if (needsGraft && healingTimeMonths != null &&
                healingTimeMonths < MIN_HEALING_WITH_GRAFT) {
            throw new ValueObjectValidationException(
                    ImplantologyError.ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-IMPLANTOLOGY-007: Validación de longitud mínima del sitio de colocación
        ValidationHelper.validateMinLength(
                placementSite,
                MIN_PLACEMENT_SITE_LENGTH,
                ImplantologyError.ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE,
                VOContext.DENTAL_SERVICES
        );

        this.healingTimeMonths = healingTimeMonths;
        this.implantType = implantType;
        this.placementSite = placementSite;
        this.requiresBoneGraft = needsGraft;
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.IMPLANTOLOGY;
    }

    public Integer getHealingTimeMonths() {
        return healingTimeMonths;
    }

    public String getImplantType() {
        return implantType;
    }

    public String getPlacementSite() {
        return placementSite;
    }

    public Boolean getRequiresBoneGraft() {
        return requiresBoneGraft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImplantologyDetails)) return false;
        ImplantologyDetails that = (ImplantologyDetails) o;
        return requiresBoneGraft == that.requiresBoneGraft &&
                Objects.equals(healingTimeMonths, that.healingTimeMonths) &&
                Objects.equals(implantType, that.implantType) &&
                Objects.equals(placementSite, that.placementSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(healingTimeMonths, implantType, placementSite, requiresBoneGraft);
    }

    @Override
    public String toString() {
        return "ImplantologyDetails{" +
                "cicatrización=" + healingTimeMonths + " meses" +
                ", tipo=" + implantType +
                ", sitio=" + placementSite +
                ", injertoÓseo=" + requiresBoneGraft +
                '}';
    }
}
