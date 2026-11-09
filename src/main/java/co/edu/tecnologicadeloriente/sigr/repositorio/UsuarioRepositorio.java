package co.edu.tecnologicadeloriente.sigr.repositorio;

import co.edu.tecnologicadeloriente.sigr.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
}
