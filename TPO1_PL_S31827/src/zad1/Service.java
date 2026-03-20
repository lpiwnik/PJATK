/**
 *
 * @author Piwnik Łukasz s31827
 *
 */

package zad1;

import zad1.exchange.Exchange;
import zad1.exchange.ExchangeService;

import zad1.nbp.NBPService;
import zad1.nbp.NBPTable;
import zad1.weather.Weather;
import zad1.weather.WeatherService;

import java.util.List;
import java.util.stream.Stream;

public class Service {
    private Weather weather;
    private String country;
    private String city;
    private String[] nbpTables;
    private String currencyCode;


    public Service(String country) {
        this.country = country;
    }

    public String getWeather(String city) throws NullPointerException {
        this.city=city;
        this.weather=WeatherService.getWeatherOld(getCountry(), city);
        return String.valueOf(weather);
    }

    public Double getRateFor(String currencyCode) throws  NullPointerException {

        Exchange exchange = ExchangeService.getExchange(currencyCode);

        final double[] dReturn = {0};
        Stream.of(exchange.rates()).forEach(
                stringDoubleMap ->
                        dReturn[0] = stringDoubleMap.get(Utils.getCurrencyCode(country))
        );
        this.currencyCode=currencyCode;

        return dReturn[0];
    }

    public Double getNBPRate() throws NullPointerException{
        String currencyCode = Utils.getCurrencyCode(this.country);

        if (currencyCode == null) return null;

        if (currencyCode.equals("PLN")) return 1.0;

        List<NBPTable.Rate> nbpRatesList_A =
                NBPService.getNBPTable("a").getFirst().rates();
        List<NBPTable.Rate> nbpRatesList_B =
                NBPService.getNBPTable("b").getFirst().rates();


        double mid = Stream.concat(nbpRatesList_A.stream(), nbpRatesList_B.stream())
                .filter(rate -> rate.code().equals(currencyCode))
                .map(NBPTable.Rate::mid)
                .findFirst()
                .orElse(0.0);
        return mid;
    }


    public String getCountry() {
        return country;
    }
    public Weather getWeather() {
        return weather;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}

