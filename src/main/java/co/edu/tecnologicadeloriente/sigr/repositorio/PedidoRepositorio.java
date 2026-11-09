package co.edu.tecnologicadeloriente.sigr.repositorio;

import co.edu.tecnologicadeloriente.sigr.modelo.EstadoPedido;
import co.edu.tecnologicadeloriente.sigr.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstadoAndCreadoEnBetween(EstadoPedido estado, LocalDateTime desde, LocalDateTime hasta);
}
