package zad1;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    static public String getCountryCode(String country) {

        return Arrays.stream(Locale.getISOCountries())
                .filter(isoCode ->
                        Locale.of("", isoCode).getDisplayCountry()
                                .equals(country))
                .findFirst().orElse(null);
    }

    static public String getCurrencyCode(String country) {

        return Currency.getInstance(Locale.of(
                        "",
                        getCountryCode(country)))
                .toString();
    }

    static public String[] getCountriesList() {
        return Arrays.stream(Locale.getAvailableLocales())
                .map(Locale::getDisplayCountry)
                .filter(displayCountry -> !displayCountry.isEmpty()).distinct().sorted().toArray(String[]::new);
    }

    static public String[] getCurrenciesList() {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .sorted()
                .toArray(String[]::new);
    }

    public static <T> T requestUrl(URI uri, Type type) {

        String json;
        try (
                InputStreamReader in = new InputStreamReader(uri.toURL().openStream());
                BufferedReader br = new BufferedReader(in);
        ) {
            json = br.lines().collect(Collectors.joining());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Gson().fromJson(json, type);
    }
}
