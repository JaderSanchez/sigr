package co.edu.tecnologicadeloriente.sigr.ui;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Page Object del formulario de reservas: concentra los localizadores y las acciones
 * para que las pruebas no dependan de cómo está maquetada la página.
 */
public class PaginaReserva {
    private final WebDriver driver;
    private final WebDriverWait espera;
    private final String url;

    public PaginaReserva(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.url = baseUrl + "/reservas/nueva";
    }

    public PaginaReserva abrir() {
        driver.get(url);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-reserva")));
        return this;
    }

    public PaginaReserva llenar(String cliente, String telefono, LocalDate fecha, LocalTime hora, Integer personas) {
        escribir("cliente", cliente);
        escribir("telefono", telefono);
        // Los campos date y time dependen del idioma del navegador; se asigna el valor ISO directamente.
        asignar("fecha", fecha == null ? "" : fecha.toString());
        asignar("hora", hora == null ? "" : hora.toString());
        escribir("personas", personas == null ? "" : personas.toString());
        return this;
    }

    public PaginaReserva enviar() {
        driver.findElement(By.id("btn-reservar")).click();
        espera.until(d -> !d.findElements(By.id("mensaje-exito")).isEmpty() || !d.findElements(By.id("mensaje-error")).isEmpty());
        return this;
    }

    public String mensajeExito() { return texto("mensaje-exito"); }
    public String mensajeError() { return texto("mensaje-error"); }

    private String texto(String id) {
        var e = driver.findElements(By.id(id));
        return e.isEmpty() ? null : e.get(0).getText();
    }

    private void escribir(String id, String valor) {
        WebElement campo = driver.findElement(By.id(id));
        campo.clear();
        if (valor != null) campo.sendKeys(valor);
    }

    private void asignar(String id, String valor) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", driver.findElement(By.id(id)), valor);
    }
}
