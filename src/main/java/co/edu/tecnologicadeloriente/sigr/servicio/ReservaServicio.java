package co.edu.tecnologicadeloriente.sigr.servicio;

import co.edu.tecnologicadeloriente.sigr.modelo.Reserva;
import co.edu.tecnologicadeloriente.sigr.repositorio.ReservaRepositorio;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaServicio {
    public static final LocalTime APERTURA = LocalTime.of(12, 0);
    public static final LocalTime ULTIMA_RESERVA = LocalTime.of(22, 0);
    public static final int MAX_PERSONAS = 12;

    private final ReservaRepositorio reservas;
    private final Clock reloj;

    public ReservaServicio(ReservaRepositorio reservas, Clock reloj) {
        this.reservas = reservas; this.reloj = reloj;
    }

    public static final int MAX_NOTAS = 200;

    public Reserva crear(String cliente, String telefono, LocalDate fecha, LocalTime hora, int personas) {
        return crear(cliente, telefono, fecha, hora, personas, null);
    }

    public Reserva crear(String cliente, String telefono, LocalDate fecha, LocalTime hora, int personas, String notas) {
        if (notas != null && notas.length() > MAX_NOTAS)
            throw new ReglaNegocioException("Las notas admiten máximo " + MAX_NOTAS + " caracteres");
        if (cliente == null || cliente.isBlank()) throw new ReglaNegocioException("El nombre del cliente es obligatorio");
        if (telefono == null || !telefono.matches("\\d{10}")) throw new ReglaNegocioException("El teléfono debe tener 10 dígitos");
        if (fecha == null || hora == null) throw new ReglaNegocioException("La fecha y la hora son obligatorias");
        if (fecha.isBefore(LocalDate.now(reloj))) throw new ReglaNegocioException("No se puede reservar en una fecha pasada");
        if (hora.isBefore(APERTURA) || hora.isAfter(ULTIMA_RESERVA))
            throw new ReglaNegocioException("El horario de reservas es de 12:00 a 22:00");
        if (personas < 1 || personas > MAX_PERSONAS)
            throw new ReglaNegocioException("Las reservas son para 1 a " + MAX_PERSONAS + " personas");
        Reserva reserva = new Reserva(cliente.trim(), telefono, fecha, hora, personas);
        reserva.setNotas(notas == null || notas.isBlank() ? null : notas.trim());
        return reservas.save(reserva);
    }

    public List<Reserva> listar() { return reservas.findAll(); }
}
