import javax.swing.JFrame;

abstract class Screen {
    JFrame currentFrame;

    protected void clearScreen() {
        currentFrame.getContentPane().removeAll();
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}
