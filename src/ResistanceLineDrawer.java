import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Layer;
import org.jfree.chart.plot.ValueMarker;
import java.awt.Color;

public class ResistanceLineDrawer extends StockViewerDecorator {
    private static ResistanceLine resistanceLine;

    public ResistanceLineDrawer(StockCharter stockChart, ResistanceLine resistanceLine){
        super(stockChart);
        this.resistanceLine = resistanceLine;
        resistanceLine.addObservers(StockHolder.getInstance());
        XYPlot plot = stockChart.chart.getXYPlot();
        ValueMarker marker = new ValueMarker(resistanceLine.getResistanceNumber());
        marker.setPaint(Color.RED);
        marker.setStroke(new java.awt.BasicStroke(2.0f)); // Set line thickness
        plot.addRangeMarker(marker, Layer.FOREGROUND);
    }

    public ResistanceLine getResistanceLine() {
        return resistanceLine;
    }

    public Double getResistanceNumber() {
        return resistanceLine.getResistanceNumber();
    }
}

