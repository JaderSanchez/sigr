package co.edu.tecnologicadeloriente.sigr.servicio;

import co.edu.tecnologicadeloriente.sigr.modelo.Categoria;
import co.edu.tecnologicadeloriente.sigr.modelo.Plato;
import co.edu.tecnologicadeloriente.sigr.repositorio.CategoriaRepositorio;
import co.edu.tecnologicadeloriente.sigr.repositorio.PlatoRepositorio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuServicio {
    private final PlatoRepositorio platos;
    private final CategoriaRepositorio categorias;

    public MenuServicio(PlatoRepositorio platos, CategoriaRepositorio categorias) {
        this.platos = platos; this.categorias = categorias;
    }

    public List<Plato> platos() { return platos.findAll(); }
    public List<Categoria> categorias() { return categorias.findAll(); }

    public Plato plato(Long id) {
        return platos.findById(id).orElseThrow(() -> new NoEncontradoException("No existe el plato " + id));
    }

    public Plato crear(String nombre, BigDecimal precio, Long categoriaId) {
        Categoria c = categorias.findById(categoriaId)
                .orElseThrow(() -> new NoEncontradoException("No existe la categoría " + categoriaId));
        return platos.save(new Plato(nombre, precio, c));
    }

    public Plato actualizar(Long id, String nombre, BigDecimal precio, Boolean disponible) {
        Plato p = plato(id);
        if (nombre != null) p.setNombre(nombre);
        if (precio != null) p.setPrecio(precio);
        if (disponible != null) p.setDisponible(disponible);
        return platos.save(p);
    }

    public void eliminar(Long id) { platos.delete(plato(id)); }
}
