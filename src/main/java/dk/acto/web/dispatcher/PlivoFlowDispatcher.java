package dk.acto.web.dispatcher;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class PlivoFlowDispatcher extends AbstractDispatcher {
    private static final String URL = "https://phlorunner.plivo.com/v1/account/MAODIWMMUYNGRMNTY4NZ/phlo/a081a57a-5f45-4f39-a4b4-8511449bd654";
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

        String[] split = getConfiguration().split(",");
        String creds = Credentials.basic(split[0], split[1]);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse(("application/json")),
                gson.toJson(message.getPayload())
        );

        Request request = new Request.Builder()
                .url(URL)
                .header("Authorization", creds)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
            return "Success";
        } catch (IOException e) {
            log.error("PlivoFlowDispatcher threw exception", e);
            return "Error";
        }
    }
}
