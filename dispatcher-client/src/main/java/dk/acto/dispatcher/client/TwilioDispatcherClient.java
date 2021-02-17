package dk.acto.dispatcher.client;

import dk.acto.dispatcher.client.request.TwilioRequest;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class TwilioDispatcherClient implements DispatcherClient <TwilioRequest> {
    private final DispatcherHttpClient dispatcherClient;

    @Override
    public void dispatch(TwilioRequest payload) throws IOException {
        dispatcherClient.sendDispatchRequest(payload);
    }
}
