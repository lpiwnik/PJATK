/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;

import javax.swing.*;
import java.util.Locale;

public class Main {
  public static void main(String[] args) {
    Locale.setDefault(new Locale("en_En","EN"));
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();

    System.out.println(weatherJson);
    //...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater( () -> {

      MainView view = new MainView(s);
      view.setVisible(true);

    });


  }
}
