import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CompareWeekFrame extends JFrame {
    private String userId, userName;
    
    // Professional Color Palette
    private static final Color PRIMARY_DARK = new Color(17, 24, 39);
    private static final Color PRIMARY_LIGHT = new Color(31, 41, 55);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_YELLOW = new Color(245, 158, 11);
    private static final Color ACCENT_PURPLE = new Color(139, 92, 246);
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color SUCCESS_LIGHT = new Color(220, 252, 231);
    private static final Color WARNING_LIGHT = new Color(254, 243, 199);
    private static final Color DANGER_LIGHT = new Color(254, 226, 226);

    public CompareWeekFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("HabitTracker Pro - Weekly Analysis");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(700, 600));
        
        // Modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
        
        // Window border
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));

        initializeComponents();
    }

    private void initializeComponents() {
        // Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Main Content Panel with Scroll
        add(createMainPanel(), BorderLayout.CENTER);
        
        // Footer Panel
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_DARK,
                    getWidth(), getHeight(), PRIMARY_LIGHT
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Modern wave pattern
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < getWidth(); i += 40) {
                    g2d.drawLine(i, 0, i + 20, getHeight());
                }
            }
        };
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 130));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // Logo and Title Section
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("📈");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        
        JLabel titleLabel = new JLabel("Weekly Progress Analysis");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("7-Day Habit Comparison for " + userName);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(203, 213, 224));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        headerPanel.add(logoPanel);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setLayout(new BorderLayout());

        // Content panel with scroll support
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Summary Card
        contentPanel.add(createSummaryCard());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Detailed Analysis Cards
        contentPanel.add(createAnalysisSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Recommendations Section
        contentPanel.add(createRecommendationsCard());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Back button
        contentPanel.add(createBackButtonPanel());

        // Add scroll capability
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Custom scroll bar styling
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_COLOR;
                this.trackColor = BACKGROUND;
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createSummaryCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Card header
        JLabel headerLabel = new JLabel("📊 Weekly Summary");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(TEXT_PRIMARY);

        // Summary stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(CARD_BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Improved habits
        statsPanel.add(createStatItem("⬆", "3", "Improved", ACCENT_GREEN));
        
        // Same performance
        statsPanel.add(createStatItem("➡", "2", "Unchanged", ACCENT_BLUE));
        
        // Declined habits
        statsPanel.add(createStatItem("⬇", "1", "Declined", ACCENT_ORANGE));

        card.add(headerLabel, BorderLayout.NORTH);
        card.add(statsPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatItem(String icon, String number, String label, Color color) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBackground(CARD_BACKGROUND);
        item.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        numberLabel.setForeground(color);
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_SECONDARY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        item.add(iconLabel);
        item.add(Box.createRigidArea(new Dimension(0, 5)));
        item.add(numberLabel);
        item.add(Box.createRigidArea(new Dimension(0, 5)));
        item.add(textLabel);

        return item;
    }

    private JPanel createAnalysisSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BACKGROUND);

        // Section title
        JLabel sectionTitle = new JLabel("📋 Detailed Analysis");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        section.add(sectionTitle);

        // Habit analysis items
        section.add(createHabitAnalysisCard("💪", "Exercise", "Improved", "5 days this week vs 3 days last week", ACCENT_GREEN, SUCCESS_LIGHT));
        section.add(Box.createRigidArea(new Dimension(0, 10)));
        
        section.add(createHabitAnalysisCard("💧", "Hydration", "Improved", "8 glasses daily vs 6 glasses last week", ACCENT_GREEN, SUCCESS_LIGHT));
        section.add(Box.createRigidArea(new Dimension(0, 10)));
        
        section.add(createHabitAnalysisCard("😴", "Sleep", "Unchanged", "7.5 hours average, same as last week", ACCENT_BLUE, new Color(239, 246, 255)));
        section.add(Box.createRigidArea(new Dimension(0, 10)));
        
        section.add(createHabitAnalysisCard("📖", "Reading", "Unchanged", "30 minutes daily, consistent pattern", ACCENT_BLUE, new Color(239, 246, 255)));
        section.add(Box.createRigidArea(new Dimension(0, 10)));
        
        section.add(createHabitAnalysisCard("📱", "Screen Time", "Increased", "6 hours daily vs 4.5 hours last week", ACCENT_ORANGE, WARNING_LIGHT));

        return section;
    }

    private JPanel createHabitAnalysisCard(String icon, String habit, String status, String details, Color statusColor, Color bgColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background
                g2d.setColor(bgColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        card.setPreferredSize(new Dimension(0, 80));

        // Left - Icon and Habit name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(bgColor);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        
        JLabel habitLabel = new JLabel(habit);
        habitLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        habitLabel.setForeground(TEXT_PRIMARY);
        
        leftPanel.add(iconLabel);
        leftPanel.add(habitLabel);

        // Center - Details
        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsLabel.setForeground(TEXT_SECONDARY);
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Right - Status
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(bgColor);
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(statusColor);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(statusColor, 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(CARD_BACKGROUND);
        
        rightPanel.add(statusLabel);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(detailsLabel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createRecommendationsCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JLabel headerLabel = new JLabel("💡 Smart Recommendations");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(TEXT_PRIMARY);

        // Recommendations list
        JPanel recPanel = new JPanel();
        recPanel.setLayout(new BoxLayout(recPanel, BoxLayout.Y_AXIS));
        recPanel.setBackground(CARD_BACKGROUND);
        recPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        recPanel.add(createRecommendationItem("🎯", "Keep up the great work with exercise! Try adding 10 more minutes to your routine."));
        recPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        recPanel.add(createRecommendationItem("⏰", "Consider setting specific screen time limits to maintain your progress."));
        recPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        recPanel.add(createRecommendationItem("🌙", "Your sleep schedule is consistent - excellent foundation for other habits!"));

        card.add(headerLabel, BorderLayout.NORTH);
        card.add(recPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRecommendationItem(String icon, String text) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(CARD_BACKGROUND);
        item.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        JLabel textLabel = new JLabel("<html>" + text + "</html>");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_SECONDARY);

        item.add(iconLabel, BorderLayout.WEST);
        item.add(textLabel, BorderLayout.CENTER);

        return item;
    }

    private JPanel createBackButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton backBtn = createModernButton("← Back to Self Comparison", ACCENT_PURPLE);
        backBtn.addActionListener(e -> {
            new CompareWithSelfFrame(userId, userName).setVisible(true);
            dispose();
        });
        
        buttonPanel.add(backBtn);
        return buttonPanel;
    }

    private JPanel createStyledCard() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 10));
                for (int i = 0; i < 5; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i, getWidth() - (i * 2), getHeight() - (i * 2), 16 + i, 16 + i));
                }
                
                // Card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Card border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
            }
        };
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color currentColor = bgColor;
                if (getModel().isPressed()) {
                    currentColor = bgColor.darker();
                } else if (getModel().isRollover()) {
                    currentColor = new Color(
                        Math.min(255, bgColor.getRed() + 20),
                        Math.min(255, bgColor.getGreen() + 20),
                        Math.min(255, bgColor.getBlue() + 20)
                    );
                }
                
                g2d.setColor(currentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 45));
        
        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(248, 250, 252));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(18, 0, 18, 0)
        ));
        
        JLabel footerLabel = new JLabel("📊 Data-driven insights to improve your habit journey");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CompareWeekFrame("testUser", "Test User").setVisible(true);
        });
    }
}



