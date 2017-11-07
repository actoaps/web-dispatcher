package dk.acto.web.dispatcher;

import dk.acto.web.DispatchMessage;
import io.vavr.collection.List;

public class LoggerDispatcher extends AbstractDispatcher {
    public LoggerDispatcher(String configuration, String apiKey) {
        super(configuration, apiKey);
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.of();
    }
}
