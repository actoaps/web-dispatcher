package dk.acto.dispatcher.client;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
public class SlackDispatcherClient implements DispatcherClient <Map<String, String>> {
    private final DispatcherHttpClient dispatcherClient;

    @Override
    public void dispatch (Map<String, String> payload) throws IOException {
        dispatcherClient.sendDispatchRequest(dispatcherClient);
    }
}
