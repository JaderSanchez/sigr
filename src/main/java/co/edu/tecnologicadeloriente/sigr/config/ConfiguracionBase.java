package co.edu.tecnologicadeloriente.sigr.config;

import co.edu.tecnologicadeloriente.sigr.modelo.*;
import co.edu.tecnologicadeloriente.sigr.repositorio.*;
import co.edu.tecnologicadeloriente.sigr.servicio.AutenticacionServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ConfiguracionBase {

    @Bean
    public Clock reloj() { return Clock.system(ZoneId.of("America/Bogota")); }

    /** Datos iniciales: usuarios de cada rol y un menú de ejemplo. */
    @Bean
    CommandLineRunner datosIniciales(UsuarioRepositorio usuarios, CategoriaRepositorio categorias, PlatoRepositorio platos) {
        return args -> {
            if (usuarios.count() > 0) return;
            usuarios.save(new Usuario("admin", AutenticacionServicio.hash("Admin2026*"), Rol.ADMINISTRADOR));
            usuarios.save(new Usuario("mesero1", AutenticacionServicio.hash("Mesero2026*"), Rol.MESERO));
            usuarios.save(new Usuario("cliente1", AutenticacionServicio.hash("Cliente2026*"), Rol.CLIENTE));
            Categoria entradas = categorias.save(new Categoria("Entradas"));
            Categoria fuertes = categorias.save(new Categoria("Platos fuertes"));
            Categoria bebidas = categorias.save(new Categoria("Bebidas"));
            platos.save(new Plato("Empanadas x3", new BigDecimal("9000"), entradas));
            platos.save(new Plato("Patacones con hogao", new BigDecimal("12000"), entradas));
            platos.save(new Plato("Bandeja paisa", new BigDecimal("35000"), fuertes));
            platos.save(new Plato("Ajiaco santafereño", new BigDecimal("32000"), fuertes));
            platos.save(new Plato("Limonada de coco", new BigDecimal("8000"), bebidas));
        };
    }
}
