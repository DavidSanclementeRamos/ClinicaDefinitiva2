package com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRolAssignmentUseCase {

    Optional<ReadAssignmentDto> findById(UserRolAssignmentId targetId  , UserId requesterId, RolId requesterRolId );
    List<ReadAssignmentDto> findByUserId(UserId targeUserId  , UserId requesterId, RolId requesterRolId);
    Optional<ReadAssignmentDto> findByUserIdAndRolId(UserId targeUserId  , RolId  targeRolId, UserId requesterId, RolId requesterRolId);
    Optional<ReadAssignmentDto> findByUserIdAndIsPrimary(UserId targetUuserId, boolean isPrimary,UserId requesterId, RolId requesterRolId);
    //Page<PageAssignmentDto> findByActiveRoles(UserId targetUserId, Pageable pageable  , UserId requesterId, RolId requesterRolId);

    ReadAssignmentDto savePermanent(CreateAssignmentPermanentDto assignment,UserId requesterId, RolId requesterRolId);
    ReadAssignmentDto saveTemporary(CreateAssignmentTemporaryDto assignment,UserId requesterId, RolId requesterRolId);
    boolean isActiveAt( UserRolAssignmentId targetId , LocalDate dateUserId, UserId requesterId, RolId requesterRolId);
    boolean isCurrentlyActive( UserRolAssignmentId targetId,UserId requesterId, RolId requesterRolId );
    void extend(UserRolAssignmentId targetId, LocalDate newValidTo, UserId requesterId, RolId requesterRolId);
    void revokeAllRol( UserId targetUserId ,UserId requesterId, RolId requesterRolId);
    void revokeRol(UserId targeUserId, RolId targeRolId, UserId requesterId, RolId requesterRolId);

    //void updatePrimary(UserRolAssignmentId assignmentId, boolean isPrimary,UserId requesterId, RolId requesterRolId);
}
