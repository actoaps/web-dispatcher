package dk.acto.web;

import com.google.gson.JsonParser;
import dk.acto.web.config.ConfigurationFactory;
import dk.acto.web.dispatcher.Dispatcher;
import dk.acto.web.exception.InvalIdApiKey;
import dk.acto.web.exception.MalformedRequest;
import dk.acto.web.exception.NoSuchDispatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DispatchHandler {
    private final Map<String, Dispatcher> dispatcherMap = ConfigurationFactory.configure();
    private final JsonParser jsonParser;

    public DispatchHandler(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public String handle(String dispatcherName, String apiKey, String topic, String body ) throws NoSuchDispatcher, InvalIdApiKey, MalformedRequest {

        if (!dispatcherMap.containsKey(dispatcherName)) {
            throw new NoSuchDispatcher();
        }

        var dispatcher = dispatcherMap.get(dispatcherName);

        if (!apiKey.equals(dispatcher.getApiKey())) {
            throw new InvalIdApiKey();
        }

        var root = jsonParser.parse(body).getAsJsonObject();
        var message = new DispatchMessage(topic, root);

        if (!dispatcher.validate(message).isEmpty()) {
            throw new MalformedRequest();
        }

        return dispatcher.dispatch(message);
    }
}
