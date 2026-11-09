package co.edu.tecnologicadeloriente.sigr.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DisplayName("API del menú digital (CRUD de platos)")
class MenuApiTest {
    @Autowired TestRestTemplate http;

    @Test
    @DisplayName("Crear, consultar y eliminar un plato")
    void crudCompleto() {
        ResponseEntity<Map> creado = http.postForEntity("/api/platos",
                Map.of("nombre", "Sancocho de gallina", "precio", 30000, "categoriaId", 2), Map.class);
        assertEquals(HttpStatus.CREATED, creado.getStatusCode());
        Object id = creado.getBody().get("id");

        assertEquals("Sancocho de gallina", http.getForObject("/api/platos/" + id, Map.class).get("nombre"));

        assertEquals(HttpStatus.NO_CONTENT,
                http.exchange("/api/platos/" + id, HttpMethod.DELETE, null, Void.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, http.getForEntity("/api/platos/" + id, Map.class).getStatusCode());
    }

    @Test
    @DisplayName("Datos inválidos responden 400 con todos los errores")
    void validacion() {
        ResponseEntity<Map> r = http.postForEntity("/api/platos", Map.of("nombre", "", "precio", 500), Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        List<?> errores = (List<?>) r.getBody().get("errores");
        assertEquals(3, errores.size());
    }

    @Test
    @DisplayName("Marcar un plato como no disponible")
    void cambiarDisponibilidad() {
        ResponseEntity<Map> r = http.exchange("/api/platos/2", HttpMethod.PATCH,
                new HttpEntity<>(Map.of("disponible", false)), Map.class);
        assertEquals(false, r.getBody().get("disponible"));
        http.exchange("/api/platos/2", HttpMethod.PATCH, new HttpEntity<>(Map.of("disponible", true)), Map.class);
    }
}
