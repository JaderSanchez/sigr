package co.edu.tecnologicadeloriente.sigr.api;

import co.edu.tecnologicadeloriente.sigr.modelo.Reserva;
import co.edu.tecnologicadeloriente.sigr.servicio.ReservaServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApi {
    private final ReservaServicio reservas;
    public ReservaApi(ReservaServicio reservas) { this.reservas = reservas; }

    public record NuevaReserva(@NotBlank(message = "es obligatorio") String cliente,
                               @NotBlank(message = "es obligatorio") String telefono,
                               @NotNull(message = "es obligatoria") LocalDate fecha,
                               @NotNull(message = "es obligatoria") LocalTime hora,
                               @NotNull(message = "es obligatorio") Integer personas,
                               String notas) {}

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Reserva crear(@Valid @RequestBody NuevaReserva r) {
        return reservas.crear(r.cliente(), r.telefono(), r.fecha(), r.hora(), r.personas(), r.notas());
    }

    @GetMapping public List<Reserva> listar() { return reservas.listar(); }
}
