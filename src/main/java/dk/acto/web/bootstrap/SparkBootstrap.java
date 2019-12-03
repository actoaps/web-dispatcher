package dk.acto.web.bootstrap;

import dk.acto.web.DispatchHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spark.Spark.*;

@Slf4j
public class SparkBootstrap {
    private static final Pattern auth = Pattern.compile("^([Bb]earer\\s+)?(.+)$");
    private final DispatchHandler handler;

    public SparkBootstrap(DispatchHandler handler) {
        this.handler = handler;
    }

    public void init() {
        port(8080);

        options("*", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
            response.header("Access-Control-Allow-Credentials", "true");
            response.header("Access-Control-Allow-Methods","POST, OPTIONS");
            return "";
        });

        post("*", (request, response) -> {
            String dispatcher = request.pathInfo().substring(1);

            log.info(dispatcher);

            String authString = request.headers("Authorization");
            Matcher matcher = auth.matcher(authString);
            if (!matcher.matches()) {
                response.status(401);
                return "401 Unauthorized";
            }

            String apiKey = matcher.group(2);

            String rBody = request.body();

            var result = handler.handle(dispatcher, apiKey, dispatcher, rBody);
            System.out.println("The result of the dispatch: " + result);
            response.header("Access-Control-Allow-Origin", "*");
            return result;
        });
    }
}
