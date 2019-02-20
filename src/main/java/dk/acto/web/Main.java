package dk.acto.web;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.acto.web.config.ConfigurationFactory;
import dk.acto.web.dispatcher.Dispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spark.Spark.*;

@Slf4j
public class Main {

    private static final Pattern auth = Pattern.compile("^([Bb]earer\\s+)?(.+)$");

    public static void main(String[] args) {
        final JsonParser jp = new JsonParser();

        final Map<String, Dispatcher> dispatcherMap = ConfigurationFactory.configure();
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
            log.info(String.join(", ", dispatcherMap.keySet()));

            if (!dispatcherMap.containsKey(dispatcher)) {
                response.status(404);
                return "404 Not Found";
            }

            Dispatcher d = dispatcherMap.get(dispatcher);
            String authString = request.headers("Authorization");
            Matcher matcher = auth.matcher(authString);
            if (!matcher.matches()) {
                response.status(401);
                return "401 Unauthorized";
            }

            String apiKey = matcher.group(2);
            if (!apiKey.equals(d.getApiKey())) {
                response.status(403);
                return "403 Forbidden";
            }

            String rBody = request.body();
            if (StringUtils.isEmpty(rBody)){
                response.status(400);
                return "400 Bad Request (Empty Body)";
            }

            JsonObject root = jp.parse((request.body())).getAsJsonObject();
            DispatchMessage message = new DispatchMessage(dispatcher, root);
            if (!d.validate(message).isEmpty()){
                response.status(400);
                return "400 Bad Request (Malformed request)";
            }

            response.header("Access-Control-Allow-Origin", "*");
            return d.dispatch(message);
        });
    }
}
