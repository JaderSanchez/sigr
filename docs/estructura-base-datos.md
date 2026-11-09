# Estructura de la base de datos (versión 1.0.0)

Base de datos H2 en memoria. Hibernate crea las tablas al arrancar a partir de las entidades JPA.

| Tabla | Campos principales | Relaciones |
|---|---|---|
| `usuario` | id, usuario (único), clave_hash, rol | |
| `categoria` | id, nombre (único) | |
| `plato` | id, nombre, precio, disponible, categoria_id | muchos platos pertenecen a una categoría |
| `pedido` | id, mesa, estado, creado_en | un pedido tiene muchos ítems |
| `item_pedido` | id, pedido_id, plato_id, cantidad, precio_unitario | cada ítem apunta a un plato |
| `reserva` | id, cliente, telefono, fecha, hora, personas | |

Decisiones:
- `item_pedido.precio_unitario` guarda el precio del plato en el momento del pedido. Si el menú cambia de precio, los pedidos anteriores conservan su valor.
- `usuario.clave_hash` guarda el hash SHA-256 de la clave, nunca la clave.
- `pedido.estado` solo avanza un paso a la vez, en el orden RECIBIDO, EN_PREPARACION, SERVIDO, PAGADO.
