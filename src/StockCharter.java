import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockCharter implements StockViewer, Subject{
    JFrame currentFrame;
    Stock stock;
    JFreeChart chart;
    XYSeriesCollection dataset;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private List<ResistanceLine> resistanceLine = new ArrayList<>();
    private List<Observer> Observers = new ArrayList<>();

    public void notifyObservers(String stockSymbol) {
        for (Observer observer :Observers) {
            observer.update(stock.getStockSymbol());
        }
    }

    public void addResistanceLine(ResistanceLine newResistanceLine) {
        resistanceLine.add(newResistanceLine);
    }

    public void addObservers(Observer observer) {
        Observers.add(observer);
    }

    public void removeObservers(Observer observer) {
        Observers.remove(observer);
    }

    public StockCharter(JFrame frame, Stock stock) {
        this.currentFrame = frame;
        this.stock = stock;
        dataset = createDataset();
        String numbersOnly = stock.getTimeInterval().replaceAll("\\D", "");
        scheduler.scheduleAtFixedRate(this::updateStockData, Long.parseLong(numbersOnly), Long.parseLong(numbersOnly), TimeUnit.MINUTES);
    }

    private void updateStockData() {
        System.out.println("Fetching new stock data...");

        updateDataSet();
        notifyObservers(stock.getStockSymbol());

        XYSeries firstSeries = dataset.getSeries(0);
        int size = firstSeries.getItemCount();

        if (size > 1) {
            double firstPricePoint = firstSeries.getY(size - 2).doubleValue();
            double secondPricePoint = firstSeries.getY(size - 1).doubleValue();

            for (ResistanceLine line :resistanceLine) {
                if(line.getResistanceNumber() >= firstPricePoint &&  line.getResistanceNumber() <= secondPricePoint) {
                    line.notifyObservers(stock.getStockSymbol());
                }
            }
        }
    }

    @Override
    public JPanel drawStock() {
        double lowerBound = 0;
        double upperBound = 0;
        chart = createChart(dataset);

        XYPlot plot = chart.getXYPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();

        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            XYSeries series = dataset.getSeries(i);
            lowerBound = series.getY(0).doubleValue();
            upperBound = series.getY(0).doubleValue();

            for (int j = 0; j < series.getItemCount(); j++) {
                if (series.getX(j).doubleValue() < lowerBound) {
                    lowerBound = series.getY(j).doubleValue();
                }

                if(series.getX(j).doubleValue() > upperBound) {
                    upperBound = series.getY(j).doubleValue();
                }
            }
        }

        yAxis.setLowerBound(lowerBound - (lowerBound * 0.1));

        yAxis.setUpperBound(upperBound + (upperBound * 0.1));

        // Embed the chart into a ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        return chartPanel;
    }

    private XYSeriesCollection createDataset() {
        XYSeries series = new XYSeries("Data Points");

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(stock.getStockPricing().toString());

            JsonNode timeSeriesNode = rootNode.get("Time Series (" + stock.getTimeInterval() + ")");

            Iterator<String> fieldNames = timeSeriesNode.fieldNames();

            for (int i = 0; i < 10; i++) {
                String timestamp = fieldNames.next();
                String timeOnly = timestamp.split(" ")[1]; 
                String closePrice = timeSeriesNode.get(timestamp).get("4. close").asText();
                series.add(convertTimeToSeconds(timeOnly), Double.parseDouble(closePrice));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private void updateDataSet() {
        XYSeries series = dataset.getSeries(0);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(stock.getStockPricing().toString());

            JsonNode timeSeriesNode = rootNode.get("Time Series (" + stock.getTimeInterval() + ")");

            Iterator<String> fieldNames = timeSeriesNode.fieldNames();

            String timestamp = fieldNames.next();
            String timeOnly = timestamp.split(" ")[1]; 
            String closePrice = timeSeriesNode.get(timestamp).get("4. close").asText();
            series.add(convertTimeToSeconds(timeOnly), Double.parseDouble(closePrice));

        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.removeSeries(0);
        dataset.addSeries(series);
    }

    private JFreeChart createChart(XYSeriesCollection dataset) {
        return ChartFactory.createXYLineChart(
            "Stock Chart for " + stock.getStockSymbol(), 
            "Time Interval " + stock.getTimeInterval(),            
            "Price Points",            
            dataset,             
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            true, true, false    
        );
    }

    private static double convertTimeToSeconds(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }
}
