package co.edu.tecnologicadeloriente.sigr.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DisplayName("API de autenticación")
class AutenticacionApiTest {
    @Autowired TestRestTemplate http;

    @Test
    @DisplayName("Credenciales correctas devuelven el rol del usuario")
    void loginCorrecto() {
        ResponseEntity<Map> r = http.postForEntity("/api/auth/login",
                Map.of("usuario", "admin", "clave", "Admin2026*"), Map.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals("ADMINISTRADOR", r.getBody().get("rol"));
    }

    @Test
    @DisplayName("Clave incorrecta responde 401")
    void claveIncorrecta() {
        ResponseEntity<Map> r = http.postForEntity("/api/auth/login",
                Map.of("usuario", "admin", "clave", "otra"), Map.class);
        assertEquals(HttpStatus.UNAUTHORIZED, r.getStatusCode());
    }

    @Test
    @DisplayName("Campos vacíos responden 400 con el detalle")
    void camposVacios() {
        ResponseEntity<Map> r = http.postForEntity("/api/auth/login", Map.of("usuario", "", "clave", ""), Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        assertTrue(r.getBody().get("errores").toString().contains("usuario: es obligatorio"));
    }
}
