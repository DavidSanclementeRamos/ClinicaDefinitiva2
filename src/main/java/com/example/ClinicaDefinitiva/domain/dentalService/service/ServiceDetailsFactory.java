package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.dentalService.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.AgeRange;

import java.util.Map;

/**
 * Fábrica para crear instancias de ServiceDetails.
 *
 * Centraliza la lógica de creación de detalles específicos de servicio,
 * facilitando:
 * - Mapeo desde DTOs o estructuras externas (JSON, requests)
 * - Construcción tipada y consistente en pruebas internas
 * - Validaciones uniformes en un único punto
 *
 * Uso recomendado:
 * - En la capa de aplicación: usar {@link #fromMap(ServiceType, Map)} para construir dinámicamente
 *   el detalle correcto según el tipo de servicio.
 * - En pruebas o casos internos: usar los métodos createXxx() para instanciar directamente
 *   un detalle específico.
 */
public class ServiceDetailsFactory {

    private ServiceDetailsFactory() {
        throw new UnsupportedOperationException("Clase de fábrica");
    }


    public static ServiceDetails createOrthodontic(
            String applianceType,
            Integer treatmentDurationMonths,
            Boolean requiresFollowup) {
        return new OrthodonticDetails(applianceType, treatmentDurationMonths, requiresFollowup);
    }

    public static ServiceDetails createSurgical(
            String surgeryType,
            String complexityLevel,
            Boolean requiresAnesthesia,
            Boolean operatingRoomNeeded) {
        return new SurgicalDetails(surgeryType, complexityLevel, requiresAnesthesia, operatingRoomNeeded);
    }

    public static ServiceDetails createAesthetic(
            String aestheticType,
            String materialUsed,
            String expectedResult) {
        return new AestheticDetails(aestheticType, materialUsed, expectedResult);
    }

    public static ServiceDetails createImplantology(
            Integer healingTimeMonths,
            String implantType,
            String placementSite,
            Boolean requiresBoneGraft) {
        return new ImplantologyDetails(healingTimeMonths, implantType, placementSite, requiresBoneGraft);
    }

    public static ServiceDetails createPediatric(
            AgeRange ageRange,
            String behaviorManagement,
            String pediatricMaterials) {
        return new PediatricDetails(ageRange, behaviorManagement, pediatricMaterials);
    }

    public static ServiceDetails createProsthetic(
            String fixedOrRemovable,
            String material,
            String prostheticType,
            Integer units) {
        return new ProstheticDetails(fixedOrRemovable, material, prostheticType, units);
    }


    /**
     * Crea detalles de servicio a partir de un mapa de campos.
     *
     * Este método es conveniente cuando se mapea desde fuentes externas (DTOs, JSON),
     * donde el tipo de servicio determina qué implementación específica se debe crear.
     *
     * @param type Tipo de servicio
     * @param fields Mapa con los valores de los campos
     * @return Implementación adecuada de ServiceDetails
     * @throws IllegalArgumentException si el tipo de servicio es desconocido
     * @throws ClassCastException si los tipos de los campos no coinciden con los esperados
     *
     * Ejemplo:
     * <pre>
     * Map<String, Object> fields = Map.of(
     *     "applianceType", "METAL_BRACKETS",
     *     "treatmentDurationMonths", 24,
     *     "requiresFollowup", true
     * );
     * ServiceDetails details = ServiceDetailsFactory.fromMap(ServiceType.ORTHODONTIC, fields);
     * </pre>
     */
    public static ServiceDetails fromMap(ServiceType type, Map<String, Object> fields) {
        return switch (type) {
            case ORTHODONTIC -> createOrthodontic(
                    (String) fields.get("applianceType"),
                    (Integer) fields.get("treatmentDurationMonths"),
                    (Boolean) fields.get("requiresFollowup")
            );
            case SURGERY -> createSurgical(
                    (String) fields.get("surgeryType"),
                    (String) fields.get("complexityLevel"),
                    (Boolean) fields.get("requiresAnesthesia"),
                    (Boolean) fields.get("operatingRoomNeeded")
            );
            case AESTHETICS -> createAesthetic(
                    (String) fields.get("aestheticType"),
                    (String) fields.get("materialUsed"),
                    (String) fields.get("expectedResult")
            );
            case IMPLANTOLOGY -> createImplantology(
                    (Integer) fields.get("healingTimeMonths"),
                    (String) fields.get("implantType"),
                    (String) fields.get("placementSite"),
                    (Boolean) fields.get("requiresBoneGraft")
            );
            case PEDIATRICS -> createPediatric(
                    (AgeRange) fields.get("ageRange"),
                    (String) fields.get("behaviorManagement"),
                    (String) fields.get("pediatricMaterials")
            );
            case PROSTHETICS -> createProsthetic(
                    (String) fields.get("fixedOrRemovable"),
                    (String) fields.get("material"),
                    (String) fields.get("prostheticType"),
                    (Integer) fields.get("units")
            );
            default -> throw new IllegalArgumentException("Tipo de servicio desconocido: " + type);
        };
    }
}
