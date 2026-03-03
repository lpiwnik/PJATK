package zad1.nbp;

import com.google.gson.GsonBuilder;

import java.util.List;


public interface NBPTable {
    List<Rate> rates();

    String table();

    record Rate(
            String currency,
            String code,
            double mid
    ) {
    }

    record ContentNBP(
            String table,
            List<Rate> rates
    ) implements NBPTable {

        @Override
        public String toString() {
            return new GsonBuilder().setPrettyPrinting().create().toJson(this);
        }
    }


}










