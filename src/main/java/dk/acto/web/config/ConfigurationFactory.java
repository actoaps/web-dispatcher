package dk.acto.web.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.acto.web.Dispatcher;
import dk.acto.web.dispatcher.DispatcherFactory;

import java.util.Map;

public class ConfigurationFactory {
    private final static JsonParser parser = new JsonParser();
    private final static Gson gson = new Gson();

    public static Map<String, Dispatcher> configure () {

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
