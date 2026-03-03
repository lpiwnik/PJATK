package zad1.exchange;

import zad1.Utils;

import java.net.URI;

public class ExchangeService {

    private final static String API_EXCHANGE = "https://open.er-api.com/v6/latest/%s";

    public static Exchange getExchange(String currencyCode) throws NullPointerException{
        return Utils.requestUrl(
                URI.create(
                        String.format(API_EXCHANGE,
                                currencyCode))
                , Exchange.class);
    }
}
