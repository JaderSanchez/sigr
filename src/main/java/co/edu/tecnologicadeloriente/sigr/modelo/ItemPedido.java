package co.edu.tecnologicadeloriente.sigr.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ItemPedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Plato plato;

    private int cantidad;

    /** Precio congelado al momento de pedir, para que un cambio de menú no altere pedidos viejos. */
    private BigDecimal precioUnitario;

    protected ItemPedido() {}

    public ItemPedido(Plato plato, int cantidad) {
        this.plato = plato; this.cantidad = cantidad; this.precioUnitario = plato.getPrecio();
    }

    public BigDecimal getSubtotal() { return precioUnitario.multiply(BigDecimal.valueOf(cantidad)); }
    public Long getId() { return id; }
    public Plato getPlato() { return plato; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
}
