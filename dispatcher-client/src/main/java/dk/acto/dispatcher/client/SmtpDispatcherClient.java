package dk.acto.dispatcher.client;

import dk.acto.dispatcher.client.request.SmtpRequest;
import dk.acto.dispatcher.client.request.TwilioRequest;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class SmtpDispatcherClient implements DispatcherClient <SmtpRequest> {
    private final DispatcherHttpClient dispatcherClient;

    @Override
    public void dispatch(SmtpRequest payload) throws IOException {
        dispatcherClient.sendDispatchRequest(payload);
    }
}
