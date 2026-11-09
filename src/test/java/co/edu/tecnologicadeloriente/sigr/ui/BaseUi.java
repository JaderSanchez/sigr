package co.edu.tecnologicadeloriente.sigr.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.nio.file.*;

/** Arranca la aplicación en un puerto libre y un Chrome sin ventana para cada prueba. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseUi {
    @LocalServerPort int puerto;
    protected WebDriver driver;

    protected String baseUrl() { return "http://localhost:" + puerto; }

    @BeforeEach
    void abrirNavegador() {
        ChromeOptions opciones = new ChromeOptions();
        opciones.addArguments("--headless=new", "--window-size=1200,900", "--lang=es-CO");
        driver = new ChromeDriver(opciones); // Selenium Manager descarga el chromedriver correcto
    }

    /** Guarda una captura por prueba en target/evidencias, incluso si la prueba falla. */
    @AfterEach
    void cerrarNavegador(TestInfo info) throws Exception {
        Path carpeta = Paths.get("target", "evidencias");
        Files.createDirectories(carpeta);
        String nombre = info.getTestClass().orElseThrow().getSimpleName() + "_" + info.getTestMethod().orElseThrow().getName() + ".png";
        Files.write(carpeta.resolve(nombre), ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        driver.quit();
    }
}
