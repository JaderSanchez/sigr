package co.edu.tecnologicadeloriente.sigr.api;

import co.edu.tecnologicadeloriente.sigr.modelo.Categoria;
import co.edu.tecnologicadeloriente.sigr.modelo.Plato;
import co.edu.tecnologicadeloriente.sigr.servicio.MenuServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuApi {
    private final MenuServicio menu;
    public MenuApi(MenuServicio menu) { this.menu = menu; }

    public record NuevoPlato(@NotBlank(message = "es obligatorio") String nombre,
                             @NotNull(message = "es obligatorio") @DecimalMin(value = "1000", message = "debe ser mínimo 1000") BigDecimal precio,
                             @NotNull(message = "es obligatoria") Long categoriaId) {}

    public record CambioPlato(String nombre, @DecimalMin(value = "1000", message = "debe ser mínimo 1000") BigDecimal precio, Boolean disponible) {}

    @GetMapping("/categorias") public List<Categoria> categorias() { return menu.categorias(); }
    @GetMapping("/platos") public List<Plato> platos() { return menu.platos(); }
    @GetMapping("/platos/{id}") public Plato plato(@PathVariable Long id) { return menu.plato(id); }

    @PostMapping("/platos") @ResponseStatus(HttpStatus.CREATED)
    public Plato crear(@Valid @RequestBody NuevoPlato p) { return menu.crear(p.nombre(), p.precio(), p.categoriaId()); }

    @PatchMapping("/platos/{id}")
    public Plato actualizar(@PathVariable Long id, @Valid @RequestBody CambioPlato c) {
        return menu.actualizar(id, c.nombre(), c.precio(), c.disponible());
    }

    @DeleteMapping("/platos/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { menu.eliminar(id); }
}
