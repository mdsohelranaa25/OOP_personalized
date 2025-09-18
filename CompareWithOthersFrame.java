import javax.swing.*;
import java.awt.*;

public class CompareWithOthersFrame extends JFrame {
    private String userId, userName;

    public CompareWithOthersFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Compare with Others - " + userName);
        setSize(400, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Select a user to compare with:");
        add(lbl);

        // Dummy user list for now
        JButton user1 = new JButton("User A");
        JButton user2 = new JButton("User B");
        JButton backBtn = new JButton("Back");

        add(user1);
        add(user2);
        add(backBtn);

        user1.addActionListener(e -> {
            new CompareWithOtherUserFrame(userId, userName, "User A").setVisible(true);
            dispose();
        });

        user2.addActionListener(e -> {
            new CompareWithOtherUserFrame(userId, userName, "User B").setVisible(true);
            dispose();
        });

        backBtn.addActionListener(e -> {
            new CompareFrame(userId, userName).setVisible(true);
            dispose();
        });
    }
}
