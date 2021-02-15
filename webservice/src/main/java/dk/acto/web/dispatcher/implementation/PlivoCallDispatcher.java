package dk.acto.web.dispatcher.implementation;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import dk.acto.web.dispatcher.AbstractDispatcher;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.IOException;

@Slf4j
public class PlivoCallDispatcher extends AbstractDispatcher {
    private static final String URL = "https://api.plivo.com/v1/Account/%s/Call/";
    private final Gson gson = new Gson();

    public PlivoCallDispatcher(String configuration, String apiKey) {
        super(configuration, apiKey);
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.of();
    }


    @Override
    public String dispatch(DispatchMessage message) {

        String[] split = getConfiguration().split(",");
        String creds = Credentials.basic(split[0], split[1]);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse(("application/json")),
                gson.toJson(message.getPayload())
        );

        Request request = new Request.Builder()
                .url(String.format(URL, split[0]))
                .header("Authorization", creds)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
            return "Ok";
        } catch (IOException e) {
            log.error("PlivoCallDispatcher threw exception", e);
            return "Not Ok";
        }
    }
}
