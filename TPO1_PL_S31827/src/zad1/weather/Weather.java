package zad1.weather;

import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public record Weather(
        Map<String,Double> coord,
        List<Map<String,String>> weather,
        String base,
        Map<String,Double> main,
        int visibility,
        Map<String,Double> wind,
        Map<String,Integer> clouds,
        String dt,
        Map<String,String> sys,
        int timezone,
        int id,
        String name,
        int cod
) {
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
