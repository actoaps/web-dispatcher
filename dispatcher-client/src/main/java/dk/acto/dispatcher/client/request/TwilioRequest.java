package dk.acto.dispatcher.client.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TwilioRequest {
    private String to;
    private String from;
}
