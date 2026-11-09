package co.edu.tecnologicadeloriente.sigr.api;

import co.edu.tecnologicadeloriente.sigr.servicio.NoEncontradoException;
import co.edu.tecnologicadeloriente.sigr.servicio.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

/** Traduce las excepciones a respuestas JSON con un mensaje entendible. */
@RestControllerAdvice(basePackages = "co.edu.tecnologicadeloriente.sigr.api")
public class ManejadorErrores {

    @ExceptionHandler(ReglaNegocioException.class)
    ResponseEntity<Map<String, Object>> negocio(ReglaNegocioException e) {
        return cuerpo(HttpStatus.UNPROCESSABLE_CONTENT, List.of(e.getMessage()));
    }

    @ExceptionHandler(NoEncontradoException.class)
    ResponseEntity<Map<String, Object>> noEncontrado(NoEncontradoException e) {
        return cuerpo(HttpStatus.NOT_FOUND, List.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException e) {
        List<String> errores = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage()).sorted().toList();
        return cuerpo(HttpStatus.BAD_REQUEST, errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, Object>> malFormado(HttpMessageNotReadableException e) {
        return cuerpo(HttpStatus.BAD_REQUEST, List.of("El cuerpo de la petición no es un JSON válido"));
    }

    private ResponseEntity<Map<String, Object>> cuerpo(HttpStatus estado, List<String> errores) {
        return ResponseEntity.status(estado).body(Map.of("estado", estado.value(), "errores", errores));
    }
}
