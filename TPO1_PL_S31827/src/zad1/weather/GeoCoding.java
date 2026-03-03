package zad1.weather;

import com.google.gson.GsonBuilder;

public record GeoCoding(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
