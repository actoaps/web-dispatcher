package dk.acto.web.dispatcher.implementation;

import com.google.gson.Gson;
import dk.acto.web.DispatchMessage;
import dk.acto.web.dispatcher.AbstractDispatcher;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TwilioDispatcher extends AbstractDispatcher {
    private static final String URL = "https://studio.twilio.com/v1/Flows/%s/Executions";

    public TwilioDispatcher(String configuration, String apiKey) {
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
        String toNumber = message.getPayload().get("to").getAsString();
        String fromNumber = message.getPayload().get("from").getAsString();

        String bodyString = String.format("To=%s&From=%s",
                URLEncoder.encode(toNumber, StandardCharsets.UTF_8),
                URLEncoder.encode(fromNumber, StandardCharsets.UTF_8));

        log.info(toNumber);
        log.info(fromNumber);
        log.info(bodyString);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse(("application/x-www-form-urlencoded")),
                bodyString
        );

        String completeURL = String.format(URL, split[2]);

        log.info(completeURL);

        Request request = new Request.Builder()
                .url(completeURL)
                .addHeader("Authorization", creds)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .method("POST", body)
                .build();

        try {
            client.newCall(request).execute();
            log.info("Success");
            return "Success";
        } catch (IOException e) {
            log.error("Twilio dispatcher threw exception", e);
            return "Error";
            }
    }
}
