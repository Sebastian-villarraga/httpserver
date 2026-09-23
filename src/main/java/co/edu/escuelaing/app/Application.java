package co.edu.escuelaing.app;

import co.edu.escuelaing.webframework.WebFramework;

public class Application {

    public static void main(String[] args) throws Exception {

        WebFramework.get("/hello", (req, resp) -> {

            String name = req.getValue("name");

            if (name == null || name.isBlank()) {
                name = "world";
            }

            return "Hello " + name;
        });

        WebFramework.get("/pi", (req, resp) ->
                String.valueOf(Math.PI));

        WebFramework.start(8080);
    }
}