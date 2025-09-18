import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class HabitFrame extends JFrame {
    private String userId;
    private String userName;

    // Cute Green-based color palette
    private static final Color PRIMARY_GREEN = new Color(34, 197, 94);     // Emerald-500
    private static final Color SECONDARY_GREEN = new Color(16, 185, 129);  // Emerald-600  
    private static final Color ACCENT_TEAL = new Color(20, 184, 166);      // Teal-500
    private static final Color LIGHT_GREEN = new Color(187, 247, 208);     // Green-200
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);     // Green-500
    private static final Color DANGER_PINK = new Color(244, 114, 182);     // Pink-400
    private static final Color MINT_50 = new Color(240, 253, 244);         // Green-50
    private static final Color MINT_100 = new Color(220, 252, 231);        // Green-100
    private static final Color SAGE_200 = new Color(187, 247, 208);        // Green-200
    private static final Color FOREST_600 = new Color(22, 163, 74);        // Green-600
    private static final Color FOREST_700 = new Color(21, 128, 61);        // Green-700
    private static final Color CHARCOAL = new Color(31, 41, 55);           // Gray-800
    private static final Color WHITE = Color.WHITE;

    public HabitFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        // Window setup
        setTitle("Habit Tracker - " + userName);
        setSize(600, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Enable resizing for better scroll experience
        getContentPane().setBackground(MINT_50);
        setLayout(new BorderLayout(0, 0));

        // Custom window icon (optional)
        try {
            setIconImage(createCircleIcon(32, PRIMARY_GREEN));
        } catch (Exception e) {
            // Icon loading failed, continue without icon
        }

        initializeComponents();
        setVisible(false); // Will be set visible by caller
    }

    private void initializeComponents() {
        // Create a main container panel that includes everything
        JPanel allContentPanel = new JPanel();
        allContentPanel.setLayout(new BorderLayout());
        allContentPanel.setBackground(MINT_50);
        
        // Add all components to the main container
        allContentPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        allContentPanel.add(createMainPanel(), BorderLayout.CENTER);
        allContentPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        // Wrap everything in a scroll pane
        JScrollPane scrollPane = new JScrollPane(allContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Custom scrollbar styling
        scrollPane.getVerticalScrollBar().setBackground(MINT_50);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = SAGE_200;
                this.trackColor = MINT_100;
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREEN),
            BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));

        // Title and subtitle
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);

        JLabel titleLabel = new JLabel("🌱 Add Daily Habits");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(CHARCOAL);

        JLabel subtitleLabel = new JLabel("✨ Track your daily activities for " + userName);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(FOREST_600);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Date label
        JLabel dateLabel = new JLabel("📅 " + LocalDate.now().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setForeground(PRIMARY_GREEN);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(MINT_50);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        mainPanel.setLayout(new BorderLayout());

        // Form container
        JPanel formContainer = new JPanel();
        formContainer.setBackground(WHITE);
        formContainer.setBorder(new RoundedBorder(16, SAGE_200));
        formContainer.setLayout(new BorderLayout());

        // Form header
        JPanel formHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formHeader.setBackground(WHITE);
        formHeader.setBorder(BorderFactory.createEmptyBorder(20, 24, 0, 24));
        
        JLabel formTitle = new JLabel("📊 Daily Activity Log");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(CHARCOAL);
        formHeader.add(formTitle);

        // Form content
        JPanel formPanel = createFormPanel();

        formContainer.add(formHeader, BorderLayout.NORTH);
        formContainer.add(formPanel, BorderLayout.CENTER);

        mainPanel.add(formContainer, BorderLayout.CENTER);
        
        // Remove fixed preferred size to let content flow naturally
        
        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        String[] habits = {"Run", "Walking", "Study", "Playing", "Online Gaming", "Programming", "Problem Solving", "Sleeping"};
        Map<String, JTextField> habitFields = new LinkedHashMap<>();
        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 0; i < habits.length; i++) {
            String habit = habits[i];
            String unit = habit.equals("Problem Solving") ? "problems" : "hours";
            String labelText = habit + " (" + unit + ")";

            // Label
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(0, 0, 16, 16);
            gbc.weightx = 0.4;
            gbc.fill = GridBagConstraints.NONE;

            JLabel label = new JLabel(labelText);
            label.setFont(new Font("Segoe UI", Font.BOLD, 15));
            label.setForeground(FOREST_700);
            formPanel.add(label, gbc);

            // Input field
            gbc.gridx = 1;
            gbc.weightx = 0.6;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 16, 0);

            JTextField field = createModernTextField();
            habitFields.put(habit, field);
            formPanel.add(field, gbc);
        }

        // Store habitFields reference for action listeners
        this.habitFields = habitFields;
        this.habits = habits;

        return formPanel;
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(220, 44));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, SAGE_200),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(MINT_100);
        field.setForeground(CHARCOAL);

        // Focus effects
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(10, PRIMARY_GREEN),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                field.setBackground(WHITE);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(10, SAGE_200),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                field.setBackground(MINT_100);
            }
        });

        return field;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(MINT_50);
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 16, 24));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, LIGHT_GREEN),
            BorderFactory.createEmptyBorder(0, 32, 0, 32)
        ));

        JButton backButton = createModernButton("⬅ Back", FOREST_600, false);
        JButton saveButton = createModernButton("💾 Save Habits", PRIMARY_GREEN, true);

        // Button actions
        backButton.addActionListener(e -> {
            new DashboardFrame(userId, userName).setVisible(true);
            dispose();
        });

        saveButton.addActionListener(e -> handleSaveAction());

        footerPanel.add(backButton);
        footerPanel.add(saveButton);

        return footerPanel;
    }

    private JButton createModernButton(String text, Color bgColor, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(isPrimary ? WHITE : CHARCOAL);
        button.setBackground(isPrimary ? bgColor : MINT_100);
        button.setPreferredSize(new Dimension(140, 44));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new RoundedBorder(10, isPrimary ? bgColor : SAGE_200));

        // Hover effects
        Color originalBg = button.getBackground();
        Color hoverBg = isPrimary ? SECONDARY_GREEN : SAGE_200;

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(originalBg);
            }
        });

        return button;
    }

    // Store these as instance variables for access in action handlers
    private Map<String, JTextField> habitFields;
    private String[] habits;

    private void handleSaveAction() {
        Map<String, Double> habitData = new LinkedHashMap<>();
        double totalHours = 0.0;

        for (String habit : habits) {
            String val = habitFields.get(habit).getText().trim();
            double num = 0.0;
            try {
                if (!val.isEmpty()) {
                    num = Double.parseDouble(val);
                    if (num < 0) {
                        showErrorDialog("Value for " + habit + " cannot be negative");
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter a valid number for " + habit);
                return;
            }
            habitData.put(habit, num);
            if (!habit.equals("Problem Solving")) {
                totalHours += num;
            }
        }

        if (totalHours > 24) {
            showErrorDialog("Total hours cannot exceed 24 hours per day");
            return;
        }

        // Save to file with today's date
        String today = LocalDate.now().toString();
        Map<String, Integer> habitDataInt = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : habitData.entrySet()) {
            habitDataInt.put(entry.getKey(), entry.getValue().intValue());
        }
        
        UserFileHandler.saveHabit(userId, today, habitDataInt);
        showSuccessDialog("Habits saved successfully for " + today);
        
        // Clear all fields
        habitFields.values().forEach(f -> f.setText(""));
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, "❌ " + message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, "🎉 " + message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private Image createCircleIcon(int size, Color color) {
        Image img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(2, 2, size - 4, size - 4);
        g2.dispose();
        return img;
    }

    // Custom rounded border class
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HabitFrame("U123", "Sohel").setVisible(true));
    }
}