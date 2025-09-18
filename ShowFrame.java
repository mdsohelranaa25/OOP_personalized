import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ShowFrame extends JFrame {
    private String userId;
    private String userName;
    
    // Modern Color Palette
    private static final Color PRIMARY_DARK = new Color(26, 32, 44);
    private static final Color PRIMARY_LIGHT = new Color(45, 55, 72);
    private static final Color ACCENT_BLUE = new Color(66, 153, 225);
    private static final Color ACCENT_GREEN = new Color(72, 187, 120);
    private static final Color ACCENT_RED = new Color(245, 101, 101);
    private static final Color ACCENT_PURPLE = new Color(128, 90, 213);
    private static final Color BACKGROUND = new Color(247, 250, 252);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(26, 32, 44);
    private static final Color TEXT_SECONDARY = new Color(74, 85, 104);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    public ShowFrame(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Personalized Habits - " + userName);
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        getContentPane().setBackground(BACKGROUND);
        
        // Add window shadow effect
        setUndecorated(false);
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));

        showMainOptions();
    }

    private void showMainOptions() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Top Header Panel with gradient
        JPanel headerPanel = createHeaderPanel("Welcome Back, " + userName + "!", 
                                             "Manage your habits and track your progress");
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Cards container
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardsPanel.setBackground(BACKGROUND);

        // Create feature cards
        JPanel habitsCard = createFeatureCard(
            "📊", "Your Habits", 
            "View and track your personal habit progress",
            ACCENT_BLUE,
            e -> showUserOptions(userId)
        );

        JPanel usersCard = createFeatureCard(
            "👥", "Explore Community", 
            "Discover what others are achieving",
            ACCENT_GREEN,
            e -> showOtherUsers()
        );

        JPanel dashboardCard = createFeatureCard(
            "🏠", "Dashboard", 
            "Return to your main dashboard",
            ACCENT_RED,
            e -> {
                new DashboardFrame(userId, userName).setVisible(true);
                dispose();
            }
        );

        cardsPanel.add(habitsCard);
        cardsPanel.add(usersCard);
        cardsPanel.add(dashboardCard);

        mainPanel.add(cardsPanel, BorderLayout.CENTER);

        // Bottom stats panel
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void showUserOptions(String uid) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel("Habit Analytics", 
                                             "Track habits for: " + UserFileHandler.getUserName(uid));
        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        // Options grid
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        optionsPanel.setBackground(BACKGROUND);

        // Create option cards
        JPanel todayCard = createOptionCard(
            "📅", "Today's Progress", 
            "View today's habit completion status",
            ACCENT_PURPLE,
            e -> new HabitViewer("Today's Habits", uid, "today").setVisible(true)
        );

        JPanel weekCard = createOptionCard(
            "📈", "Weekly Overview", 
            "Analyze your last 7 days performance",
            ACCENT_BLUE,
            e -> new HabitViewer("Last 7 Days Habits", uid, "7days").setVisible(true)
        );

        JPanel backCard = createOptionCard(
            "↩️", "Back", 
            "Return to main menu",
            ACCENT_RED,
            e -> showMainOptions()
        );

        // Create a spacer panel for visual balance
        JPanel spacerCard = new JPanel();
        spacerCard.setBackground(BACKGROUND);

        optionsPanel.add(todayCard);
        optionsPanel.add(weekCard);
        optionsPanel.add(spacerCard);
        optionsPanel.add(backCard);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void showOtherUsers() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel("Community Hub", 
                                             "Explore and connect with other habit trackers");
        add(headerPanel, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Table panel with modern styling
        JPanel tablePanel = createModernTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton backBtn = createModernButton("← Back to Menu", ACCENT_RED);
        backBtn.addActionListener(e -> showMainOptions());
        bottomPanel.add(backBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel(String title, String subtitle) {
        JPanel panel = new JPanel() {
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
            }
        };
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(0, 120));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(203, 213, 224));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        panel.add(titleLabel);
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createFeatureCard(String icon, String title, String description, 
                                   Color accentColor, java.awt.event.ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background with rounded corners
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15));
                
                // Top accent bar
                g2d.setColor(accentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 4, 15, 15));
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setPreferredSize(new Dimension(280, 200));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(titleLabel);
        card.add(descLabel);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BACKGROUND);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(null);
            }
        });

        return card;
    }

    private JPanel createOptionCard(String icon, String title, String description, 
                                  Color accentColor, java.awt.event.ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 12, 12));
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(titleLabel);
        card.add(descLabel);
        card.add(Box.createVerticalGlue());

        // Enhanced hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(247, 250, 252));
                titleLabel.setForeground(accentColor.darker());
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BACKGROUND);
                titleLabel.setForeground(accentColor);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(null);
            }
        });

        return card;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
        JLabel statsLabel = new JLabel("🎯 Building better habits, one day at a time");
        statsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statsLabel.setForeground(TEXT_SECONDARY);
        
        panel.add(statsLabel);
        return panel;
    }

    private JPanel createModernTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        List<String[]> users = UserFileHandler.getAllUsers();
        String[] columns = {"User ID", "Name", "Status"};
        Object[][] data = new Object[users.size()][3];
        
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i)[0];
            data[i][1] = users.get(i)[1];
            data[i][2] = "Active"; // You can modify this based on actual status
        }

        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Modern table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setBackground(CARD_BACKGROUND);
        table.setSelectionBackground(new Color(237, 242, 247));
        table.setSelectionForeground(TEXT_PRIMARY);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(247, 250, 252));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setReorderingAllowed(false);

        // Custom cell renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(CARD_BACKGROUND);
                    } else {
                        c.setBackground(new Color(250, 252, 254));
                    }
                }
                
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                
                if (column == 2) { // Status column
                    setForeground(ACCENT_GREEN);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(TEXT_PRIMARY);
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
                
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String selectedId = (String) table.getValueAt(row, 0);
                        showUserOptions(selectedId);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(CARD_BACKGROUND);
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);

        // Info label
        JLabel infoLabel = new JLabel("💡 Double-click on any user to view their habits");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                
                g2d.setColor(getForeground());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 40));
        
        return button;
    }
}