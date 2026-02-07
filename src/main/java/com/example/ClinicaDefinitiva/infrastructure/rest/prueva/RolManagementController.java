package com.example.ClinicaDefinitiva.infrastructure.rest.prueva;

@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('RECEPTIONIST')") // Solo admin
public class RolManagementController {

    private final RolManagementService rolService;

    public RolManagementController(RolManagementService rolService) {
        this.rolService = rolService;
    }

    /**
     * Crear rol personalizado
     */
    @PostMapping
    public ResponseEntity<RolDto> createCustomRole(@RequestBody CreateRoleRequest request) {
        Set<Permission> permissions = request.permissions().stream()
                .map(p -> Permission.of(p.resource(), p.action()))
                .collect(Collectors.toSet());

        Rol created = rolService.createCustomRole(
                request.baseType(),
                request.description(),
                permissions
        );

        return ResponseEntity.ok(RolDto.from(created));
    }

    /**
     * Actualizar permisos de rol
     */
    @PutMapping("/{rolId}/permissions")
    public ResponseEntity<RolDto> updatePermissions(
            @PathVariable Long rolId,
            @RequestBody Set<PermissionDto> permissions) {

        Set<Permission> perms = permissions.stream()
                .map(p -> Permission.of(p.resource(), p.action()))
                .collect(Collectors.toSet());

        Rol updated = rolService.updateRolePermissions(rolId, perms);

        return ResponseEntity.ok(RolDto.from(updated));
    }

    /**
     * Agregar permiso a rol
     */
    @PostMapping("/{rolId}/permissions")
    public ResponseEntity<RolDto> addPermission(
            @PathVariable Long rolId,
            @RequestBody PermissionDto permission) {

        Permission perm = Permission.of(permission.resource(), permission.action());
        Rol updated = rolService.addPermissionToRole(rolId, perm);

        return ResponseEntity.ok(RolDto.from(updated));
    }

    /**
     * Remover permiso de rol
     */
    @DeleteMapping("/{rolId}/permissions")
    public ResponseEntity<RolDto> removePermission(
            @PathVariable Long rolId,
            @RequestBody PermissionDto permission) {

        Permission perm = Permission.of(permission.resource(), permission.action());
        Rol updated = rolService.removePermissionFromRole(rolId, perm);

        return ResponseEntity.ok(RolDto.from(updated));
    }

    /**
     * Clonar rol existente
     */
    @PostMapping("/{rolId}/clone")
    public ResponseEntity<RolDto> cloneRole(
            @PathVariable Long rolId,
            @RequestBody CloneRoleRequest request) {

        Rol cloned = rolService.cloneRole(rolId, request.newDescription());

        return ResponseEntity.ok(RolDto.from(cloned));
    }

    /**
     * Eliminar rol personalizado
     */
    @DeleteMapping("/{rolId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long rolId) {
        rolService.deleteRole(rolId);
        return ResponseEntity.noContent().build();
    }
}
