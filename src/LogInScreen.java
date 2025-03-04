import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInScreen extends Screen {

    public LogInScreen(JFrame frame) {
        this.currentFrame = frame;
        this.createFrame();
    }

    private void createFrame() {
        this.clearScreen();

        //Big Bold Title
        JLabel label = new JLabel("Welcome To Stock Owl", SwingConstants.CENTER);
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

        //Username Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("Enter User Name:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 30));
        currentFrame.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField userText = new JTextField(20);
        userText.setFont(new Font("Arial", Font.PLAIN, 25));
        currentFrame.add(userText, gbc);

        //Password Fields
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passWordLabel = new JLabel("Enter PassWord:");
        passWordLabel.setFont(new Font("Arial", Font.BOLD, 30));
        currentFrame.add(passWordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JPasswordField passWordText = new JPasswordField(20);
        passWordText.setFont(new Font("Arial", Font.PLAIN, 25));
        currentFrame.add(passWordText, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton loginButton = new JButton("Login");
        currentFrame.add(loginButton, gbc);

        // Action Listener for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText().trim();
                char[] password = passWordText.getPassword();

                if (username.isEmpty() || password.length == 0) {
                    JOptionPane.showMessageDialog(currentFrame, "Both fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(currentFrame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    StockSelectionScreen stockSelectionScreen = new StockSelectionScreen(currentFrame);
                }
            }
        });

        currentFrame.setVisible(true);
    }

}
