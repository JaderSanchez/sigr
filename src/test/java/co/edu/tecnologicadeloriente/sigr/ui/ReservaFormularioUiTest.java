package co.edu.tecnologicadeloriente.sigr.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
@DisplayName("Formulario web de reservas (Selenium)")
class ReservaFormularioUiTest extends BaseUi {
    private static final LocalDate EN_UNA_SEMANA = LocalDate.now().plusDays(7);

    private PaginaReserva pagina() { return new PaginaReserva(driver, baseUrl()).abrir(); }

    @Test
    @DisplayName("Una reserva válida muestra la confirmación con su código")
    void reservaValida() {
        PaginaReserva p = pagina().llenar("Laura Gómez", "3155551212", EN_UNA_SEMANA, LocalTime.of(19, 30), 4).enviar();
        assertNotNull(p.mensajeExito(), "Debía aparecer el mensaje de éxito");
        assertTrue(p.mensajeExito().matches("Reserva confirmada con el código R-\\d+ para 4 personas.*"));
        assertNull(p.mensajeError());
    }

    @Test
    @DisplayName("Una hora fuera del horario muestra el error y conserva los datos")
    void horaFueraDeHorario() {
        PaginaReserva p = pagina().llenar("Andrés Pineda", "3009876543", EN_UNA_SEMANA, LocalTime.of(23, 0), 2).enviar();
        assertEquals("El horario de reservas es de 12:00 a 22:00", p.mensajeError());
        assertEquals("Andrés Pineda", driver.findElement(org.openqa.selenium.By.id("cliente")).getAttribute("value"));
    }

    @Test
    @DisplayName("Más de 12 personas se rechaza")
    void demasiadasPersonas() {
        PaginaReserva p = pagina().llenar("Marcela Ruiz", "3181234567", EN_UNA_SEMANA, LocalTime.of(13, 0), 13).enviar();
        assertEquals("Las reservas son para 1 a 12 personas", p.mensajeError());
    }

    @Test
    @DisplayName("Un teléfono con menos de 10 dígitos se rechaza")
    void telefonoInvalido() {
        PaginaReserva p = pagina().llenar("Julián Castro", "31244455", EN_UNA_SEMANA, LocalTime.of(20, 0), 3).enviar();
        assertEquals("El teléfono debe tener 10 dígitos", p.mensajeError());
    }

    @Test
    @DisplayName("El formulario vacío no se acepta")
    void formularioVacio() {
        PaginaReserva p = pagina().llenar("", "", null, null, null).enviar();
        assertEquals("El nombre del cliente es obligatorio", p.mensajeError());
    }
}
