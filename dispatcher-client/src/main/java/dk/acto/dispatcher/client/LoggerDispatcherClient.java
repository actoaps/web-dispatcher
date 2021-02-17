package dk.acto.dispatcher.client;

import dk.acto.dispatcher.client.request.LoggerRequest;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class LoggerDispatcherClient implements DispatcherClient<LoggerRequest> {
    private final DispatcherHttpClient dispatcherClient;

    @Override
    public void dispatch(LoggerRequest request) throws IOException {
        dispatcherClient.sendDispatchRequest(request);
    }
}
