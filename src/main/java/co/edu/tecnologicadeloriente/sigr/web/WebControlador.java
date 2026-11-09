package co.edu.tecnologicadeloriente.sigr.web;

import co.edu.tecnologicadeloriente.sigr.modelo.Usuario;
import co.edu.tecnologicadeloriente.sigr.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
public class WebControlador {
    static final String SESION = "usuarioActual";

    private final AutenticacionServicio auth;
    private final MenuServicio menu;
    private final PedidoServicio pedidos;
    private final ReservaServicio reservas;

    public WebControlador(AutenticacionServicio auth, MenuServicio menu, PedidoServicio pedidos, ReservaServicio reservas) {
        this.auth = auth; this.menu = menu; this.pedidos = pedidos; this.reservas = reservas;
    }

    @GetMapping("/") public String inicio() { return "redirect:/menu"; }

    @GetMapping("/login") public String login() { return "login"; }

    @PostMapping("/login")
    public String entrar(@RequestParam(required = false) String usuario, @RequestParam(required = false) String clave,
                         HttpSession sesion, Model model) {
        Optional<Usuario> u = auth.autenticar(usuario, clave);
        if (u.isEmpty()) {
            model.addAttribute("error", "Usuario o clave incorrectos");
            model.addAttribute("usuario", usuario);
            return "login";
        }
        sesion.setAttribute(SESION, u.get().getUsuario() + " (" + u.get().getRol() + ")");
        return "redirect:/menu";
    }

    @PostMapping("/logout")
    public String salir(HttpSession sesion) { sesion.invalidate(); return "redirect:/login"; }

    @GetMapping("/menu")
    public String verMenu(Model model) { model.addAttribute("platos", menu.platos()); return "menu"; }

    @GetMapping("/pedidos")
    public String verPedidos(Model model) { model.addAttribute("pedidos", pedidos.listar()); return "pedidos"; }

    @GetMapping("/caja")
    public String verCaja(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, Model model) {
        LocalDate dia = fecha != null ? fecha : LocalDate.now();
        model.addAttribute("cierre", pedidos.cierre(dia));
        return "caja";
    }

    @GetMapping("/reservas/nueva")
    public String formularioReserva() { return "reserva"; }

    @PostMapping("/reservas/nueva")
    public String reservar(@RequestParam(required = false) String cliente, @RequestParam(required = false) String telefono,
                           @RequestParam(required = false) String fecha, @RequestParam(required = false) String hora,
                           @RequestParam(required = false) Integer personas, Model model) {
        model.addAttribute("cliente", cliente); model.addAttribute("telefono", telefono);
        model.addAttribute("fecha", fecha); model.addAttribute("hora", hora); model.addAttribute("personas", personas);
        try {
            var r = reservas.crear(cliente, telefono,
                    fecha == null || fecha.isBlank() ? null : LocalDate.parse(fecha),
                    hora == null || hora.isBlank() ? null : LocalTime.parse(hora),
                    personas == null ? 0 : personas);
            model.addAttribute("exito", "Reserva confirmada con el código R-" + r.getId()
                    + " para " + r.getPersonas() + " personas el " + r.getFecha() + " a las " + r.getHora());
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "reserva";
    }
}
