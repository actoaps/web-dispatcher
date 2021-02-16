package dk.acto.dispatcher.client;

import java.io.IOException;

public interface DispatcherClient <T> {
    void dispatch (T payload) throws IOException;
}
