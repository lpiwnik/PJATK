package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private WebEngine webEngine;
    private final JFXPanel fxPanel;
    private final Service service;

    private final JComboBox<String> countriesCB = new JComboBox<>(Utils.getCountriesList());
    private final JComboBox<String> currenciesCB = new JComboBox<>(Utils.getCurrenciesList());
    private final JTextField cityInput = new JTextField(15);
    private final JButton submitBtn = new JButton("Get details");

    private static final JLabel weatherInfo = new JLabel("Weather: N/A", SwingConstants.CENTER);
    private static final JLabel nbpInfo = new JLabel("NBP: N/A", SwingConstants.CENTER);
    private static final JLabel exchInfo = new JLabel("Rate: N/A", SwingConstants.CENTER);
    private static final JLabel errorInfo = new JLabel("ERROR: wrong data", SwingConstants.CENTER);
    private static JPanel dataRow;

    public MainView(Service initialService) {
        this.service = initialService;

        setTitle("TPO1_S31827_WEBCLIENTS");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JPanel inputRow = new JPanel(new FlowLayout());

        inputRow.add(new JLabel("Country:"));
        inputRow.add(countriesCB);
        inputRow.add(new JLabel("City:"));
        inputRow.add(cityInput);
        inputRow.add(new JLabel("Currency:"));
        inputRow.add(currenciesCB);
        inputRow.add(submitBtn);

        dataRow = getDataRow();
        dataRow.setVisible(false);

        topPanel.add(inputRow);
        topPanel.add(dataRow);
        add(topPanel, BorderLayout.NORTH);

        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(this::initFX);

        submitBtn.addActionListener(e -> {
            service.setCountry((String) countriesCB.getSelectedItem());
            refreshData(cityInput.getText(), (String) currenciesCB.getSelectedItem());
        });

        setLocationRelativeTo(null);
    }

    private static JPanel getDataRow() {
        dataRow = new JPanel(new BorderLayout());
        dataRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        dataRow.setPreferredSize(new Dimension(0, 40));

        Font dataFont = new Font("SansSerif", Font.BOLD, 13);
        weatherInfo.setFont(dataFont);
        nbpInfo.setFont(dataFont);
        exchInfo.setFont(dataFont);
        errorInfo.setFont(dataFont);

        return dataRow;
    }

    private void initFX() {
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(false);

        countriesCB.setSelectedItem(service.getCountry());
        cityInput.setText(service.getCity());
        currenciesCB.setSelectedItem(service.getCurrencyCode());

        webEngine.load("https://en.wikipedia.org/wiki/Main_Page");
        fxPanel.setScene(new Scene(webView));
    }

    private void refreshData(String city, String currency) {
        try {
            Double rateNBP = service.getNBPRate();
            Double rateEx = service.getRateFor(currency);
            service.getWeather(city);

            SwingUtilities.invokeLater(() -> {
                dataRow.removeAll();
                dataRow.setLayout(new GridLayout(1, 3, 10, 0));

                weatherInfo.setText(String.format("Weather: %s°C, %s",
                        service.getWeather().main().get("temp"),
                        service.getWeather().weather().getFirst().get("description")));
                nbpInfo.setText(String.format("NBP (PLN): %.4f", rateNBP));
                exchInfo.setText(String.format("Exch (%s): %.4f", currency, rateEx));

                dataRow.add(weatherInfo);
                dataRow.add(nbpInfo);
                dataRow.add(exchInfo);

                dataRow.setVisible(true);
                dataRow.revalidate();
                dataRow.repaint();
            });

            Platform.runLater(() -> webEngine.load("https://en.wikipedia.org/wiki/" + city.trim().replace(" ", "_")));

        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                dataRow.removeAll();
                dataRow.setLayout(new BorderLayout());
                errorInfo.setText("Error: Could not find data for " + city);
                errorInfo.setForeground(Color.RED);
                dataRow.add(errorInfo, BorderLayout.CENTER);
                dataRow.setVisible(true);
                dataRow.revalidate();
                dataRow.repaint();
            });
        }
    }
}