package dk.acto.web.dispatcher;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class PlivoFlowDispatcher extends AbstractDispatcher {
    private static final String URL = "https://phlorunner.plivo.com/v1/account/";
    private final Gson gson = new Gson();

    PlivoFlowDispatcher(String configuration, String apiKey) {
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

        Headers headers = new Headers.Builder()
                .add("Authorization", creds)
                .add("Content-Type", "application/json")
                .build();
        log.info(headers.toString());

        String completeUrl = String.format("%s%s/phlo/a081a57a-5f45-4f39-a4b4-8511449bd654", URL, split[0]);

        Request request = new Request.Builder()
                .url(completeUrl)
                .headers(headers)
                .method("POST", body)
                .build();
        log.info(request.toString());

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
