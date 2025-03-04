public class StockHolder implements Observer {

    private volatile static StockHolder onlyInstance;    
    
    private StockHolder(){};

    public static StockHolder getInstance() {
        if(onlyInstance == null) {
            synchronized(StockHolder.class) {
                if(onlyInstance == null) {
                    onlyInstance = new StockHolder();
                }
            }
        }
        return onlyInstance;
    }

    public void update(String Stock) {
        System.out.println("Stock: " + Stock + " has reached its resistance Line");
    }
}
