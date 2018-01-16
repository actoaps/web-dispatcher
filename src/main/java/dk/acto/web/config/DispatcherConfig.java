package dk.acto.web.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DispatcherConfig {
    private final String apiKey;
    private final String config;
    private final String dispatcher;
}
