package com.example.ClinicaDefinitiva.domain.administration.authorization.output;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRolAssignmentRepository {
    UserRolAssignment save(UserRolAssignment assignment);
    Optional<UserRolAssignment> findById(UserRolAssignmentId id);
    Optional<UserRolAssignment> findByUserIdAndIsPrimary(UserIdentityId userIdentityId, boolean isPrimary);
    void delete(UserRolAssignmentId id);

    Page<UserRolAssignment> findByUserId(UserIdentityId userIdentityId,Pageable pageable);
    Page<UserRolAssignment> findByUserIdAndRolId(UserIdentityId userIdentityId, RolId rolId,Pageable pageable);
    
    /**
     * Actualiza solo el flag isPrimary de una asignación
     * @param assignmentId ID de la asignación
     * @param isPrimary nuevo valor
     */
    void updatePrimary(UserRolAssignmentId assignmentId, boolean isPrimary);

}
