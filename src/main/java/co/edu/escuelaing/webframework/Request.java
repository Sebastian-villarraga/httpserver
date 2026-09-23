package co.edu.escuelaing.webframework;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final String query;
    private final Map<String, String> parameters;

    public Request(String method, String path, String query) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.parameters = parseQueryParameters(query);
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

    public String getValue(String name) {
        return parameters.get(name);
    }
}