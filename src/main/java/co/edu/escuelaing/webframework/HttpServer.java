package co.edu.escuelaing.webframework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {

    private final Router router;
    private final StaticFileService staticFileService;

    public HttpServer(Router router, String staticPath) {
        this.router = router;
        this.staticFileService = new StaticFileService(staticPath);
    }

    public void start(int port) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            while (true) {

                try (Socket clientSocket = serverSocket.accept()) {

                    handleClient(clientSocket);
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream(),
                        StandardCharsets.UTF_8));

        OutputStream out = clientSocket.getOutputStream();

        String requestLine = in.readLine();

        if (requestLine == null || requestLine.isBlank()) {
            sendResponse(
                    out,
                    "HTTP/1.1 400 Bad Request",
                    "text/plain",
                    "400 Bad Request");

            return;
        }

        // Consumir headers
        String line;

        while ((line = in.readLine()) != null && !line.isEmpty()) {
            // Por ahora no necesitamos procesar los headers.
        }

        try {

            Request request = parseRequest(requestLine);

            Route route = router.find(request.getPath());

            if (route != null) {

                String body = route.handle(
                        request,
                        new Response());

                sendResponse(
                        out,
                        "HTTP/1.1 200 OK",
                        "text/plain; charset=UTF-8",
                        body);

                return;
            }

            byte[] file = staticFileService.getFile(
                    request.getPath());

            if (file != null) {

                String contentType =
                        staticFileService.getContentType(
                                request.getPath());

                sendBinaryResponse(
                        out,
                        "HTTP/1.1 200 OK",
                        contentType,
                        file);

                return;
            }

            sendResponse(
                    out,
                    "HTTP/1.1 404 Not Found",
                    "text/plain",
                    "404 Not Found");

        } catch (IllegalArgumentException e) {

            sendResponse(
                    out,
                    "HTTP/1.1 400 Bad Request",
                    "text/plain",
                    "400 Bad Request");
        }
    }

    private Request parseRequest(String requestLine) {

        String[] parts = requestLine.trim().split("\\s+");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Invalid HTTP request line");
        }

        String method = parts[0];
        String target = parts[1];

        if (!"GET".equalsIgnoreCase(method)) {
            throw new IllegalArgumentException(
                    "Only GET is supported");
        }

        String path = target;
        String query = "";

        int questionMark = target.indexOf('?');

        if (questionMark >= 0) {

            path = target.substring(0, questionMark);
            query = target.substring(questionMark + 1);
        }

        return new Request(method, path, query);
    }

    private void sendResponse(
            OutputStream out,
            String statusLine,
            String contentType,
            String body) throws IOException {

        byte[] bodyBytes =
                body.getBytes(StandardCharsets.UTF_8);

        String headers =
                statusLine + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + bodyBytes.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }

    private void sendBinaryResponse(
            OutputStream out,
            String statusLine,
            String contentType,
            byte[] body) throws IOException {

        String headers =
                statusLine + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }
}