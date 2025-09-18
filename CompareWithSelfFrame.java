import javax.swing.*;
import java.awt.*;

public class CompareWithSelfFrame extends JFrame {
    private String userId, userName;

    public CompareWithSelfFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Self Comparison - " + userName);
        setSize(400, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Compare your habits with:");
        add(lbl);

        JButton yesterdayBtn = new JButton("Yesterday");
        JButton weekBtn = new JButton("Last 7 Days");
        JButton backBtn = new JButton("Back");

        add(yesterdayBtn);
        add(weekBtn);
        add(backBtn);

        yesterdayBtn.addActionListener(e -> {
            new CompareYesterdayFrame(userId, userName).setVisible(true);
            dispose();
        });

        weekBtn.addActionListener(e -> {
            new CompareWeekFrame(userId, userName).setVisible(true);
            dispose();
        });

        backBtn.addActionListener(e -> {
            new CompareFrame(userId, userName).setVisible(true);
            dispose();
        });
    }
}
