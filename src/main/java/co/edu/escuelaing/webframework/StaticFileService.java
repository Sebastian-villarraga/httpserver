package co.edu.escuelaing.webframework;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticFileService {

    private String staticPath;

    public StaticFileService(String staticPath) {
        this.staticPath = staticPath;
    }

    public byte[] getFile(String requestPath) throws IOException {

        String resourcePath = staticPath + requestPath;

        if (resourcePath.endsWith("/")) {
            resourcePath += "index.html";
        }

        Path filePath = Paths.get(
                "src/main/resources",
                resourcePath.substring(1));

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            return null;
        }

        return Files.readAllBytes(filePath);
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

        return "application/octet-stream";
    }
}