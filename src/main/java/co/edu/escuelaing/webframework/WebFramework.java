package co.edu.escuelaing.webframework;

import java.util.function.BiFunction;

public class WebFramework {

    private static final Router router = new Router();

    public static void get(
            String path,
            BiFunction<Request, Response, String> handler) {

        router.get(path, handler);
    }

    public static Router getRouter() {
        return router;
    }

    public static void start(int port) throws Exception {

        HttpServer server =
                new HttpServer(router, "/webroot");

        server.start(port);
    }

    public static void start() throws Exception {

        start(8080);
    }
}