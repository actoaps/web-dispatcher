package dk.acto.web.config;

import dk.acto.web.dispatcher.implementation.LoggerDispatcher;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ConfigurationFactoryTest {

    @Test
    public void testConfigureLegacy() {
        try {
            set(Map.of("ACTO_CONF", "{\"path\": {\"apiKey\": \"2J5GCMcBWw2dMFPyquu364aNjf8AD6ss\",\"config\": \"\",\"dispatcher\": \"Log\"}}"));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        var result = ConfigurationFactory.configure();
        assertNotNull(result);
        assertNotNull(result.get("path"));
        assertEquals(result.get("path").getClass(), LoggerDispatcher.class);
    }
    @Test
    public void testConfigureLegacyMultiple() {
        try {
            set(Map.of("ACTO_CONF", "{\"path\":{\"apiKey\":\"2J5GCMcBWw2dMFPyquu364aNjf8AD6ss\",\"config\":\"\",\"dispatcher\":\"Log\"},\"path2\":{\"apiKey\":\"2J5GCMcBWw2dMFPyquu364aNjf8AD6ss\",\"config\":\"\",\"dispatcher\":\"Log\"},\"path3\":{\"apiKey\":\"2J5GCMcBWw2dMFPyquu364aNjf8AD6ss\",\"config\":\"\",\"dispatcher\":\"Log\"}}"));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        var result = ConfigurationFactory.configure();
        assertNotNull(result);
        assertNotNull(result.get("path"));
        assertEquals(result.get("path").getClass(), LoggerDispatcher.class);
        assertNotNull(result.get("path2"));
        assertEquals(result.get("path2").getClass(), LoggerDispatcher.class);
        assertNotNull(result.get("path3"));
        assertEquals(result.get("path3").getClass(), LoggerDispatcher.class);
    }

    static void set(Map<String, String> newenv) throws NoSuchFieldException, IllegalAccessException {
        Class[] classes = Collections.class.getDeclaredClasses();
        Map<String, String> env = System.getenv();
        for(Class cl : classes) {
            if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                Field field = cl.getDeclaredField("m");
                field.setAccessible(true);
                Object obj = field.get(env);
                Map<String, String> map = (Map<String, String>) obj;
                map.clear();
                map.putAll(newenv);
            }
        }
    }
}
