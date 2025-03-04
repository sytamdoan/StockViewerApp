import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        JFrame currentFrame = new JFrame("Stock Viewer");
        currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        LogInScreen startScreen = new LogInScreen(currentFrame);
    }
}