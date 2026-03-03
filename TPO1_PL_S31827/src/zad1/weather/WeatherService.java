package zad1.weather;

import com.google.gson.reflect.TypeToken;
import zad1.Utils;

import java.net.URI;
import java.util.List;

public class WeatherService{
    private static final String API_KEY_WEATHER="e0f61b6ed5afdc42e0f0ec2f52da2269";
    private static final String API_WEATHER_GET_GEOCODING="http://api.openweathermap.org/geo/1.0/direct?q=%s,%s&limit=1&appid=%s";
    private static final String API_WEATHER_GET_WEATHER="https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s9&mode=JSON&units=metric&appid=%s";


    static public Weather getWeather(String country,String city ) throws NullPointerException{
        GeoCoding cityData;
        cityData = ((List<GeoCoding>) Utils.requestUrl(URI
                        .create(String.format(API_WEATHER_GET_GEOCODING,
                                city,
                                Utils.getCountryCode(country),
                                API_KEY_WEATHER)),
                new TypeToken<List<GeoCoding>>() {}.getType())).getFirst();

        Weather weather;
        weather = Utils.requestUrl(URI
                        .create(String.format(API_WEATHER_GET_WEATHER,
                                cityData.lat(),
                                cityData.lon(),
                                API_KEY_WEATHER)),
                Weather.class);

    return weather;
    }
}
