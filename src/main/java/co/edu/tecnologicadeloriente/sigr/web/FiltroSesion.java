package co.edu.tecnologicadeloriente.sigr.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Las pantallas internas exigen sesión; el formulario de reservas es público para los clientes. */
@Configuration
public class FiltroSesion implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
                var s = req.getSession(false);
                if (s == null || s.getAttribute(WebControlador.SESION) == null) {
                    res.sendRedirect("/login");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/menu", "/pedidos", "/caja");
    }
}
