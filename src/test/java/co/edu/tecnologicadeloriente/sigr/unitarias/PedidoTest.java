package co.edu.tecnologicadeloriente.sigr.unitarias;

import co.edu.tecnologicadeloriente.sigr.modelo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cálculos y estados de un pedido")
class PedidoTest {
    private final Categoria fuertes = new Categoria("Platos fuertes");

    @Test
    @DisplayName("El total suma precio por cantidad de cada ítem")
    void totalSumaLosSubtotales() {
        Pedido pedido = new Pedido(4);
        pedido.agregar(new ItemPedido(new Plato("Bandeja paisa", new BigDecimal("35000"), fuertes), 2));
        pedido.agregar(new ItemPedido(new Plato("Limonada", new BigDecimal("8000"), fuertes), 3));
        assertEquals(0, new BigDecimal("94000").compareTo(pedido.getTotal()));
    }

    @Test
    @DisplayName("Cambiar el precio del menú no altera un pedido ya hecho")
    void elPrecioQuedaCongelado() {
        Plato ajiaco = new Plato("Ajiaco", new BigDecimal("32000"), fuertes);
        Pedido pedido = new Pedido(2);
        pedido.agregar(new ItemPedido(ajiaco, 1));
        ajiaco.setPrecio(new BigDecimal("40000"));
        assertEquals(0, new BigDecimal("32000").compareTo(pedido.getTotal()));
    }

    @Test
    @DisplayName("Saltarse un estado lanza error y no cambia el pedido")
    void noPermiteSaltarEstados() {
        Pedido pedido = new Pedido(1);
        assertThrows(IllegalStateException.class, () -> pedido.avanzarA(EstadoPedido.PAGADO));
        assertEquals(EstadoPedido.RECIBIDO, pedido.getEstado());
    }
}
