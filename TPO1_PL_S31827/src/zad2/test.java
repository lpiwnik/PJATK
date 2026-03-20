//package zad1;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.Currency;
//import java.util.Locale;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//public class Service {
//    private String country;
//    private String countryCode;
//    private String currencyCode;
//
//    private static final String OWM_API_KEY = "";
//    private static final String EXCHANGE_API_KEY = "";
//
//    public Service(String country) {
//        this.country = country;
//        this.countryCode = "";
//        this.currencyCode = "";
//        setCodes(country);
//    }
//    private void setCodes(String countryName) {
//        for (Locale locale : Locale.getAvailableLocales()) {
//            if (countryName.equalsIgnoreCase(locale.getDisplayCountry(Locale.ENGLISH)) ||
//                    countryName.equalsIgnoreCase(locale.getDisplayCountry(new Locale("pl", "PL")))) {
//
//                this.countryCode = locale.getCountry();
//                try {
//                    Currency currency = Currency.getInstance(locale);
//                    this.currencyCode = currency.getCurrencyCode();
//                    break;
//                } catch (Exception e) {
//                }
//            }
//        }
//        if (this.currencyCode == null || this.currencyCode.isEmpty()) {
//            this.currencyCode = "USD";
//        }
//    }
//    public String getWeather(String city) {
//        try {
//            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
//                    + URLEncoder.encode(city, "UTF-8") + "," + countryCode
//                    + "&appid=" + OWM_API_KEY + "&units=metric";
//            return getResponse(urlString);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Brak danych o pogodzie";
//        }
//    }
//
//    public Double getRateFor(String targetCurrency) {
//        if (currencyCode.equalsIgnoreCase(targetCurrency)) return 1.0;
//
//        try {
//            String urlString = "https://v6.exchangerate-api.com/v6/" + EXCHANGE_API_KEY + "/latest/" + currencyCode;
//            String json = getResponse(urlString);
//            double targetRate = extractRate(json, targetCurrency.toUpperCase());
//            if (targetRate > 0) {
//                return targetRate;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1.0;
//    }
//
//    public Double getNBPRate() {
//        if ("PLN".equalsIgnoreCase(currencyCode)) return 1.0;
//        try {
//            String urlA = "http://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/?format=json";
//            return extractNBPRate(getResponse(urlA));
//        } catch (Exception e) {
//            try {
//                String urlB = "http://api.nbp.pl/api/exchangerates/rates/b/" + currencyCode + "/?format=json";
//                return extractNBPRate(getResponse(urlB));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return -1.0;
//    }
//    private String getResponse(String urlString) throws Exception {
//        URL url = new URL(urlString);
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        if (con.getResponseCode() != 200) {
//            throw new RuntimeException("HTTP GET Error: " + con.getResponseCode());
//        }
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//        StringBuilder content = new StringBuilder();
//        String inputLine;
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        con.disconnect();
//        return content.toString();
//    }
//    private double extractRate(String json, String currency) {
//        Pattern pattern = Pattern.compile("\"" + currency + "\"\\s*:\\s*([0-9.]+)");
//        Matcher matcher = pattern.matcher(json);
//        if (matcher.find()) {
//            return Double.parseDouble(matcher.group(1));
//        }
//        return -1;
//    }
//
//    private double extractNBPRate(String json) {
//        Pattern pattern = Pattern.compile("\"mid\"\\s*:\\s*([0-9.]+)");
//        Matcher matcher = pattern.matcher(json);
//        if (matcher.find()) {
//            return Double.parseDouble(matcher.group(1));
//        }
//        return -1;
//    }
//    public String getCurrencyCode() {
//        return currencyCode;
//    }
//}%