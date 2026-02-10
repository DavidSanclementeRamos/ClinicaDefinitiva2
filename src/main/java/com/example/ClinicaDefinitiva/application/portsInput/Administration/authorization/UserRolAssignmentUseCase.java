package com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRolAssignmentUseCase {

    Optional<ReadAssignmentDto> findById(UserRolAssignmentId targetId  , UserIdentityId requesterId, RolId requesterRolId );
    List<ReadAssignmentDto> findByUserId(UserIdentityId targeUserIdentityId, UserIdentityId requesterId, RolId requesterRolId);
    Optional<ReadAssignmentDto> findByUserIdAndRolId(UserIdentityId targeUserIdentityId, RolId  targeRolId, UserIdentityId requesterId, RolId requesterRolId);
    Optional<ReadAssignmentDto> findByUserIdAndIsPrimary(UserIdentityId targetUuserIdentityId, boolean isPrimary, UserIdentityId requesterId, RolId requesterRolId);
    //Page<PageAssignmentDto> findByActiveRoles(UserId targetUserId, Pageable pageable  , UserId requesterId, RolId requesterRolId);

    ReadAssignmentDto savePermanent(CreateAssignmentPermanentDto assignment, UserIdentityId requesterId, RolId requesterRolId);
    ReadAssignmentDto saveTemporary(CreateAssignmentTemporaryDto assignment, UserIdentityId requesterId, RolId requesterRolId);
    boolean isActiveAt(UserRolAssignmentId targetId , LocalDate dateUserId, UserIdentityId requesterId, RolId requesterRolId);
    boolean isCurrentlyActive(UserRolAssignmentId targetId, UserIdentityId requesterId, RolId requesterRolId );
    void extend(UserRolAssignmentId targetId, LocalDate newValidTo, UserIdentityId requesterId, RolId requesterRolId);
    void revokeAllRol(UserIdentityId targetUserIdentityId, UserIdentityId requesterId, RolId requesterRolId);
    void revokeRol(UserIdentityId targeUserIdentityId, RolId targeRolId, UserIdentityId requesterId, RolId requesterRolId);

    //void updatePrimary(UserRolAssignmentId assignmentId, boolean isPrimary,UserId requesterId, RolId requesterRolId);
}
