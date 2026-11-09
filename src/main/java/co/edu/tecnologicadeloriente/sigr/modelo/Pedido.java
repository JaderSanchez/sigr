package co.edu.tecnologicadeloriente.sigr.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int mesa;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.RECIBIDO;

    private LocalDateTime creadoEn = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> items = new ArrayList<>();

    protected Pedido() {}
    public Pedido(int mesa) { this.mesa = mesa; }

    public void agregar(ItemPedido item) { items.add(item); }

    public BigDecimal getTotal() {
        return items.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void avanzarA(EstadoPedido siguiente) {
        if (!estado.puedePasarA(siguiente)) {
            throw new IllegalStateException("No se puede pasar de " + estado + " a " + siguiente);
        }
        this.estado = siguiente;
    }

    public Long getId() { return id; }
    public int getMesa() { return mesa; }
    public EstadoPedido getEstado() { return estado; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public List<ItemPedido> getItems() { return items; }
}
