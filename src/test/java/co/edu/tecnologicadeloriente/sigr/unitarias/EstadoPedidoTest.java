package co.edu.tecnologicadeloriente.sigr.unitarias;

import co.edu.tecnologicadeloriente.sigr.modelo.EstadoPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Transiciones de estado del pedido")
class EstadoPedidoTest {

    @ParameterizedTest(name = "{0} -> {1} permitido = {2}")
    @CsvSource({
            "RECIBIDO, EN_PREPARACION, true",
            "EN_PREPARACION, SERVIDO, true",
            "SERVIDO, PAGADO, true",
            "RECIBIDO, SERVIDO, false",
            "RECIBIDO, PAGADO, false",
            "PAGADO, RECIBIDO, false",
            "SERVIDO, EN_PREPARACION, false"
    })
    void soloSeAvanzaUnPasoAdelante(EstadoPedido desde, EstadoPedido hacia, boolean esperado) {
        assertEquals(esperado, desde.puedePasarA(hacia));
    }
}
