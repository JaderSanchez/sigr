# SIGR: Sistema Integral de Gestión de Restaurante

Aplicación web para gestionar la operación de un restaurante: pedidos, reservas, menú digital, usuarios y cierre de caja. Proyecto académico de la asignatura Gestión del Software (Corporación Escuela Tecnológica del Oriente).

## Módulos de la versión 1.0.0 (línea base)

| Módulo | Qué hace | Interfaz |
|---|---|---|
| Autenticación | Ingreso con roles CLIENTE, MESERO y ADMINISTRADOR; claves guardadas como hash SHA-256 | `/login`, `POST /api/auth/login` |
| Menú digital | CRUD de platos y consulta de categorías | `/menu`, `/api/platos`, `/api/categorias` |
| Pedidos | Registro de pedidos por mesa y seguimiento de estado (RECIBIDO, EN_PREPARACION, SERVIDO, PAGADO) | `/pedidos`, `/api/pedidos` |
| Reservas | Reservas por fecha y hora con validaciones de horario, aforo y teléfono | `/reservas/nueva`, `/api/reservas` |
| Caja | Cierre de caja y reporte de ventas diarias | `/caja`, `GET /api/caja/cierre?fecha=` |

## Requisitos

- Java 21 o superior
- Maven 3.9 o superior (o el `mvnw` incluido)
- Google Chrome, solo para las pruebas de interfaz con Selenium

## Clonar y ejecutar

```bash
git clone <url-del-repositorio> sigr
cd sigr
./mvnw spring-boot:run
```

La aplicación queda en http://localhost:8080. Usuarios de prueba que crea la carga inicial:

| Usuario | Clave | Rol |
|---|---|---|
| admin | Admin2026* | ADMINISTRADOR |
| mesero1 | Mesero2026* | MESERO |
| cliente1 | Cliente2026* | CLIENTE |

## Pruebas

```bash
./mvnw test                 # unitarias, integración de API (TestRestTemplate) y UI (Selenium)
./mvnw test -Dgroups=ui     # solo las pruebas de interfaz
```

El informe de cobertura queda en `target/site/jacoco/index.html` y las capturas de Selenium en `target/evidencias/`.

Las colecciones de Postman están en `postman/`. Con la aplicación corriendo:

```bash
npx newman run postman/SIGR_API.postman_collection.json
npx newman run postman/SIGR_Reservas_DatosCSV.postman_collection.json -d postman/reservas_casos.csv
```

## Estructura de carpetas

```
src/main/java/.../sigr/
  modelo/        entidades JPA y reglas propias de cada entidad
  repositorio/   acceso a datos (Spring Data JPA)
  servicio/      reglas de negocio
  api/           controladores REST y manejo de errores
  web/           controladores de las pantallas (Thymeleaf)
  config/        configuración y datos iniciales
src/main/resources/templates/   vistas HTML
src/test/java/.../sigr/
  unitarias/     pruebas de reglas de negocio
  api/           pruebas de integración de la API
  ui/            pruebas de interfaz con Selenium (Page Object)
docs/            estructura de base de datos y manual de despliegue
postman/         colecciones y datos de prueba de la API
```

## Convenciones

- Código, nombres y mensajes en español.
- Una rama por cambio (`feature/...`, `fix/...`) que se integra a `main` con merge sin avance rápido (`--no-ff`).
- Mensajes de commit en imperativo y en español.
- Versionado semántico; cada versión estable se marca con una etiqueta `vX.Y.Z`.

## Licencia

MIT. Ver `LICENSE.txt`.
