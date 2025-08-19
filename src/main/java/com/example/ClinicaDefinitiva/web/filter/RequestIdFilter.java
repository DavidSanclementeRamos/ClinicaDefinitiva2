package com.example.ClinicaDefinitiva.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/*Esta clase intercepta cada petición que llega a tu servidor y le asigna un ID único (requestId), útil para trazabilidad en logs, errores y respuestas. Ese ID:
- Se genera automáticamente con UUID.randomUUID().
- Se guarda en un ThreadLocal para que cualquier clase en la misma solicitud pueda accederlo.
- Se adjunta como encabezado HTTP (X-Request-ID) en la respuesta (opcional).
- Se elimina al final para evitar fuga de datos entre hilos.

🧩 Explicación por partes
✅ @Component
Registra la clase automáticamente como bean en el contexto de Spring, para que el filtro sea detectado y ejecutado.

✅ extends OncePerRequestFilter
Este filtro asegura que se ejecuta una sola vez por petición, incluso si hay múltiples redirecciones internas.

✅ public static final String REQUEST_ID_HEADER
Define el nombre del encabezado HTTP que usará para exponer el ID a clientes (ej.: X-Request-ID).

✅ private static final ThreadLocal<String> requestIdHolder
Un ThreadLocal garantiza que el ID generado para una petición esté disponible solo dentro del hilo que la maneja. Es seguro y aislado por hilo.

✅ getRequestId()
Método público estático que permite recuperar el ID actual de la petición desde cualquier clase Java:
String id = RequestIdFilter.getRequestId();


Ideal para incluirlo en logs, respuestas personalizadas, excepciones, etc.

✅ doFilterInternal(...)
Este es el núcleo del filtro:
String requestId = UUID.randomUUID().toString(); // Genera un ID único
requestIdHolder.set(requestId);                  // Lo guarda por hilo
response.setHeader(REQUEST_ID_HEADER, requestId);// Lo envía en la respuesta


Luego deja continuar la ejecución normal:
filterChain.doFilter(request, response);

Y al final:
requestIdHolder.remove(); // Limpieza para evitar que otros hilos lo vean

📍 ¿Para qué sirve esto en la práctica?
- 🔎 Trazabilidad: Si ocurre un error, puedes identificar la solicitud por su requestId en tus logs.
- 🧠 Depuración: En sistemas con muchas peticiones concurrentes, saber qué error pertenece a qué solicitud es vital.
- 📤 Exposición al cliente: Si incluyes el requestId en las respuestas, el frontend puede reportar errores con más contexto.
*/
@Component
public class RequestIdFilter extends OncePerRequestFilter {
    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();

    public static String getRequestId() {
        return requestIdHolder.get();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        requestIdHolder.set(requestId);

        response.setHeader(REQUEST_ID_HEADER, requestId); // opcional para frontend
        try {
            filterChain.doFilter(request, response);
        } finally {
            requestIdHolder.remove();
        }
    }
}
