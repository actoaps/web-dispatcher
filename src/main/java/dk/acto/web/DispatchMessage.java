package dk.acto.web;

import com.google.gson.JsonObject;

public class DispatchMessage {
    private final String topic;
    private final JsonObject payload;

    DispatchMessage(String topic, JsonObject payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public JsonObject getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "DispatchMessage{" +
                "topic='" + topic + '\'' +
                ", payload=" + payload +
                '}';
    }
}
