package dk.acto.dispatcher.client.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmtpRequest {
    private String to;
    private String subject;
    private String body;

    private String name;
    private String type;
    private String data;
}
