# Servidor HTTP y Web Framework en Java

Este proyecto implementa un servidor HTTP mínimo y un framework web desarrollado en Java utilizando `ServerSocket` y `Socket`.

El proyecto permite definir rutas HTTP mediante una API sencilla, manejar parámetros de consulta, servir archivos estáticos y configurar el comportamiento del servidor mediante variables de entorno.

## ¿Qué hace?

- Servidor HTTP basado en `ServerSocket` y `Socket`.
- Framework web con una API sencilla para registrar rutas.
- Servicios dinámicos mediante `WebFramework.get(...)`.
- Lectura de parámetros de consulta mediante `req.getValue("name")`.
- Archivos estáticos HTML, CSS y JavaScript.
- Recursos binarios como imágenes PNG.
- JavaScript `fetch()` para consumir servicios dinámicos.
- Configuración de archivos estáticos mediante `WebFramework.staticfiles("/webroot")`.
- Configuración del puerto mediante `PORT`.
- Configuración del saludo mediante `GREETING_PREFIX`.
- Configuración del entorno mediante `APP_ENV`.
- Endpoint `/shutdown` únicamente en desarrollo.
- Apagado controlado del servidor.
- Generación de un JAR ejecutable mediante Maven.

## Arquitectura

La aplicación utiliza el framework mediante:

- `WebFramework`
- `Router`
- `Route`
- `Request`
- `Response`

El servidor HTTP maneja las conexiones TCP y las solicitudes HTTP, mientras que el router determina qué servicio debe ejecutarse.

### Estructura principal

```text
src/
└── main/
    ├── java/
    │   ├── com/example/
    │   │   └── ...
    │   │
    │   └── co/edu/escuelaing/
    │       ├── app/
    │       │   └── Application.java
    │       │
    │       └── webframework/
    │           ├── HttpServer.java
    │           ├── Request.java
    │           ├── Response.java
    │           ├── Route.java
    │           ├── Router.java
    │           ├── StaticFileService.java
    │           └── WebFramework.java
    │
    └── resources/
        └── webroot/
            ├── index.html
            ├── styles.css
            ├── app.js
            └── images/
                └── logo.png
```

## Requisitos

- Java 17
- Maven 3.9+

## Compilar

```bash
mvn compile
```

```bash
mvn test
```

```bash
mvn clean package
```

## Ejecutar

Mediante Maven:

```bash
mvn exec:java "-Dexec.mainClass=co.edu.escuelaing.app.Application"
```

Mediante el JAR:

```bash
java -jar target/httpserver-1.0-SNAPSHOT.jar
```

Por defecto, el servidor inicia en el puerto `8080`.

```text
Server started on port 8080
```

## Variables de entorno

### PORT

Define el puerto del servidor. Si no se especifica, se utiliza `8080`.

PowerShell:

```powershell
$env:PORT="9090"
java -jar target/httpserver-1.0-SNAPSHOT.jar
```

El servidor estará disponible en:

```text
http://localhost:9090/
```

### GREETING_PREFIX

Permite modificar el prefijo utilizado por `/hello`.

Por defecto:

```text
Hello
```

Ejemplo:

```powershell
$env:GREETING_PREFIX="Bienvenido"
```

Solicitud:

```text
http://localhost:8080/hello?name=Sebastian
```

Respuesta:

```text
Bienvenido Sebastian
```

### APP_ENV

Define el ambiente de ejecución.

Por defecto:

```text
development
```

En desarrollo se registra `/shutdown`.

En producción:

```powershell
$env:APP_ENV="production"
```

la ruta `/shutdown` no se registra y devuelve `404 Not Found`.

## Servicios disponibles

### GET /hello

```text
http://localhost:8080/hello
```

Respuesta:

```text
Hello world
```

También acepta `name`:

```text
http://localhost:8080/hello?name=Sebastian
```

Respuesta:

```text
Hello Sebastian
```

### GET /pi

```text
http://localhost:8080/pi
```

Respuesta:

```text
3.141592653589793
```

### GET /shutdown

Disponible únicamente cuando:

```text
APP_ENV=development
```

Solicitud:

