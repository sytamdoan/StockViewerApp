import javax.swing.JPanel;

public class StockViewerDecorator implements StockViewer {
    private StockCharter wrappee;

    public StockViewerDecorator(StockCharter chart) {
        this.wrappee = chart;
    }

    public JPanel drawStock(){
        return wrappee.drawStock();
    } 

}
