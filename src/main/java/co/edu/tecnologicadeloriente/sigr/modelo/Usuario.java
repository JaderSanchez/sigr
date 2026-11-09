package co.edu.tecnologicadeloriente.sigr.modelo;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String usuario;

    /** Hash SHA-256 de la clave; nunca se guarda la clave en texto plano. */
    @Column(nullable = false)
    private String claveHash;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    protected Usuario() {}

    public Usuario(String usuario, String claveHash, Rol rol) {
        this.usuario = usuario; this.claveHash = claveHash; this.rol = rol;
    }

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getClaveHash() { return claveHash; }
    public Rol getRol() { return rol; }
}
