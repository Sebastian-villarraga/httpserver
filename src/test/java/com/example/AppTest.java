package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void shouldParseHttpRequestLine() {
        HttpRequest request = HttpRequest.parse("GET / HTTP/1.1");

        assertEquals("GET", request.getMethod());
        assertEquals("/", request.getPath());
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    @Test
    public void shouldParseHttpRequestWithQueryString() {
        HttpRequest request = HttpRequest.parse("GET /hello?name=Ana HTTP/1.1");

        assertEquals("GET", request.getMethod());
        assertEquals("/hello", request.getPath());
        assertEquals("name=Ana", request.getQuery());
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    @Test
    public void shouldBuildValidHttpResponse() {
        String response = HttpResponse.build("HTTP/1.1 200 OK", "text/html; charset=UTF-8", "<html><body>Hello</body></html>");

        assertTrue(response.startsWith("HTTP/1.1 200 OK\r\n"));
        assertTrue(response.contains("Content-Type: text/html; charset=UTF-8\r\n"));
        assertTrue(response.contains("\r\n\r\n"));
        assertTrue(response.endsWith("<html><body>Hello</body></html>"));
        assertTrue(response.contains("Content-Length: " + "<html><body>Hello</body></html>".getBytes().length));
    }
}
