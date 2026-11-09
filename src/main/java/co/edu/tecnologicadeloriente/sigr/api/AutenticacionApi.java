package co.edu.tecnologicadeloriente.sigr.api;

import co.edu.tecnologicadeloriente.sigr.servicio.AutenticacionServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionApi {
    private final AutenticacionServicio auth;
    public AutenticacionApi(AutenticacionServicio auth) { this.auth = auth; }

    public record Credenciales(@NotBlank(message = "es obligatorio") String usuario,
                               @NotBlank(message = "es obligatoria") String clave) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Credenciales c) {
        return auth.autenticar(c.usuario(), c.clave())
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of("usuario", u.getUsuario(), "rol", u.getRol())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("estado", 401, "errores", java.util.List.of("Usuario o clave incorrectos"))));
    }
}
