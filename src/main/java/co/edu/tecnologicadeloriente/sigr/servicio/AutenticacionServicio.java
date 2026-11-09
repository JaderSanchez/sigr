package co.edu.tecnologicadeloriente.sigr.servicio;

import co.edu.tecnologicadeloriente.sigr.modelo.Usuario;
import co.edu.tecnologicadeloriente.sigr.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class AutenticacionServicio {
    private final UsuarioRepositorio usuarios;

    public AutenticacionServicio(UsuarioRepositorio usuarios) { this.usuarios = usuarios; }

    public static String hash(String clave) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(clave.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Devuelve el usuario si las credenciales coinciden; vacío en cualquier otro caso. */
    public Optional<Usuario> autenticar(String usuario, String clave) {
        if (usuario == null || clave == null || usuario.isBlank() || clave.isBlank()) return Optional.empty();
        return usuarios.findByUsuario(usuario.trim())
                .filter(u -> u.getClaveHash().equals(hash(clave)));
    }
}
