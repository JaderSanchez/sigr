package co.edu.tecnologicadeloriente.sigr.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DisplayName("API de pedidos y cierre de caja")
class PedidoApiTest {
    @Autowired TestRestTemplate http;

    /** Crea un pedido de 2 bandejas paisas (id 3) y 2 limonadas (id 5) y devuelve su id. */
    private long crearPedido() {
        ResponseEntity<Map> r = http.postForEntity("/api/pedidos",
                Map.of("mesa", 4, "platos", Map.of("3", 2, "5", 2)), Map.class);
        assertEquals(HttpStatus.CREATED, r.getStatusCode());
        return ((Number) r.getBody().get("id")).longValue();
    }

    private ResponseEntity<Map> cambiarEstado(long id, String estado) {
        return http.exchange("/api/pedidos/" + id + "/estado", HttpMethod.PATCH,
                new HttpEntity<>(Map.of("estado", estado)), Map.class);
    }

    @Test
    @DisplayName("Un pedido nuevo queda RECIBIDO y con el total correcto")
    void crearPedidoCalculaTotal() {
        Map pedido = http.getForObject("/api/pedidos/" + crearPedido(), Map.class);
        assertEquals("RECIBIDO", pedido.get("estado"));
        assertEquals(0, new BigDecimal("86000").compareTo(new BigDecimal(pedido.get("total").toString())));
    }

    @Test
    @DisplayName("El pedido recorre el ciclo completo hasta PAGADO")
    void cicloCompleto() {
        long id = crearPedido();
        for (String e : new String[]{"EN_PREPARACION", "SERVIDO", "PAGADO"}) {
            ResponseEntity<Map> r = cambiarEstado(id, e);
            assertEquals(HttpStatus.OK, r.getStatusCode());
            assertEquals(e, r.getBody().get("estado"));
        }
    }

    @Test
    @DisplayName("Saltarse un estado responde 422 con el motivo")
    void saltoDeEstado() {
        ResponseEntity<Map> r = cambiarEstado(crearPedido(), "PAGADO");
        assertEquals(422, r.getStatusCode().value(), "422 = regla de negocio incumplida");
        assertTrue(r.getBody().get("errores").toString().contains("No se puede pasar de RECIBIDO a PAGADO"));
    }

    @Test
    @DisplayName("Un plato inexistente responde 404")
    void platoInexistente() {
        ResponseEntity<Map> r = http.postForEntity("/api/pedidos", Map.of("mesa", 2, "platos", Map.of("999", 1)), Map.class);
        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
    }

    @Test
    @DisplayName("Una mesa fuera de rango responde 422")
    void mesaInvalida() {
        ResponseEntity<Map> r = http.postForEntity("/api/pedidos", Map.of("mesa", 0, "platos", Map.of("1", 1)), Map.class);
        assertEquals(422, r.getStatusCode().value(), "422 = regla de negocio incumplida");
    }

    @Test
    @DisplayName("El cierre de caja suma solo los pedidos pagados del día")
    void cierreDeCaja() {
        Map antes = http.getForObject("/api/caja/cierre?fecha=" + LocalDate.now(), Map.class);
        long pagado = crearPedido();
        cambiarEstado(pagado, "EN_PREPARACION"); cambiarEstado(pagado, "SERVIDO"); cambiarEstado(pagado, "PAGADO");
        crearPedido(); // este queda sin pagar y no debe sumar
        Map despues = http.getForObject("/api/caja/cierre?fecha=" + LocalDate.now(), Map.class);
        BigDecimal diferencia = new BigDecimal(despues.get("totalVentas").toString())
                .subtract(new BigDecimal(antes.get("totalVentas").toString()));
        assertEquals(0, new BigDecimal("86000").compareTo(diferencia));
    }
}
