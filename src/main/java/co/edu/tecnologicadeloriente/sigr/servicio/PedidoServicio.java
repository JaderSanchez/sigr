package co.edu.tecnologicadeloriente.sigr.servicio;

import co.edu.tecnologicadeloriente.sigr.modelo.*;
import co.edu.tecnologicadeloriente.sigr.repositorio.PedidoRepositorio;
import co.edu.tecnologicadeloriente.sigr.repositorio.PlatoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PedidoServicio {
    private final PedidoRepositorio pedidos;
    private final PlatoRepositorio platos;

    public PedidoServicio(PedidoRepositorio pedidos, PlatoRepositorio platos) {
        this.pedidos = pedidos; this.platos = platos;
    }

    /** Crea un pedido a partir de un mapa platoId -> cantidad. */
    @Transactional
    public Pedido crear(int mesa, Map<Long, Integer> lineas) {
        if (mesa < 1 || mesa > 30) throw new ReglaNegocioException("La mesa debe estar entre 1 y 30");
        if (lineas == null || lineas.isEmpty()) throw new ReglaNegocioException("El pedido debe tener al menos un plato");
        Pedido pedido = new Pedido(mesa);
        lineas.forEach((platoId, cantidad) -> {
            Plato plato = platos.findById(platoId)
                    .orElseThrow(() -> new NoEncontradoException("No existe el plato " + platoId));
            if (!plato.isDisponible()) throw new ReglaNegocioException("El plato " + plato.getNombre() + " no está disponible");
            if (cantidad == null || cantidad < 1) throw new ReglaNegocioException("La cantidad debe ser al menos 1");
            pedido.agregar(new ItemPedido(plato, cantidad));
        });
        return pedidos.save(pedido);
    }

    public Pedido buscar(Long id) {
        return pedidos.findById(id).orElseThrow(() -> new NoEncontradoException("No existe el pedido " + id));
    }

    public List<Pedido> listar() { return pedidos.findAll(); }

    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido nuevo) {
        Pedido pedido = buscar(id);
        try {
            pedido.avanzarA(nuevo);
        } catch (IllegalStateException e) {
            throw new ReglaNegocioException(e.getMessage());
        }
        return pedidos.save(pedido);
    }

    /** Cierre de caja: suma lo pagado en el día. */
    public CierreCaja cierre(LocalDate fecha) {
        List<Pedido> pagados = pedidos.findByEstadoAndCreadoEnBetween(
                EstadoPedido.PAGADO, fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay());
        BigDecimal total = pagados.stream().map(Pedido::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CierreCaja(fecha, pagados.size(), total);
    }

    public record CierreCaja(LocalDate fecha, int pedidosPagados, BigDecimal totalVentas) {}
}
