
### ADR‑46 (Arquitectura): Integración JWT y Spring Security en arquitectura hexagonal

- **Fecha**: 2026‑02‑01
- **Estado**: Propuesto/Aprobado
- **Categoría**: Seguridad
- **Complementa**: ADR‑020, ADR‑021, ADR‑022

---

### Problema
El módulo de acceso necesita un mecanismo de autenticación **stateless** y escalable. Spring Security por sí solo maneja sesiones o Basic Auth, lo cual no es suficiente para integraciones futuras (mensajería, pagos). Se requiere un mecanismo que:
- Respete las reglas de negocio del agregado `UserIdentity`.
- Permita transportar identidad y roles de forma segura.
- Sea independiente del framework y reemplazable en el futuro.

---

### Decisión
Adoptar **JWT como implementación del puerto `TokenServicePort`**, con Spring Security como infraestructura técnica para interceptar requests y aplicar filtros.
- El agregado `UserIdentity` sigue siendo el dueño de las reglas (bloqueo, verificación, estado).
- Spring Security traduce esas reglas a excepciones estándar (`LockedException`, `DisabledException`).
- Los tokens incluyen claims mínimos: `sub` (email), `roles`, `iat`, `exp`.
- La política de intentos fallidos y bloqueo temporal se define en `SecurityPolicy`.

---

### Consecuencias
- **Ganamos**:
    - Autenticación stateless, lista para integraciones externas.
    - Flexibilidad para reemplazar JWT por OAuth2/OpenID sin tocar el dominio.
    - Exhibición profesional del proyecto (arquitectura preparada para crecer).

- **Perdemos**:
    - Mayor complejidad inicial (filtro JWT, proveedor de tokens).
    - Necesidad de manejar expiración y revocación de tokens.

---

### Implementación
1. **Puerto de salida `TokenServicePort`** implementado con JWT.
2. **Adaptador `JwtProvider`** para generar y validar tokens.
3. **Filtro `JwtAuthenticationFilter`** en Spring Security para interceptar requests.
4. **Caso de uso `authenticate`** en `UserApplicationService` que valida credenciales con `UserIdentity` y genera token.
5. **Eventos de dominio** (`UserLoginFailed`, `UserLoggedIn`) para auditoría.

---

### Relación con otros ADRs
- **Complementa**:
    - ADR‑020 (hexagonal en módulo de acceso).
    - ADR‑021 (UserIdentity como agregado rico).
    - ADR‑022 (Integración Spring Security con dominio).
- **Motiva**:
    - Preparación para integraciones futuras con APIs externas.
    - Seguridad consistente y trazable desde el dominio.

