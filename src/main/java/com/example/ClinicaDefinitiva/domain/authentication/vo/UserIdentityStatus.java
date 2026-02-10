package com.example.ClinicaDefinitiva.domain.authentication.vo;


public class UserIdentityStatus {
    public final State state;

    public enum State {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        PENDING_VERIFICATION
    }

    private UserIdentityStatus(State state) {
        this.state = state;
    }



    public static UserIdentityStatus of(State state) {
        return new UserIdentityStatus(state );
    }

    /**public void mustBeActive(ErrorCatalog error, EntityContext contexto) {
        if (state != State.ACTIVE) {
            throw new BusinessRuleViolationException(error, contexto);
        }
    }*/

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public boolean isInactive() {
        return state == State.INACTIVE;
    }

    public boolean isSuspended() {
        return state == State.SUSPENDED;
    }

    public boolean isPendingVerification() {
        return state == State.PENDING_VERIFICATION;
    }

    public State getState() {
        return state;
    }

    /**
     * Valida si una transición de estado es válida.
     *
     * Transiciones válidas:
     * - PENDING_VERIFICATION -> ACTIVE (después de verificar)
     * - ACTIVE -> INACTIVE (desactivación voluntaria)
     * - ACTIVE -> SUSPENDED (suspensión administrativa)
     * - INACTIVE -> ACTIVE (reactivación)
     * - SUSPENDED -> ACTIVE (levantamiento de suspensión)
     *
     * Transiciones inválidas:
     * - PENDING_VERIFICATION -> SUSPENDED
     * - INACTIVE -> SUSPENDED
     * - Cualquier otra no listada arriba
     *
     * @param newState Nuevo estado al que se quiere transicionar
     * @return true si la transición es válida, false en caso contrario
     */
    public boolean canTransitionTo(State newState) {
        if (this.state == newState) {
            return false; // No tiene sentido transicionar al mismo estado
        }

        return switch (this.state) {
            case PENDING_VERIFICATION, INACTIVE, SUSPENDED -> newState == State.ACTIVE;
            case ACTIVE -> newState == State.INACTIVE || newState == State.SUSPENDED;
        };

        /**
         *
         * switch (this.state) {
         *             case PENDING_VERIFICATION:
         *                 return newState == State.ACTIVE;
         *
         *             case ACTIVE:
         *                 return newState == State.INACTIVE || newState == State.SUSPENDED;
         *
         *             case INACTIVE:
         *                 return newState == State.ACTIVE;
         *
         *             case SUSPENDED:
         *                 return newState == State.ACTIVE;
         *
         *             default:
         *                 return false;
         *         }*/
    }
}