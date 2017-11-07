package dk.acto.web.dispatcher;

import com.google.common.collect.ImmutableMap;
import dk.acto.web.Dispatcher;
import dk.acto.web.config.DispatcherConfig;
import io.vavr.Function2;

public class DispatcherFactory {
    private final ImmutableMap<String, Function2<String, String, Dispatcher>> factoryMap = ImmutableMap.of(
            "Slack", (SlackDispatcher::new)
    );

    public Dispatcher of(DispatcherConfig dispatcherConfig) {
        return factoryMap.get(dispatcherConfig.getDispatcher()).apply(
                dispatcherConfig.getConfig(),
                dispatcherConfig.getApiKey()
        );
    }
}
