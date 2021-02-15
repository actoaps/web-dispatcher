package dk.acto.web.dispatcher.implementation;

import dk.acto.web.DispatchMessage;
import dk.acto.web.dispatcher.AbstractDispatcher;
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
