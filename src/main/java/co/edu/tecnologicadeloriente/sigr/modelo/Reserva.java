package co.edu.tecnologicadeloriente.sigr.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private String telefono;
    private LocalDate fecha;
    private LocalTime hora;
    private int personas;

    protected Reserva() {}

    public Reserva(String cliente, String telefono, LocalDate fecha, LocalTime hora, int personas) {
        this.cliente = cliente; this.telefono = telefono; this.fecha = fecha; this.hora = hora; this.personas = personas;
    }

    public Long getId() { return id; }
    public String getCliente() { return cliente; }
    public String getTelefono() { return telefono; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public int getPersonas() { return personas; }
}
