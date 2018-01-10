package dk.acto.web.dispatcher;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import io.vavr.collection.List;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PlivoCallDispatcher extends AbstractDispatcher {
    private final Logger logger = LoggerFactory.getLogger(PlivoCallDispatcher.class);
    private final String url  = "https://api.plivo.com/v1/Account/%s/Call/";
    private final Gson gson = new Gson();
    PlivoCallDispatcher(String configuration, String apiKey) {
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
                .url(String.format(url, split[0]))
                .header("Authorization", creds)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return "Ok";
        } catch (IOException e) {
            return "Not Ok";
        }
    }
}
