package dk.acto.web.dispatcher;

import com.google.common.collect.ImmutableMap;
import dk.acto.web.config.DispatcherConfig;
import dk.acto.web.dispatcher.implementation.*;
import io.vavr.Function2;

public class DispatcherFactory {
    private final ImmutableMap<String, Function2<String, String, Dispatcher>> factoryMap = new ImmutableMap.Builder<String, Function2<String, String, Dispatcher>>()
            .put("Slack", SlackDispatcher::new)
            .put("PlivoCall", PlivoCallDispatcher::new)
            .put("PlivoFlow", PlivoFlowDispatcher::new)
            .put("Log", LoggerDispatcher::new)
            .put("Twilio", TwilioDispatcher::new)
            .put("Smtp", SmtpTlsDispatcher::new)
            .build();


    public Dispatcher of(DispatcherConfig dispatcherConfig) {
        return factoryMap.get(dispatcherConfig.getDispatcher()).apply(
                dispatcherConfig.getConfig(),
                dispatcherConfig.getApiKey()
        );
    }
}
