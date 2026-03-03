package zad1.exchange;

import com.google.gson.GsonBuilder;

import java.util.Map;

public record Exchange(
    String result,
    String base_code,
    Map<String,Double> rates
) {

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
