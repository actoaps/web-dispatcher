package dk.acto.dispatcher.client;

import lombok.AllArgsConstructor;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
public class SlackDispatcherClient implements DispatcherClient <Map<String, String>> {
    private final String dispatcherUrl;
    private final String apiKey;
    private final String dispatcherPath;
    private final JsonSerializer jsonSerializer;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void dispatch (Map<String, String> payload) throws IOException {
        var url = HttpUrl.parse(dispatcherUrl + dispatcherPath);
        var body = RequestBody.create(
                jsonSerializer.toJson(payload),
                MediaType.parse("application/json; charset=utf-8"));

        var request = new Request.Builder()
                .url(url)
                .header("Authorization", String.format("Bearer %s", apiKey))
                .post(body)
                .build();

        client.newCall(request).execute();
    }
}
