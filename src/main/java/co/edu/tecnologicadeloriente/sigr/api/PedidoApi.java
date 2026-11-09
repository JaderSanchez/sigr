package co.edu.tecnologicadeloriente.sigr.api;

import co.edu.tecnologicadeloriente.sigr.modelo.EstadoPedido;
import co.edu.tecnologicadeloriente.sigr.modelo.Pedido;
import co.edu.tecnologicadeloriente.sigr.servicio.PedidoServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PedidoApi {
    private final PedidoServicio pedidos;
    public PedidoApi(PedidoServicio pedidos) { this.pedidos = pedidos; }

    public record NuevoPedido(@NotNull(message = "es obligatoria") Integer mesa,
                              @NotEmpty(message = "debe tener al menos un plato") Map<Long, Integer> platos) {}

    public record CambioEstado(@NotNull(message = "es obligatorio") EstadoPedido estado) {}

    @PostMapping("/pedidos") @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(@Valid @RequestBody NuevoPedido p) { return pedidos.crear(p.mesa(), p.platos()); }

    @GetMapping("/pedidos") public List<Pedido> listar() { return pedidos.listar(); }
    @GetMapping("/pedidos/{id}") public Pedido buscar(@PathVariable Long id) { return pedidos.buscar(id); }

    @PatchMapping("/pedidos/{id}/estado")
    public Pedido cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstado c) {
        return pedidos.cambiarEstado(id, c.estado());
    }

    @GetMapping("/caja/cierre")
    public PedidoServicio.CierreCaja cierre(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return pedidos.cierre(fecha);
    }
}
