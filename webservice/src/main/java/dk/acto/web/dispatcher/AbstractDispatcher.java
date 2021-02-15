package dk.acto.web.dispatcher;

import com.google.common.escape.Escapers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractDispatcher implements Dispatcher {
    private final String configuration;
    private final String apiKey;

    public AbstractDispatcher(String configuration, String apiKey) {
        this.configuration = configuration;
        this.apiKey = apiKey;
    }

    protected List<Tuple2<String, String>> flattenJson(JsonObject src) {
        return List.ofAll(unpack(src));
    }

    private ArrayList<Tuple2<String, String>> unpack(JsonObject jsonObject) {
        ArrayList<Tuple2<String, String>> result = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement nextNode = entry.getValue();
            if (nextNode.isJsonPrimitive()) {
                result.add(Tuple.of(entry.getKey(), entry.getValue().getAsString()));
            }
            if (nextNode.isJsonObject()) {
                result.addAll(unpack(nextNode.getAsJsonObject()));
            }
            if (nextNode.isJsonArray()) {
                result.add(Tuple.of(entry.getKey(), nextNode.getAsJsonArray().get(0).getAsString()));
            }
        }
        return result;
    }

    protected String decodeEntities(String src) {
        return StringEscapeUtils.unescapeXml(src);
    }

    protected String encodeNewLines(String src) {
        return Escapers.builder()
                .addEscape('\n', "\\n")
                .build()
                .escape(src);
    }

    protected String getConfiguration() {
        return configuration;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
