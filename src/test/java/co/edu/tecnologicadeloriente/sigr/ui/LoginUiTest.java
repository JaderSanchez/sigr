package co.edu.tecnologicadeloriente.sigr.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@DisplayName("Formulario web de inicio de sesión (Selenium)")
class LoginUiTest extends BaseUi {

    private void ingresar(String usuario, String clave) {
        driver.get(baseUrl() + "/login");
        driver.findElement(By.id("usuario")).sendKeys(usuario);
        driver.findElement(By.id("clave")).sendKeys(clave);
        driver.findElement(By.id("btn-ingresar")).click();
    }

    @Test
    @DisplayName("Con credenciales correctas entra al menú digital")
    void loginCorrecto() {
        ingresar("mesero1", "Mesero2026*");
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("/menu"));
        assertEquals("Menú digital", driver.findElement(By.id("titulo-menu")).getText());
        assertTrue(driver.findElements(By.cssSelector("#tabla-menu tr")).size() > 1, "El menú debe listar platos");
    }

    @Test
    @DisplayName("Con clave incorrecta se queda en el login con el aviso")
    void loginIncorrecto() {
        ingresar("mesero1", "clave-equivocada");
        // Espera explícita: el aviso aparece solo cuando el servidor responde y la página se recarga.
        var aviso = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-error")));
        assertEquals("Usuario o clave incorrectos", aviso.getText());
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
    }
}
