package com.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String query;
    private final String protocol;
    private final Map<String, String> parameters;

    public HttpRequest(String method, String path, String query, String protocol) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.protocol = protocol;
        this.parameters = parseQueryParameters(query);
    }

    public static HttpRequest parse(String requestLine) {
        String[] parts = requestLine.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid HTTP request line");
        }

        String method = parts[0];
        String target = parts[1];
        String protocol = parts[2];

        if (!"GET".equalsIgnoreCase(method) && !"POST".equalsIgnoreCase(method)) {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        if (!protocol.startsWith("HTTP/")) {
            throw new IllegalArgumentException("Invalid HTTP version");
        }

        String path;
        String query = "";
        try {
            URI uri = new URI(target);
            path = uri.getPath() == null ? "/" : uri.getPath();
            query = uri.getQuery() == null ? "" : uri.getQuery();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI in request line", e);
        }

        return new HttpRequest(method, path, query, protocol);
    }

    private Map<String, String> parseQueryParameters(String query) {
        Map<String, String> values = new HashMap<>();
        if (query == null || query.isBlank()) {
            return values;
        }
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            values.put(key, value);
        }
        return values;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getQueryParameter(String name) {
        return parameters.get(name);
    }
}
