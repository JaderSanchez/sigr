package co.edu.tecnologicadeloriente.sigr.unitarias;

import co.edu.tecnologicadeloriente.sigr.modelo.Reserva;
import co.edu.tecnologicadeloriente.sigr.repositorio.ReservaRepositorio;
import co.edu.tecnologicadeloriente.sigr.servicio.ReglaNegocioException;
import co.edu.tecnologicadeloriente.sigr.servicio.ReservaServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Reglas de negocio de las reservas")
class ReservaServicioTest {
    /** Reloj fijo: la prueba no depende del día en que se ejecute. */
    private static final Clock HOY = Clock.fixed(Instant.parse("2026-11-09T15:00:00Z"), ZoneId.of("America/Bogota"));
    private static final LocalDate MANANA = LocalDate.of(2026, 11, 10);

    private ReservaRepositorio repositorio;
    private ReservaServicio servicio;

    @BeforeEach
    void preparar() {
        repositorio = mock(ReservaRepositorio.class);
        when(repositorio.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
        servicio = new ReservaServicio(repositorio, HOY);
    }

    private Reserva reservar(LocalTime hora, int personas) {
        return servicio.crear("Laura Gómez", "3155551212", MANANA, hora, personas);
    }

    @Test
    @DisplayName("Una reserva válida se guarda")
    void reservaValida() {
        Reserva r = reservar(LocalTime.of(19, 30), 4);
        assertEquals("Laura Gómez", r.getCliente());
        verify(repositorio).save(any(Reserva.class));
    }

    @ParameterizedTest(name = "hora límite permitida {0}")
    @ValueSource(strings = {"12:00", "22:00"})
    void horasLimitePermitidas(String hora) {
        assertDoesNotThrow(() -> reservar(LocalTime.parse(hora), 2));
    }

    @ParameterizedTest(name = "hora fuera de horario {0}")
    @ValueSource(strings = {"11:59", "22:01"})
    void horasFueraDeHorario(String hora) {
        var e = assertThrows(ReglaNegocioException.class, () -> reservar(LocalTime.parse(hora), 2));
        assertEquals("El horario de reservas es de 12:00 a 22:00", e.getMessage());
        verify(repositorio, never()).save(any());
    }

    @ParameterizedTest(name = "personas válidas {0}")
    @ValueSource(ints = {1, 12})
    void personasEnElLimite(int personas) {
        assertDoesNotThrow(() -> reservar(LocalTime.NOON, personas));
    }

    @ParameterizedTest(name = "personas inválidas {0}")
    @ValueSource(ints = {0, 13})
    void personasFueraDeRango(int personas) {
        assertThrows(ReglaNegocioException.class, () -> reservar(LocalTime.NOON, personas));
    }

    @Test
    @DisplayName("No se puede reservar en una fecha pasada")
    void fechaPasada() {
        var e = assertThrows(ReglaNegocioException.class,
                () -> servicio.crear("Laura", "3155551212", LocalDate.of(2026, 11, 8), LocalTime.NOON, 2));
        assertEquals("No se puede reservar en una fecha pasada", e.getMessage());
    }

    @ParameterizedTest(name = "teléfono inválido {0}")
    @ValueSource(strings = {"315555121", "31555512123", "315555121A", ""})
    void telefonoInvalido(String telefono) {
        assertThrows(ReglaNegocioException.class,
                () -> servicio.crear("Laura", telefono, MANANA, LocalTime.NOON, 2));
    }
}
