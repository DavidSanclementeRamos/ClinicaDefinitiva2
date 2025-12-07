package com.example.ClinicaDefinitiva.domain.administration.permisos;


public interface PermissionPolicy {
    PermissionPolicy getCodigo();

     boolean estaPermitido(Rol rol, ContextoAccion contexto);


}
