package dk.acto.web.dispatcher;

import com.google.gson.JsonObject;
import dk.acto.web.Dispatcher;
import io.vavr.Tuple2;
import io.vavr.collection.List;

public abstract class AbstractDispatcher implements Dispatcher {
    private final String configuration;
    private final String apiKey;

    AbstractDispatcher(String configuration, String apiKey) {
        this.configuration = configuration;
        this.apiKey = apiKey;
    }

    List<Tuple2<String, String>> flattenJson(JsonObject src) {
        return List.ofAll(src.entrySet()).map(x -> new Tuple2<>(x.getKey(), x.getValue().getAsString()));
    }


    String getConfiguration() {
        return configuration;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