```text
http://localhost:8080/shutdown
```

Respuesta:

```text
Server will stop after this response.
```

Después de enviar la respuesta, el servidor finaliza de manera controlada.

## Parámetros de consulta

Los parámetros enviados en la URL pueden obtenerse mediante:

```java
req.getValue("name")
```

Por ejemplo:

```text
/hello?name=Sebastian
```

se interpreta como:

```text
name = Sebastian
```

## Archivos estáticos

Se configuran mediante:

```java
WebFramework.staticfiles("/webroot");
```

Recursos disponibles:

```text
http://localhost:8080/index.html
http://localhost:8080/styles.css
http://localhost:8080/app.js
http://localhost:8080/images/logo.png
```

Los recursos se encuentran en:

```text
src/main/resources/webroot/
```

## JavaScript y Fetch

La aplicación incluye JavaScript que consume el servicio `/hello` mediante `fetch()`.

Esto demuestra la comunicación entre los recursos estáticos del frontend y los servicios dinámicos del servidor.

## Ejemplo de definición de rutas

```java
WebFramework.get("/hello", (req, resp) -> {
    String name = req.getValue("name");

    if (name == null || name.isBlank()) {
        name = "world";
    }

    String greetingPrefix = System.getenv()
            .getOrDefault("GREETING_PREFIX", "Hello");

    return greetingPrefix + " " + name;
});
```

Otro servicio:

```java
WebFramework.get("/pi", (req, resp) ->
        String.valueOf(Math.PI));
```

## Ejemplo de request HTTP

```http
GET /hello?name=Sebastian HTTP/1.1
Host: localhost:8080
```

## Ejemplo de response HTTP

```http
HTTP/1.1 200 OK
Content-Type: text/plain; charset=UTF-8
Content-Length: ...

Hello Sebastian
```

## Manejo de rutas inexistentes

Cuando una ruta no está registrada y tampoco corresponde a un archivo estático:

```text
404 Not Found
```

Ejemplo:

```text
http://localhost:8080/ruta-inexistente
```

## Ejecución del JAR

Después de:

```bash
mvn clean package
```

se genera:

```text
target/httpserver-1.0-SNAPSHOT.jar
```

Se puede ejecutar mediante:

```bash
java -jar target/httpserver-1.0-SNAPSHOT.jar
```

La clase principal es:

```text
co.edu.escuelaing.app.Application
```

## Pruebas realizadas

Se verificó:

- Compilación del proyecto.
- Pruebas Maven.
- Ejecución mediante Maven.
- Ejecución mediante JAR.
- Servicio `/hello`.
- Parámetro `name`.
- Servicio `/pi`.
- Rutas inexistentes.
- Archivos HTML.
- Archivos CSS.
- Archivos JavaScript.
- Imágenes binarias.
- JavaScript mediante `fetch()`.
- Variable `PORT`.
- Variable `GREETING_PREFIX`.
- Variable `APP_ENV`.
- `/shutdown` en desarrollo.
- Deshabilitación de `/shutdown` en producción.

## Ejecución en producción

Variables:

```bash
export PORT=8080
export APP_ENV=production
export GREETING_PREFIX=Hello
```

Ejecutar:

```bash
java -jar target/httpserver-1.0-SNAPSHOT.jar
```

En Windows PowerShell:

```powershell
$env:PORT="8080"
$env:APP_ENV="production"
$env:GREETING_PREFIX="Hello"

java -jar target/httpserver-1.0-SNAPSHOT.jar
```

En producción no se expone:

```text
/shutdown
```

## Despliegue en AWS

El proyecto está preparado para desplegarse en una instancia de Amazon EC2.

La instancia deberá ejecutar el JAR y configurar el puerto mediante `PORT`.

La aplicación podrá ser accedida mediante:

```text
http://<PUBLIC-IP>:8080/
```

El ambiente de producción deberá utilizar:

```text
APP_ENV=production
```

## Tecnologías utilizadas

- Java 17
- Maven
- Sockets TCP
- HTTP
- HTML
- CSS
- JavaScript
- JavaScript Fetch API
- Amazon EC2
