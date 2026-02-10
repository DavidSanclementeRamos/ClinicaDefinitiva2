package com.example.ClinicaDefinitiva.domain.administration.authorization.output;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.util.List;
import java.util.Optional;

public interface UserRolAssignmentRepository {
    UserRolAssignment save(UserRolAssignment assignment);
    Optional<UserRolAssignment> findById(UserRolAssignmentId id);
    //Optional<UserRolAssignment> findByUserId(UserId userId);
    //Page<UserRolAssignment> findByUserIdAndRolId(UserId userId, RolId rolId, Pageable pageable);
    Optional<UserRolAssignment> findByUserIdAndIsPrimary(UserIdentityId userIdentityId, boolean isPrimary);
    void updatePrimary(UserRolAssignmentId assignmentId, boolean isPrimary);
    void delete(UserRolAssignmentId id);

    List<UserRolAssignment> findByUserId(UserIdentityId userIdentityId);
    List<UserRolAssignment> findByUserIdAndRolId(UserIdentityId userIdentityId, RolId rolId);
}
