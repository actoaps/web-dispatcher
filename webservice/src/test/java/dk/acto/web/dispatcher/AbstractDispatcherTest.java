package dk.acto.web.dispatcher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.acto.web.dispatcher.implementation.LoggerDispatcher;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.junit.Test;

import static org.testng.Assert.assertEquals;

public class AbstractDispatcherTest {

    @Test
    public void testFlattenExampleJson(){
        String test = "{\n" +
                "    \"glossary\": {\n" +
                "        \"title\": \"example glossary\",\n" +
                "\t\t\"GlossDiv\": {\n" +
                "            \"title\": \"S\",\n" +
                "\t\t\t\"GlossList\": {\n" +
                "                \"GlossEntry\": {\n" +
                "                    \"ID\": \"SGML\",\n" +
                "\t\t\t\t\t\"SortAs\": \"SGML\",\n" +
                "\t\t\t\t\t\"GlossTerm\": \"Standard Generalized Markup Language\",\n" +
                "\t\t\t\t\t\"Acronym\": \"SGML\",\n" +
                "\t\t\t\t\t\"Abbrev\": \"ISO 8879:1986\",\n" +
                "\t\t\t\t\t\"GlossDef\": {\n" +
                "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                "\t\t\t\t\t\t\"GlossSeeAlso\": [\"GML\", \"XML\"]\n" +
                "                    },\n" +
                "\t\t\t\t\t\"GlossSee\": \"markup\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        JsonObject json = JsonParser.parseString(test).getAsJsonObject();
        LoggerDispatcher subject = new LoggerDispatcher("", "a");
        List<Tuple2<String, String>> result = subject.flattenJson(json);
        assertEquals(10, result.length());
    }

    @Test
    public void testFlattenJson(){
        String test = "{\n" +
                "\"name\":\"name\",\n" +
                "\"email\":\"email\",\n" +
                "\"phone\":\"phone\",\n" +
                "\"message\":\"message\"\n" +
                "}";
        JsonObject json = JsonParser.parseString(test).getAsJsonObject();
        LoggerDispatcher subject = new LoggerDispatcher("", "a");
        List<Tuple2<String, String>> result = subject.flattenJson(json);
        assertEquals(result.length(), 4);
    }

    @Test
    public void testDecodeEntities(){
        String test = "Hello&#10;&#9;world";
        LoggerDispatcher subject = new LoggerDispatcher("", "a");
        String result = subject.decodeEntities(test);
        assertEquals(result, "Hello\n\tworld");
    }

    @Test
    public void testEncodeNewLines(){
        String test = "Hello\nworld";
        LoggerDispatcher subject = new LoggerDispatcher("", "a");
        String result = subject.encodeNewLines(test);
        assertEquals( result, "Hello\\nworld");
    }
}
