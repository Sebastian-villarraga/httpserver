package co.edu.escuelaing.app;

import co.edu.escuelaing.webframework.WebFramework;

public class Application {

    public static void main(String[] args) throws Exception {

        WebFramework.staticfiles("/webroot");

        WebFramework.get("/hello", (req, resp) -> {

            String name = req.getValue("name");

            if (name == null || name.isBlank()) {
                name = "world";
            }

            String greetingPrefix = System.getenv()
                    .getOrDefault("GREETING_PREFIX", "Hello");

            return greetingPrefix + " " + name;
        });

        WebFramework.get("/pi", (req, resp) ->
                String.valueOf(Math.PI));

        String environment = System.getenv()
                .getOrDefault("APP_ENV", "development");

        if (environment.equals("development")) {

            WebFramework.get("/shutdown", (req, resp) -> {

                WebFramework.stop();

                return "Server will stop after this response.";
            });
        }

        WebFramework.start();
    }
}