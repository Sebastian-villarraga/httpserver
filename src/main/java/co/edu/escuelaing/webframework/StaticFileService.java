package co.edu.escuelaing.webframework;

import java.io.IOException;
import java.io.InputStream;

public class StaticFileService {

    private final String staticPath;

    public StaticFileService(String staticPath) {
        this.staticPath = staticPath;
    }

    public byte[] getFile(String requestPath) throws IOException {

        if (requestPath == null || requestPath.isBlank()) {
            return null;
        }

        if (requestPath.contains("..")) {
            return null;
        }

        String resourcePath = staticPath + requestPath;

        if (resourcePath.endsWith("/")) {
            resourcePath += "index.html";
        }

        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }

        try (InputStream inputStream =
                     StaticFileService.class
                             .getClassLoader()
                             .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                return null;
            }

            return inputStream.readAllBytes();
        }
    }

    public String getContentType(String path) {

        if (path.endsWith(".html")) {
            return "text/html; charset=UTF-8";
        }

        if (path.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        }

        if (path.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        }

        if (path.endsWith(".png")) {
            return "image/png";
        }

        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        if (path.endsWith(".gif")) {
            return "image/gif";
        }

        if (path.endsWith(".svg")) {
            return "image/svg+xml";
        }

        return "application/octet-stream";
    }
}