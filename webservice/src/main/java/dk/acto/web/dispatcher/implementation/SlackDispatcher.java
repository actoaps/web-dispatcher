package dk.acto.web.dispatcher.implementation;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dk.acto.web.DispatchMessage;
import dk.acto.web.dispatcher.AbstractDispatcher;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.StringWriter;

@Slf4j
public class SlackDispatcher extends AbstractDispatcher {
    private final Mustache mustache = new DefaultMustacheFactory().compile("slacktemplate.mustache");

    public SlackDispatcher(String configuration, String apiKey) {
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
                    StringEscapeUtils.unescapeHtml4(temp.toString()),
                    MediaType.parse(("application/json; charset=utf-8"))
            );

            Request request = new Request.Builder()
                    .url(getConfiguration())
                    .post(body)
                    .build();
            client.newCall(request).execute();
            return "Ok";

        } catch (IOException e) {
            log.error("SlackDispatcher threw exception", e);
        }
        return "Not Ok";
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.of();
    }

    private List<Tuple2<String, String>> parse(DispatchMessage message) {

        return flattenJson(message.getPayload())
                .map(x -> x.update2(decodeEntities(x._2())))
                .map(x -> x.update2(encodeNewLines(x._2())));
    }
}
