package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {
    private static final int PORT = 35000;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Received connection from " + clientSocket.getInetAddress().getHostAddress());
                    handleClient(clientSocket);
                }
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        OutputStream out = clientSocket.getOutputStream();

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isBlank()) {
            sendResponse(out, "HTTP/1.1 400 Bad Request", "text/html; charset=UTF-8",
                    "<html><body><h1>400 Bad Request</h1></body></html>");
            return;
        }

        System.out.println(requestLine);

        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            System.out.println(line);
        }

        try {
            HttpRequest request = HttpRequest.parse(requestLine);
            String body = buildResponseBody(request);
            String statusLine = "HTTP/1.1 200 OK";
            if ("/".equals(request.getPath()) || "".equals(request.getPath())) {
                statusLine = "HTTP/1.1 200 OK";
            } else if (!"/hello".equals(request.getPath()) && !request.getPath().startsWith("/hello")) {
                statusLine = "HTTP/1.1 404 Not Found";
                body = "<html><body><h1>404 Not Found</h1></body></html>";
            }
            sendResponse(out, statusLine, "text/html; charset=UTF-8", body);
        } catch (IllegalArgumentException e) {
            sendResponse(out, "HTTP/1.1 400 Bad Request", "text/html; charset=UTF-8",
                    "<html><body><h1>400 Bad Request</h1></body></html>");
        }
    }

    private static String buildResponseBody(HttpRequest request) {
        String path = request.getPath();
        if ("/".equals(path)) {
            return "<!DOCTYPE html>\n"
                    + "<html lang=\"es\">\n"
                    + "  <head><meta charset=\"UTF-8\"><title>HTTP Server</title></head>\n"
                    + "  <body>\n"
                    + "    <h1>Servidor HTTP mínimo</h1>\n"
                    + "    <p>Prueba el endpoint <a href=\"/hello?name=Ana\">/hello</a>.</p>\n"
                    + "  </body>\n"
                    + "</html>";
        }

        if (path.startsWith("/hello")) {
            String name = request.getQueryParameter("name");
            if (name == null || name.isBlank()) {
                name = "World";
            }
            return "<!DOCTYPE html>\n"
                    + "<html lang=\"es\">\n"
                    + "  <head><meta charset=\"UTF-8\"><title>Hello</title></head>\n"
                    + "  <body>\n"
                    + "    <h1>Hello, " + escapeHtml(name) + "!</h1>\n"
                    + "  </body>\n"
                    + "</html>";
        }

        return "<!DOCTYPE html>\n"
                + "<html><head><meta charset=\"UTF-8\"><title>Not Found</title></head>\n"
                + "<body><h1>404 Not Found</h1></body></html>";
    }

    private static void sendResponse(OutputStream out, String statusLine, String contentType, String body) throws IOException {
        String response = HttpResponse.build(statusLine, contentType, body);
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private static String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}