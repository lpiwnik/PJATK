package zad1.weather;

import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public record Weather(
        List<Map<String,String>> weather,
        Map<String,Double> main,
        int visibility,
        Map<String,Double> wind
) {
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
