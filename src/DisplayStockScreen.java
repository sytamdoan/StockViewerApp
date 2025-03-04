import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayStockScreen extends Screen implements Observer{

    private static String stock = "";
    private static String timeInterval = "";
    JPanel currentChartPanel;
    public static StockCharter stockCharter;


    public DisplayStockScreen(JFrame frame, String newStock, String time) {
        this.currentFrame = frame;
        stock = newStock;
        timeInterval = time;
        this.createFrame();
    }

    @Override
    public void update(String message) {
        System.out.println("The Stock: " + message + " has updated, now to check resistance Line");
        currentChartPanel = stockCharter.drawStock();


    }

    private void createFrame(){
        this.clearScreen();
        Stock newStock = new Stock(stock, timeInterval);
        stockCharter = new StockCharter(currentFrame, newStock);
        stockCharter.addObservers(this);
        currentChartPanel = stockCharter.drawStock();

        currentFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //Adding In A Graph
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        currentFrame.add(currentChartPanel, gbc);

        //Resistance Line Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton resistanceLine = new JButton("Resistance Line");
        currentFrame.add(resistanceLine, gbc);

        //logout Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton logOutButton = new JButton("Log Out");
        currentFrame.add(logOutButton, gbc);

        resistanceLine.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(currentFrame, "Enter a number where the resistance line will be:");
            
            if (input != null) {
                try {
                    Double number = Double.parseDouble(input.trim());
                    ResistanceLine newLine = new ResistanceLine(number);
                    stockCharter.addResistanceLine(newLine);
                    newLine.drawResistanceLine(stockCharter);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(currentFrame, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogInScreen startScreen = new LogInScreen(currentFrame);
            }
        });

        currentFrame.revalidate();
        currentFrame.repaint();
        currentFrame.setVisible(true);
    }
}
