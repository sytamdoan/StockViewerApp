import javax.swing.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockSelectionScreen extends Screen {

    private static final String APIURL = "https://www.alphavantage.co/query";
    private static final String APIkey = "90MSA6KYH9GRJW3T";

    public StockSelectionScreen(JFrame frame) {
        this.currentFrame = frame;
        this.createFrame();
    }

    private void createFrame() {
        this.clearScreen();
        //Page Title
        JLabel label = new JLabel("What Stock Do You Want To See?", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        currentFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.33;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentFrame.add(label ,gbc);
        gbc.gridwidth = 1;

        //Stock Symbol Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel stockSymbol = new JLabel("Enter Stock Symbol:");
        stockSymbol.setFont(new Font("Arial", Font.BOLD, 20));
        currentFrame.add(stockSymbol, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField stockSymbolText = new JTextField(5);
        stockSymbolText.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.fill = GridBagConstraints.EAST;
        currentFrame.add(stockSymbolText, gbc);

        //Stock Interval Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel userLabel = new JLabel("Select Time Interval:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentFrame.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        String[] options = {"1 min", "5 min", "15 min"};
        JComboBox<String> dropdownMenu = new JComboBox<>(options);
        dropdownMenu.setFont(new Font("Arial", Font.PLAIN, 20));
        currentFrame.add(dropdownMenu, gbc);

        //See Stock Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton seeStockButton = new JButton("See Stock");
        currentFrame.add(seeStockButton, gbc);

        //Log Out Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton logOutButton = new JButton("Log Out");
        currentFrame.add(logOutButton, gbc);

        // Action Listener for Login Button
        seeStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String symbol = stockSymbolText.getText().trim();
                String time = ((String) dropdownMenu.getSelectedItem()).replaceAll("\\s+", "");
                boolean stockExist = StockSelectionScreen.this.checkStock(symbol, time);
                if ((symbol.length() == 4) && (stockExist == true)) {
                    JOptionPane.showMessageDialog(currentFrame, "Stock Found!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    DisplayStockScreen displayStockScreen = new DisplayStockScreen(currentFrame, symbol, time);
                } else {
                    JOptionPane.showMessageDialog(currentFrame, "Something Is Wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogInScreen startScreen = new LogInScreen(currentFrame);
            }
        });

        currentFrame.setVisible(true);
    }

    private boolean checkStock(String symbol, String time) {
        
        String urlString = "https://my-json-server.typicode.com/sytamdoan/stockinfo/db";
        
        //String urlString = APIURL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + time + "&apikey=" + APIkey;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { 
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.toString());
                if (rootNode.has("Information")) {
                    return false;
                }

                return true;
            } else {
                System.out.println("Error: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
