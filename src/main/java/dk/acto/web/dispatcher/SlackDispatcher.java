package dk.acto.web.dispatcher;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dk.acto.web.DispatchMessage;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;


public class SlackDispatcher extends AbstractDispatcher{
    private final Mustache mustache = new DefaultMustacheFactory().compile("slacktemplate.mustache");
    private final Logger logger = LoggerFactory.getLogger(SlackDispatcher.class);

    SlackDispatcher(String configuration, String apiKey) {
        super(configuration, apiKey);
    }

    @Override
    public String dispatch(DispatchMessage message) {
        try {
            List<Tuple2<String, String>> result = parse(message);
            StringWriter temp = new StringWriter();
            this.mustache.execute(temp, result.asJava()).flush();


            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(
                    MediaType.parse(("application/json; charset=utf-8")),
                    String.format("{\"text\":\"%s\"}", temp.toString()));

            Request request = new Request.Builder()
                    .url(getConfiguration())
                    .post(body)
                    .build();
            client.newCall(request).execute();
            return "Ok";

        } catch (IOException e) {
            logger.error("SlackDispatcher threw exception", e);
        }
        return "Not Ok";
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.of();
    }

    private List<Tuple2<String, String>> parse(DispatchMessage message) {
        return flattenJson(message.getPayload());
    }
}
