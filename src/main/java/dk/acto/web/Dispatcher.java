package dk.acto.web;

import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Dispatcher {

    default String dispatch( DispatchMessage message) {
        Logger logger = LoggerFactory.getLogger(Dispatcher.class);
        logger.info(String.valueOf(message));
        return "Ok";
    }

    List<String> validate(DispatchMessage message);

    String getApiKey();
}
