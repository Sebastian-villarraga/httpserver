package co.edu.escuelaing.webframework;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Router {

    private final Map<String, Route> routes = new HashMap<>();

    public void get(String path, BiFunction<Request, Response, String> handler) {
        routes.put(path, new Route(path, handler));
    }

    public Route find(String path) {
        return routes.get(path);
    }
}