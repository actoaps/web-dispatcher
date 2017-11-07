package dk.acto.web.config;

public class DispatcherConfig {
    private final String apiKey;
    private final String config;
    private final String dispatcher;

    public DispatcherConfig(String apiKey, String config, String dispatcher) {
        this.apiKey = apiKey;
        this.config = config;
        this.dispatcher = dispatcher;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getConfig() {
        return config;
    }

    public String getDispatcher() {
        return dispatcher;
    }
}
