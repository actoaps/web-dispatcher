package dk.acto.dispatcher.client;

import lombok.AllArgsConstructor;
import okhttp3.*;

import java.io.IOException;

@AllArgsConstructor
public class DispatcherHttpClient {
    protected final String dispatcherUrl;
    protected final String apiKey;
    protected final String dispatcherPath;
    protected final JsonSerializer jsonSerializer;
    protected final OkHttpClient client = new OkHttpClient();

    public void sendDispatchRequest (Object payload) throws IOException {
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
