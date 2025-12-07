package com.example.ClinicaDefinitiva.domain.administration.accessControl;


public interface PermissionPolicy {
    PermissionPolicy getCodigo();

     boolean estaPermitido(Rol rol, ContextoAccion contexto);


}
