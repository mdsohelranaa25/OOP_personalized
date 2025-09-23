import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardFrame extends JFrame {
    private String userId;
    private String userName;
    
 
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);      // Blue-600
    private static final Color SECONDARY_COLOR = new Color(99, 102, 241);   // Indigo-500
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Slate-50
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);        // Slate-900
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);     // Slate-600
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);      // Green-500
    private static final Color WARNING_COLOR = new Color(245, 158, 11);     // Amber-500
    private static final Color ERROR_COLOR = new Color(239, 68, 68);        // Red-500
    private static final Color PURPLE_COLOR = new Color(147, 51, 234);      // Purple-600
    private static final Color CYAN_COLOR = new Color(6, 182, 212);         // Cyan-500

    public DashboardFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Personalize Habit Tracker - Dashboard");
        setSize(580, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

      
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

       
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

       
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(25, 30, 25, 30)
        ));

       
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_COLOR);

        JLabel welcomeLabel = new JLabel("Welcome back, " + userName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        JLabel dateLabel = new JLabel(currentTime);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_SECONDARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mottoLabel = new JLabel("Ready to build better habits today?");
        mottoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mottoLabel.setForeground(TEXT_SECONDARY);
        mottoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(welcomeLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(dateLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(mottoLabel);

       
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(CARD_COLOR);

        JLabel userIdLabel = new JLabel("ID: " + userId);
        userIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userIdLabel.setForeground(TEXT_SECONDARY);
        userIdLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statusPanel.setBackground(CARD_COLOR);
        
        JLabel statusDot = new JLabel("●");
        statusDot.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusDot.setForeground(SUCCESS_COLOR);
        
        JLabel statusLabel = new JLabel("Active");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS_COLOR);
        
        statusPanel.add(statusDot);
        statusPanel.add(Box.createHorizontalStrut(3));
        statusPanel.add(statusLabel);

        rightPanel.add(userIdLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(statusPanel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 2, 15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(25, 0, 25, 0));

      
        JPanel habitCard = createDashboardCard("Add Habit", "Create new habits to track", 
            "➕", SUCCESS_COLOR, e -> {
                new HabitFrame(userId, userName).setVisible(true);
                dispose();
            });

        JPanel showCard = createDashboardCard("View Habits", "Check your habit progress", 
            "📊", PRIMARY_COLOR, e -> {
                new ShowFrame(userId, userName).setVisible(true);
                dispose();
            });

        JPanel compareCard = createDashboardCard("Compare Habits", "Analyze habit trends", 
            "📈", WARNING_COLOR, e -> {
                new CompareFrame(userId, userName).setVisible(true);
                dispose();
            });

        JPanel profileCard = createDashboardCard("Profile", "Update your information", 
            "👤", PURPLE_COLOR, e -> {
                new ProfileFrame(userId).setVisible(true);
                dispose();
            });

       
        JPanel placeholderCard = createPlaceholderCard();

        JPanel logoutCard = createDashboardCard("Sign Out", "Exit your account", 
            "🚪", ERROR_COLOR, e -> {
                int option = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to sign out?", 
                    "Confirm Sign Out", 
                    JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    new LoginFrame().setVisible(true);
                    dispose();
                }
            });

        contentPanel.add(habitCard);
        contentPanel.add(showCard);
        contentPanel.add(compareCard);
        contentPanel.add(profileCard);
        contentPanel.add(placeholderCard);
        contentPanel.add(logoutCard);

        return contentPanel;
    }

    private JPanel createPlaceholderCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true));

       
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(250, 250, 250));
        centerPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel placeholderIcon = new JLabel("⭐");
        placeholderIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        placeholderIcon.setForeground(TEXT_SECONDARY);
        placeholderIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel placeholderText = new JLabel("More features");
        placeholderText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        placeholderText.setForeground(TEXT_SECONDARY);
        placeholderText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // JLabel placeholderSubText = new JLabel("coming soon");
        // placeholderSubText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        // placeholderSubText.setForeground(TEXT_SECONDARY);
        // placeholderSubText.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(placeholderIcon);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(placeholderText);
        centerPanel.add(Box.createVerticalStrut(5));
       // centerPanel.add(placeholderSubText);

        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createDashboardCard(String title, String description, String icon, 
                                     Color accentColor, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(19, 19, 19, 19)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    new EmptyBorder(20, 20, 20, 20)
                ));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (action != null) {
                    action.actionPerformed(null);
                }
            }
        });

        // Top section - Icon
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(CARD_COLOR);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(accentColor);
        
        topPanel.add(iconLabel);

        // Center section - Content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(CARD_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(descLabel);

        // Bottom section - Action indicator
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setBackground(CARD_COLOR);
        
        JLabel actionLabel = new JLabel("→");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionLabel.setForeground(accentColor);
        
        bottomPanel.add(actionLabel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel footerLabel = new JLabel("HabitTracker Pro v1.0 | Build better habits, live a better life");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(TEXT_SECONDARY);

        footerPanel.add(footerLabel);

        return footerPanel;
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new DashboardFrame("U123", "Sohel"));
    }
}