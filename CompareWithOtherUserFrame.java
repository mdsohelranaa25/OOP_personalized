import javax.swing.*;
import java.awt.*;

public class CompareWithOtherUserFrame extends JFrame {
    private String userId, userName, otherUser;

    public CompareWithOtherUserFrame(String userId, String userName, String otherUser) {
        this.userId = userId;
        this.userName = userName;
        this.otherUser = otherUser;

        setTitle("Compare with " + otherUser);
        setSize(400, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Comparing your habits with " + otherUser);
        add(lbl);

        JTextArea result = new JTextArea(10, 30);
        result.setText("Exercise: You do more\nSleep: " + otherUser + " sleeps more\nScreen time: You are worse");
        add(result);

        JButton backBtn = new JButton("Back");
        add(backBtn);

        backBtn.addActionListener(e -> {
            new CompareWithOthersFrame(userId, userName).setVisible(true);
            dispose();
        });
    }
}
