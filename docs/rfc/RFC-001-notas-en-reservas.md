# RFC-001: Notas en las reservas

| Campo | Valor |
|---|---|
| Fecha de solicitud | 09/11/2026 |
| Solicitante | Administración del restaurante |
| Tipo de cambio | Evolutivo (nueva funcionalidad sobre la línea base 1.0.0) |
| Prioridad | Media |
| Estado | Aprobado |

## Descripción

Los clientes avisan por teléfono si alguien tiene una alergia o si la reserva es para una celebración, y hoy esa información se pierde porque el formulario no tiene dónde guardarla. Se solicita un campo opcional de notas en la reserva.

## Evaluación de impacto

| Dimensión | Impacto |
|---|---|
| Técnico | Bajo. Una columna nueva en `reserva`, un campo en la API y otro en el formulario. No toca pedidos, menú ni caja. |
| Funcional | Positivo. El personal sabe con anticipación de alergias y ocasiones especiales. |
| Económico | Bajo. Menos de un día de trabajo. |
| Riesgo | Bajo. El campo es opcional, así que las reservas existentes y los clientes de la API siguen funcionando sin cambios. |

## Criterios de aceptación

- El campo es opcional y admite hasta 200 caracteres.
- Una nota de más de 200 caracteres se rechaza con un mensaje claro.
- Todas las pruebas de la línea base siguen pasando.

## Decisión

Aprobado por el coordinador del equipo. Se implementa en la rama `feature/rfc-001-notas-reserva` y se integra a `main` con merge `--no-ff` después de pasar las pruebas.
