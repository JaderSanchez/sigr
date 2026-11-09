# Historial de cambios

Formato basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y versionado semántico.

## [Sin publicar]

### Agregado
- Campo opcional de notas en las reservas, hasta 200 caracteres, para alergias u ocasiones especiales (RFC-001).

## [1.0.0] - 2026-11-09 (línea base)

### Agregado
- Estructura del proyecto Spring Boot 4 con Maven.
- Modelo de datos: usuarios, categorías, platos, pedidos con sus ítems y reservas.
- Módulo de autenticación con tres roles y claves almacenadas como hash SHA-256.
- Módulo de menú digital con CRUD de platos.
- Módulo de pedidos con cálculo de total y ciclo de estados controlado.
- Módulo de reservas con validación de horario (12:00 a 22:00), aforo (1 a 12 personas), fecha y teléfono.
- Módulo de cierre de caja con reporte de ventas diarias.
- Interfaz web con Thymeleaf y protección de pantallas internas por sesión.
- Manejo centralizado de errores con respuestas JSON (400, 401, 404 y 422).
- Pruebas automatizadas: unitarias, de integración con TestRestTemplate y de interfaz con Selenium.
- Colecciones de Postman y casos de reserva en CSV.
- Documentación: README, estructura de base de datos y manual de despliegue.
