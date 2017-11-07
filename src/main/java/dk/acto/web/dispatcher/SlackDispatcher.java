package dk.acto.web.dispatcher;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import dk.acto.web.DispatchMessage;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
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

            Request.Post(getConfiguration())
                    .bodyString(String.format("{\"text\":\"%s\"}", temp.toString()) , ContentType.APPLICATION_JSON )
                    .execute().returnContent();
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
