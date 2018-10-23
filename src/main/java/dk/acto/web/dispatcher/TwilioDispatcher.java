package dk.acto.web.dispatcher;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class TwilioDispatcher extends AbstractDispatcher {
    private static final String URL = "https://studio.twilio.com/v1/Flows/%s/Executions";
    private final Gson gson = new Gson();

    TwilioDispatcher(String configuration, String apiKey) {
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
                MediaType.parse(("application/x-www-form-urlencoded")),
                gson.toJson(message.getPayload())
        );

        Request request = new Request.Builder()
                .url(String.format(URL, getApiKey()))
                .addHeader("Authorization", creds)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .method("POST", body)
                .build();

        try {
            client.newCall(request).execute();
            return "Success";
        } catch (IOException e) {
            log.error("Twilio dispatcher threw exception", e);
            return "Error";
        }
    }
}
