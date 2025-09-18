import javax.swing.*;
import java.awt.*;

public class CompareYesterdayFrame extends JFrame {
    private String userId, userName;

    public CompareYesterdayFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Yesterday Comparison - " + userName);
        setSize(400, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Your habits compared to yesterday:");
        add(lbl);

        // এখানে habit-wise comparison দেখাবে
        // Example:
        JTextArea result = new JTextArea(10, 30);
        result.setText("Exercise: Increased\nSleep: Decreased\nScreen time: Worse");
        add(result);

        JButton backBtn = new JButton("Back");
        add(backBtn);

        backBtn.addActionListener(e -> {
            new CompareWithSelfFrame(userId, userName).setVisible(true);
            dispose();
        });
    }
}
