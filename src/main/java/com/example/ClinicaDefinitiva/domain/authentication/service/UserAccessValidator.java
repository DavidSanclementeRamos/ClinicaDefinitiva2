package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.application.exceptions.UserIdentityNoFoundException;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.UserNotEligibleException;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import java.time.Instant;
import java.util.List;


/**
 * Domain Service que actúa como Anti-Corruption Layer entre:
 * - Módulo técnico de acceso (UserIdentity) que usa Outcome
 * - Módulos de negocio (Patient, Appointment, etc.) que usan Exceptions
 *
 * Responsabilidades:
 * - Validar que un usuario puede realizar acciones sensibles
 * - Traducir Outcome (técnico) a Exceptions (negocio)
 * - Centralizar lógica de validación de acceso
 * - Evitar acoplamiento directo entre bounded contexts
 *
 */
public class UserAccessValidator {

    private final UserIdentityRepository userRepository;

    public UserAccessValidator(UserIdentityRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Valida que un usuario existe y puede realizar acciones sensibles.
     *
     * Lanza excepciones de negocio si:
     * - El usuario no existe
     * - El usuario no está verificado
     * - El usuario está bloqueado
     * - El usuario está inactivo/suspendido
     *
     * @param userIdentityId ID del usuario a validar
     * @param now Instante actual para validaciones temporales (bloqueos)
     * @param requesterContext Contexto de la entidad que solicita la validación
     * @throws UserIdentityNoFoundException si el usuario no existe
     * @throws UserNotEligibleException si el usuario no cumple los requisitos
     */
    public void validateUserCanPerformSensitiveAction(
            UserIdentityId userIdentityId,
            Instant now,
            EntityContext requesterContext
    ) {
        UserIdentity user = userRepository.findById(userIdentityId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                       
                ));

        Outcome<UserIdentity> eligibility = user.canPerformSensitiveAction(now);

        if (!eligibility.isSuccess()) {
            throw translateToBusinessException(
                    userIdentityId,
                    eligibility.getDetalles(),
                    requesterContext
            );
        }
    }





    /**
     * Traduce un Outcome fallido (del módulo técnico) a una excepción
     * de negocio apropiada (para módulos de dominio).
     *
     * Esta es la capa anti-corrupción que mantiene separados los
     * estilos de manejo de errores de cada bounded context.
     */
    private UserNotEligibleException translateToBusinessException(
            UserIdentityId userIdentityId,
            List<OutcomeDetail> details,
            EntityContext requesterContext
    ) {
        OutcomeDetail primaryDetail = details.get(0);


        String reason = buildReasonMessage(primaryDetail);

        return new UserNotEligibleException(
                userIdentityId,
                reason,
                requesterContext,
                details
        );
    }

    /**
     * Construye un mensaje legible desde un OutcomeDetail.
     */
    private String buildReasonMessage(OutcomeDetail detail) {
        return switch (detail.getCode().toString()) {
            case "ERR_USER_NOT_VERIFIED" ->
                    "Usuario no ha verificado su cuenta";
            case "ERR_USER_ACCOUNT_LOCKED" ->
                    "Cuenta de usuario bloqueada temporalmente";
            case "ERR_USER_INACTIVE" ->
                    "Usuario inactivo";
            case "ERR_USER_SUSPENDED" ->
                    "Usuario suspendido";
            default ->
                    "Usuario no elegible para realizar esta acción";
        };
    }
}
