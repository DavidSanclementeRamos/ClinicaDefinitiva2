package com.example.ClinicaDefinitiva.domain.dentalService.service;


import com.example.ClinicaDefinitiva.domain.dentalService.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Set;

/**
 * Interfaz base para los detalles específicos de un servicio dental.
 *
 * Cada tipo de servicio dental (Ortodoncia, Cirugía, Estética, etc.)
 * implementa esta interfaz para proveer atributos propios y validaciones.
 *
 * Esta interfaz asegura polimorfismo y consistencia en la categorización
 * de los servicios, además de centralizar validaciones comunes.
 */
public interface ServiceDetails {

    /**
     * Devuelve el tipo de servicio representado por estos detalles.
     * Se utiliza para comportamiento polimórfico y validación de categorías.
     */
    ServiceType serviceType();

    /**
     * Clase auxiliar que provee métodos de validación comunes
     * para las implementaciones de ServiceDetails.
     */
    abstract class ValidationHelper {

        /**
         * Normaliza un valor de texto a mayúsculas y lo valida contra un conjunto de valores permitidos.
         *
         * @param value      Valor a normalizar y validar.
         * @param validValues Conjunto de valores válidos en mayúsculas.
         * @param errorCatalog  Catálogo de error a lanzar si la validación falla.
         * @param context    Contexto para el reporte de error.
         * @return Valor normalizado en mayúsculas.
         * @throws ValueObjectValidationException si el valor es nulo, vacío o no está en la lista de válidos.
         */
        public static String normalizeAndValidate(
                String value,
                Set<String> validValues,
                ErrorCatalog errorCatalog,
                VOContext context) {

            if (value == null || value.isBlank()) {
                throw new ValueObjectValidationException(errorCatalog, context);
            }

            String normalized = value.toUpperCase();

            if (!validValues.contains(normalized)) {
                throw new ValueObjectValidationException(errorCatalog, context);
            }

            return normalized;
        }

        /**
         * Valida que un valor numérico esté dentro de un rango específico (inclusive).
         *
         * @param value     Valor a validar (puede ser nulo).
         * @param min       Valor mínimo permitido.
         * @param max       Valor máximo permitido.
         * @param errorCatalog Catálogo de error a lanzar si la validación falla.
         * @param context   Contexto para el reporte de error.
         * @throws ValueObjectValidationException si el valor está fuera del rango.
         */
        public static void validateRange(
                Integer value,
                int min,
                int max,
                ErrorCatalog errorCatalog,
                VOContext context) {

            if (value != null && (value < min || value > max)) {
                throw new ValueObjectValidationException(errorCatalog, context);
            }
        }

        /**
         * Valida que una cadena tenga una longitud mínima.
         *
         * @param value     Valor a validar (puede ser nulo).
         * @param minLength Longitud mínima requerida.
         * @param errorCatalog Catálogo de error a lanzar si la validación falla.
         * @param context   Contexto para el reporte de error.
         * @throws ValueObjectValidationException si la cadena es más corta que la longitud mínima.
         */
        public static void validateMinLength(
                String value,
                int minLength,
                ErrorCatalog errorCatalog,
                VOContext context) {

            if (value != null && value.length() < minLength) {
                throw new ValueObjectValidationException(errorCatalog, context);
            }
        }
    }
}
