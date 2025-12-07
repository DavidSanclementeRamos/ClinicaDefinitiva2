package com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes;

import java.util.Objects;

public class UserStatus {
    public enum Status {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        PENDING_VERIFICATION,
        DELETED
    }

    private final Status value;

    private UserStatus(Status value) {
        this.value = Objects.requireNonNull(value, "Status cannot be null");
    }

    public static UserStatus of(Status value) {
        return new UserStatus(value);
    }

    public Status getValue() {
        return value;
    }

    public boolean isActive() {
        return value == Status.ACTIVE;
    }

    public boolean isSuspended() {
        return value == Status.SUSPENDED;
    }

    public boolean isInactive() {
        return value == Status.INACTIVE;
    }
    public boolean isPendingVerification() {
        return value == Status.PENDING_VERIFICATION;
    }

    public boolean isDeleted() {
        return value == Status.DELETED;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStatus)) return false;
        UserStatus that = (UserStatus) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "StatuUser{" + "value=" + value + '}';
    }



}
