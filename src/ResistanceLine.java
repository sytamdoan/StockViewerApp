import java.util.ArrayList;
import java.util.List;

public class ResistanceLine implements Subject {
    private Double ResistanceNumber;
    private ResistanceLineDrawer resistanceLineDrawer;
    private List<Observer> customers = new ArrayList<>();

    public ResistanceLine(Double number) {
        this.ResistanceNumber = number;
    }
    
    public void drawResistanceLine(StockCharter newStockChart) {
        resistanceLineDrawer = new ResistanceLineDrawer(newStockChart, this);
    }

    public Double getResistanceNumber(){
        return ResistanceNumber;
    }

    public void addObservers(Observer observer) {
        customers.add(observer);
    }

    public void removeObservers(Observer observer) {
        customers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer customer :customers) {
            customer.update(message);
        }
    }
}
