package dk.acto.web.dispatcher.implementation;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import dk.acto.web.dispatcher.AbstractDispatcher;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class PlivoFlowDispatcher extends AbstractDispatcher {
    private static final String URL = "https://phlorunner.plivo.com/v1/account/";
    private final Gson gson = new Gson();

    public PlivoFlowDispatcher(String configuration, String apiKey) {
        super(configuration, apiKey);
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.of();
    }


    @Override
    public String dispatch(DispatchMessage message) {
        log.info(message.toString());

        String[] split = getConfiguration().split(",");
        String creds = Credentials.basic(split[0], split[1]);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse(("application/json")),
                gson.toJson(message.getPayload())
        );
        log.info(gson.toJson(message.getPayload()));

//        Headers headers = new Headers.Builder()
//                .add("Authorization", creds)
//                .add("Content-Type", "application/json")
//                .build();
//        log.info(headers.toString());

        String completeUrl = String.format("%s%s/phlo/a081a57a-5f45-4f39-a4b4-8511449bd654", URL, split[0]);

        Request request = new Request.Builder()
                .url(completeUrl)
                .addHeader("Authorization", creds)
                .addHeader("Content-Type", "application/json")
                .method("POST", body)
                .build();
        log.info(request.toString());
        log.info("Headers:");
        log.info(request.headers().toString());
        log.info("Body content-type:");
        log.info(request.body().contentType().toString());

        try {
            log.info(String.format("Making a request for: %s", completeUrl));
            client.newCall(request).execute();
            return "Success";
        } catch (IOException e) {
            log.error("PlivoFlowDispatcher threw exception", e);
            return "Error";
        }
    }
}
