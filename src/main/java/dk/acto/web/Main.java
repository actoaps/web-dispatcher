package dk.acto.web;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.acto.web.config.DispatcherConfig;
import dk.acto.web.dispatcher.DispatcherFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {

    private static final Pattern auth = Pattern.compile("^([Bb]earer\\s+)?(.+)$");

    public static void main(String[] args) {

        final JsonParser jp = new JsonParser();
        final Map<String, Dispatcher> dispatcherMap = configure(new Gson(), jp);
        port(8080);

        options("/:dispatcher", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
            response.header("Access-Control-Allow-Credentials", "true");
            response.header("Access-Control-Allow-Methods","POST, OPTIONS");
            return "";
        });

        post("/:dispatcher", (request, response) -> {
            String dispatcher = request.params(":dispatcher");

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

            JsonObject root  = jp.parse((request.body())).getAsJsonObject();
            DispatchMessage message = new DispatchMessage(dispatcher, root);
            if (!d.validate(message).isEmpty()){
                response.status(400);
                return "400 Bad Request (Malformed request)";
            }

            return d.dispatch(message);
        });
    }

    private static Map<String, Dispatcher> configure (Gson gson, JsonParser parser ) {

        final String configString = System.getenv("ACTO_CONF");
        final DispatcherFactory df = new DispatcherFactory();

        JsonObject conf = parser.parse(configString).getAsJsonObject();
        ImmutableMap.Builder<String, Dispatcher> builder = ImmutableMap.builder();
        conf.entrySet().forEach(x ->
                        builder.put(
                            x.getKey(),
                            df.of(gson.fromJson(x.getValue(), DispatcherConfig.class))));
        return builder.build();
    }
}
