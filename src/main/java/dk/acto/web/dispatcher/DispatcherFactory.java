package dk.acto.web.dispatcher;

import com.google.common.collect.ImmutableMap;
import dk.acto.web.config.DispatcherConfig;
import dk.acto.web.dispatcher.implementation.*;
import io.vavr.Function2;

public class DispatcherFactory {
    private final ImmutableMap<String, Function2<String, String, Dispatcher>> factoryMap = ImmutableMap.of(
            "Slack", SlackDispatcher::new,
            "PlivoCall", PlivoCallDispatcher::new,
            "PlivoFlow", PlivoFlowDispatcher::new,
            "Log", LoggerDispatcher::new,
            "Twilio", TwilioDispatcher::new
    );

    public Dispatcher of(DispatcherConfig dispatcherConfig) {
        return factoryMap.get(dispatcherConfig.getDispatcher()).apply(
                dispatcherConfig.getConfig(),
                dispatcherConfig.getApiKey()
        );
    }
}
