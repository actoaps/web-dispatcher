package dk.acto.dispatcher.client.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoggerRequest {
    private final String message;
}
