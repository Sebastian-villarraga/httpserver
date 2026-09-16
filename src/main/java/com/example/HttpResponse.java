package com.example;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    public static String build(String statusLine, String contentType, String body) {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        StringBuilder response = new StringBuilder();
        response.append(statusLine).append("\r\n");
        response.append("Content-Type: ").append(contentType).append("\r\n");
        response.append("Content-Length: ").append(bytes.length).append("\r\n");
        response.append("Connection: close\r\n");
        response.append("\r\n");
        response.append(body);
        return response.toString();
    }
}
