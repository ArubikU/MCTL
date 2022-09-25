package dev.arubik.mctl.utils.Json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.lumine.mythic.bukkit.utils.gson.JsonBuilder.JsonArrayBuilder;

public final class JsonBuilder {
    private final StringBuilder builder = new StringBuilder();

    public JsonBuilder() {
        builder.append("{");
    }

    public JsonBuilder append(String key, String value) {
        builder.append("\"").append(key).append("\":\"").append(value).append("\",");
        return this;
    }

    public JsonBuilder append(String key, int value) {
        builder.append("\"").append(key).append("\":").append(value).append(",");
        return this;
    }

    public JsonBuilder append(String key, boolean value) {
        builder.append("\"").append(key).append("\":").append(value).append(",");
        return this;
    }

    public JsonBuilder append(String key, double value) {
        builder.append("\"").append(key).append("\":").append(value).append(",");
        return this;
    }

    public JsonBuilder append(String key, long value) {
        builder.append("\"").append(key).append("\":").append(value).append(",");
        return this;
    }

    public JsonBuilder append(String key, float value) {
        builder.append("\"").append(key).append("\":").append(value).append(",");
        return this;
    }

    public JsonBuilder append(String key, JsonBuilder value) {
        builder.append("\"").append(key).append("\":").append(value.toString()).append(",");
        return this;
    }

    public JsonBuilder append(String key, JsonArrayBuilder value) {
        builder.append("\"").append(key).append("\":").append(value.toString()).append(",");
        return this;
    }

    public JsonBuilder append(String key, Object value) {
        builder.append("\"").append(key).append("\":\"").append(value.toString()).append("\",");
        return this;
    }

    public JsonBuilder append(String key, Object[] value) {
        builder.append("\"").append(key).append("\":[");
        for (Object o : value) {
            builder.append("\"").append(o.toString()).append("\",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("],");
        return this;
    }

    public JsonBuilder append(String key, int[] value) {
        builder.append("\"").append(key).append("\":[");
        for (int o : value) {
            builder.append(o).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("],");
        return this;
    }

    public JsonBuilder append(String key, boolean[] value) {
        builder.append("\"").append(key).append("\":[");
        for (boolean o : value) {
            builder.append(o).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("],");
        return this;
    }

    // generate a Json Element from the StrinBuilder
    public String toString() {
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

    public JsonElement toJson() {
        return new Gson().fromJson(toString(), JsonElement.class);
    }

}