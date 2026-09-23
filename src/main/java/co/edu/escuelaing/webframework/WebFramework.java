package co.edu.escuelaing.webframework;

import java.util.function.BiFunction;

public class WebFramework {

    private static final Router router = new Router();

    private static String staticFilesPath = "/webroot";

    private static HttpServer server;

    public static void get(
            String path,
            BiFunction<Request, Response, String> handler) {

        router.get(path, handler);
    }

    public static void staticfiles(String path) {
        staticFilesPath = path;
    }

    public static Router getRouter() {
        return router;
    }

    public static void start(int port) throws Exception {

        server = new HttpServer(router, staticFilesPath);

        server.start(port);
    }

    public static void stop() {

        if (server != null) {
            server.stop();
        }
    }

    public static void start() throws Exception {

        String portValue = System.getenv("PORT");

        int port = (portValue == null || portValue.isBlank())
                ? 8080
                : Integer.parseInt(portValue);

        start(port);
    }
}