# Manual de despliegue (versión 1.0.0)

## 1. Compilar

```bash
./mvnw clean package
```

El comando ejecuta todas las pruebas y genera `target/sigr-0.0.1-SNAPSHOT.jar`. Si una prueba falla, no se genera el paquete.

## 2. Ejecutar

```bash
java -jar target/sigr-0.0.1-SNAPSHOT.jar
```

Para usar otro puerto: `java -jar target/sigr-0.0.1-SNAPSHOT.jar --server.port=9090`.

## 3. Verificar

- `GET http://localhost:8080/api/platos` debe responder 200 con los cinco platos iniciales.
- `http://localhost:8080/reservas/nueva` debe mostrar el formulario de reservas.

## 4. Consideraciones

- La versión 1.0.0 usa H2 en memoria: los datos se pierden al reiniciar. Es suficiente para la línea base y las pruebas; para producción se debe cambiar a una base persistente.
- Las claves de los usuarios iniciales son de prueba y deben cambiarse en cualquier ambiente real.
