package zad1.nbp;

import com.google.gson.reflect.TypeToken;
import zad1.Utils;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

public class NBPService {

    private final static String API_NBP = "http://api.nbp.pl/api/exchangerates/tables/%s/";

    public static List<NBPTable.ContentNBP> getNBPTable(String tableType) throws NullPointerException {
        Type listType = new TypeToken<List<NBPTable.ContentNBP>>() {
        }.getType();
        return Utils.requestUrl(URI
                        .create(String.format(API_NBP, tableType))
                , listType);

    }
}
