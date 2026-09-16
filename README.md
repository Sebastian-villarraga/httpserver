# Servidor HTTP mínimo

Este proyecto implementa un servidor HTTP básico en Java usando sockets directamente con `ServerSocket` y `Socket`.

## ¿Qué hace?

- Escucha conexiones TCP en el puerto 35000.
- Lee una solicitud HTTP entrante.
- Identifica el método, la ruta y la versión HTTP.
- Devuelve una respuesta HTTP válida con `status line`, `headers`, separación entre headers y body, y cuerpo HTML.
- Mantiene el alcance del laboratorio anterior, sin añadir endpoints ni funcionalidades del Lab 2.

## Requisitos

- Java 17
- Maven 3.9+

## Compilar

```bash
mvn test
mvn package
```

## Ejecutar

```bash
java -cp target/classes com.example.App
```

El servidor queda escuchando en:

- http://localhost:35000/

## Probar desde el navegador

Abre esta URL en el navegador:

- http://localhost:35000/
- http://localhost:35000/hello?name=Ana

## Ejemplo de request

```http
GET /hello?name=Ana HTTP/1.1
Host: localhost:35000
```

## Ejemplo de response

```http
HTTP/1.1 200 OK
Content-Type: text/html; charset=UTF-8
Content-Length: 123
Connection: close

<!DOCTYPE html>
<html>
  <body>
    <h1>Hello, Ana!</h1>
  </body>
</html>
```
