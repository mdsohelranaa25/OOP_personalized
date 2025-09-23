import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CompareFrame extends JFrame {
    private String currentUserId, currentUserName;
    private JPanel userListPanel, comparisonPanel;
    private JScrollPane userScrollPane, comparisonScrollPane;
    private boolean comparisonMode = false;
    private String selectedUserId, selectedUserName;
    
    // Professional Color Palette
    private static final Color PRIMARY_DARK = new Color(15, 23, 42);
    private static final Color PRIMARY_LIGHT = new Color(30, 41, 59);
    private static final Color ACCENT_PURPLE = new Color(139, 92, 246);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    // Data structures for habit tracking
    private Map<String, Map<String, Double>> userHabitAverages = new HashMap<>();
    private Set<String> allHabits = new HashSet<>();

    public CompareFrame(String userId, String userName) {
        this.currentUserId = userId;
        this.currentUserName = userName;

        setTitle("HabitTracker Pro - Compare Habits");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
        
        // Window border
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));

        loadRealUserData();
        initializeComponents();
    }

    private void loadRealUserData() {
        // Initialize habit names
        allHabits.addAll(Arrays.asList("Run", "Walking", "Study", "Playing", "Online Gaming", 
                                     "Programming", "Problem Solving", "Sleeping"));

        // Load all users from file
        List<String[]> allUsers = UserFileHandler.getAllUsers();
        
        // Calculate last week averages for all users
        for (String[] user : allUsers) {
            String uid = user[0];
            calculateUserWeeklyAverages(uid);
        }
        
        // Ensure current user is included even if not in the list
        calculateUserWeeklyAverages(currentUserId);
    }

    private void calculateUserWeeklyAverages(String userId) {
        Map<String, Double> userAverages = new HashMap<>();
        
        // Get last 7 days of habit data
        List<Map<String, Object>> weekData = UserFileHandler.readHabitsWithDate(userId, 7);
        
        // Calculate averages for each habit
        for (String habit : allHabits) {
            double total = 0.0;
            int validDays = 0;
            
            for (Map<String, Object> dayData : weekData) {
                @SuppressWarnings("unchecked")
                Map<String, Integer> habits = (Map<String, Integer>) dayData.get("habits");
                
                if (habits != null && habits.containsKey(habit)) {
                    Integer value = habits.get(habit);
                    if (value != null && value > 0) {
                        total += value;
                        validDays++;
                    }
                }
            }
            
            // Calculate average (skip days with no data)
            double average = validDays > 0 ? (total / validDays) : 0.0;
            userAverages.put(habit, average);
        }
        
        userHabitAverages.put(userId, userAverages);
    }

    private void initializeComponents() {
        // Create initial view selection panel
        add(createInitialPanel(), BorderLayout.CENTER);
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInitialPanel() {
        JPanel initialPanel = new JPanel(new GridBagLayout());
        initialPanel.setBackground(BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Compare with yourself card
        JPanel selfCard = createOptionCard(
            "Compare with Yourself", 
            "Analyze your habit trends over time",
            "📈", 
            ACCENT_BLUE,
            e -> showSelfComparisonDialog()
        );
        
        // Compare with others card  
        JPanel othersCard = createOptionCard(
            "Compare with Others",
            "See how you stack up against community members", 
            "👥",
            ACCENT_PURPLE,
            e -> {
                comparisonMode = false;
                refreshToUserList();
            }
        );
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        initialPanel.add(selfCard, gbc);
        
        gbc.gridx = 1;
        initialPanel.add(othersCard, gbc);
        
        return initialPanel;
    }

    private JPanel createOptionCard(String title, String description, String icon, Color accentColor, 
                                   java.awt.event.ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 8));
                for (int i = 0; i < 6; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i + 2, getWidth() - (i * 2), 
                             getHeight() - (i * 2) - 2, 16 + i, 16 + i));
                }
                
                // Card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Accent stripe
                g2d.setColor(accentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 6, 16, 16));
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(350, 200));

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BACKGROUND);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton actionBtn = createModernButton("Select", accentColor);
        actionBtn.addActionListener(action);
        actionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(iconLabel);
        contentPanel.add(titleLabel);
        contentPanel.add(descLabel);
        contentPanel.add(actionBtn);

        card.add(contentPanel, BorderLayout.CENTER);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(249, 250, 251));
                titleLabel.setForeground(accentColor);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BACKGROUND);
                titleLabel.setForeground(TEXT_PRIMARY);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (action != null) {
                    action.actionPerformed(null);
                }
            }
        });

        return card;
    }

    private void showSelfComparisonDialog() {
        // Simple self-analysis dialog
        StringBuilder analysis = new StringBuilder();
        analysis.append("📊 Your Last Week Habit Analysis:\n\n");
        
        Map<String, Double> myHabits = userHabitAverages.get(currentUserId);
        if (myHabits == null || myHabits.isEmpty()) {
            analysis.append("No habit data found for the last week.\n");
            analysis.append("Start tracking your habits to see your progress!");
        } else {
            double totalAvg = myHabits.values().stream().mapToDouble(Double::doubleValue).sum() / myHabits.size();
            analysis.append(String.format("Overall Average: %.1f units per day\n\n", totalAvg));
            
            // Sort habits by performance
            myHabits.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    String habit = entry.getKey();
                    double avg = entry.getValue();
                    String unit = habit.equals("Problem Solving") ? "problems" : "hours";
                    String performance = avg >= 2.0 ? "🟢 Great" : avg >= 1.0 ? "🟡 Good" : "🔴 Needs Work";
                    analysis.append(String.format("• %s: %.1f %s/day %s\n", habit, avg, unit, performance));
                });
        }
        
        JTextArea textArea = new JTextArea(analysis.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(BACKGROUND);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Your Habit Analysis", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshToUserList() {
        getContentPane().removeAll();
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createUserListPanel(), BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);
        revalidate();
        repaint();
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
                    0, 0, ACCENT_BLUE,
                    getWidth(), getHeight(), ACCENT_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle pattern
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int x = 0; x < getWidth(); x += 40) {
                    for (int y = 0; y < getHeight(); y += 40) {
                        g2d.drawLine(x, y, x + 20, y + 20);
                        g2d.drawLine(x + 20, y, x, y + 20);
                    }
                }
            }
        };
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Community Comparison");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel(comparisonMode ? 
            "Comparing " + currentUserName + " vs " + selectedUserName : 
            "Select a community member to compare with " + currentUserName);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(230, 235, 245));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        headerPanel.add(titlePanel);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createUserListPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(BACKGROUND);

        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(BACKGROUND);
        userListPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Info header
        JPanel headerInfo = new JPanel();
        headerInfo.setLayout(new BoxLayout(headerInfo, BoxLayout.Y_AXIS));
        headerInfo.setBackground(BACKGROUND);
        headerInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        List<String[]> allUsers = UserFileHandler.getAllUsers();
        int otherUsersCount = 0;
        for (String[] user : allUsers) {
            if (!user[0].equals(currentUserId)) {
                otherUsersCount++;
            }
        }

        JLabel statsLabel = new JLabel("Community Members (" + otherUsersCount + " active users)");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsLabel.setForeground(TEXT_PRIMARY);
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel("Click on any member to compare your last week's habit performance");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        headerInfo.add(statsLabel);
        headerInfo.add(infoLabel);

        userListPanel.add(headerInfo);
        userListPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // User grid
        JPanel userGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        userGrid.setBackground(BACKGROUND);

        Color[] cardColors = {ACCENT_PURPLE, ACCENT_BLUE, ACCENT_GREEN, ACCENT_ORANGE, 
                             ACCENT_RED, ACCENT_PINK};
        int colorIndex = 0;

        for (String[] user : allUsers) {
            String uid = user[0];
            String uname = user[1];
            
            // Skip current user
            if (uid.equals(currentUserId)) {
                continue;
            }

            Color cardColor = cardColors[colorIndex % cardColors.length];
            
            // Calculate user's overall performance
            Map<String, Double> userHabits = userHabitAverages.get(uid);
            double overallAvg = 0.0;
            if (userHabits != null && !userHabits.isEmpty()) {
                overallAvg = userHabits.values().stream().mapToDouble(Double::doubleValue).sum() / userHabits.size();
            }
            
            String badge = overallAvg >= 3.0 ? "Habit Champion" : 
                          overallAvg >= 2.0 ? "Consistent Tracker" :
                          overallAvg >= 1.0 ? "Getting Started" : "New Member";
            
            String rating = String.format("%.1f/5.0", Math.min(5.0, overallAvg));

            JPanel userCard = createUserCard(uid, uname, badge, rating, cardColor);
            userGrid.add(userCard);
            colorIndex++;
        }

        userListPanel.add(userGrid);

        userScrollPane = new JScrollPane(userListPanel);
        userScrollPane.setBackground(BACKGROUND);
        userScrollPane.setBorder(BorderFactory.createEmptyBorder());
        userScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollPane(userScrollPane);

        containerPanel.add(userScrollPane, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createUserCard(String userId, String userName, String badge, String rating, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 8));
                for (int i = 0; i < 6; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i + 2, getWidth() - (i * 2), 
                             getHeight() - (i * 2) - 2, 16 + i, 16 + i));
                }
                
                // Card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Accent stripe
                g2d.setColor(accentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 6, 16, 16));
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(300, 140));

        // User info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BACKGROUND);

        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badgeLabel = new JLabel(badge);
        badgeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        badgeLabel.setForeground(TEXT_SECONDARY);
        badgeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        badgeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));

        JLabel ratingLabel = new JLabel("Score: " + rating);
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ratingLabel.setForeground(accentColor);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(badgeLabel);
        infoPanel.add(ratingLabel);

        // Compare button
        JButton compareBtn = createActionButton("Compare", accentColor);
        compareBtn.addActionListener(e -> {
            selectedUserId = userId;
            selectedUserName = userName;
            comparisonMode = true;
            showComparison();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.add(compareBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(249, 250, 251));
                nameLabel.setForeground(accentColor);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BACKGROUND);
                nameLabel.setForeground(TEXT_PRIMARY);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                selectedUserId = userId;
                selectedUserName = userName;
                comparisonMode = true;
                showComparison();
            }
        });

        return card;
    }

    private void showComparison() {
        getContentPane().removeAll();
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createComparisonPanel(), BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private JPanel createComparisonPanel() {
        comparisonPanel = new JPanel();
        comparisonPanel.setLayout(new BoxLayout(comparisonPanel, BoxLayout.Y_AXIS));
        comparisonPanel.setBackground(BACKGROUND);
        comparisonPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Summary cards
        JPanel summaryPanel = createSummaryCards();
        comparisonPanel.add(summaryPanel);
        comparisonPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Habit comparison charts
        JPanel chartsPanel = createHabitCharts();
        comparisonPanel.add(chartsPanel);

        comparisonScrollPane = new JScrollPane(comparisonPanel);
        comparisonScrollPane.setBackground(BACKGROUND);
        comparisonScrollPane.setBorder(BorderFactory.createEmptyBorder());
        comparisonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        comparisonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollPane(comparisonScrollPane);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(comparisonScrollPane, BorderLayout.CENTER);
        return wrapperPanel;
    }

    private JPanel createSummaryCards() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        summaryPanel.setBackground(BACKGROUND);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Current user summary
        JPanel currentUserCard = createSummaryCard(
            currentUserName + " (You)",
            calculateOverallAverage(currentUserId),
            "Your Performance",
            ACCENT_BLUE,
            "🎯"
        );

        // Selected user summary
        JPanel selectedUserCard = createSummaryCard(
            selectedUserName,
            calculateOverallAverage(selectedUserId),
            "Their Performance",
            ACCENT_PURPLE,
            "👤"
        );

        summaryPanel.add(currentUserCard);
        summaryPanel.add(selectedUserCard);

        return summaryPanel;
    }

    private JPanel createSummaryCard(String title, double average, String subtitle, Color accentColor, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 10));
                for (int i = 0; i < 8; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i + 2, getWidth() - (i * 2), 
                             getHeight() - (i * 2) - 2, 20, 20));
                }
                
                // Card background with gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, CARD_BACKGROUND,
                    getWidth(), getHeight(), 
                    new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 15)
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Accent border
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 20, 20));
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setPreferredSize(new Dimension(0, 160));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(accentColor);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0, 0, 0, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 10, 0));

        String unit = "avg/day";
        JLabel averageLabel = new JLabel(String.format("%.1f %s", average, unit));
        averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        averageLabel.setForeground(accentColor);
        averageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(subtitleLabel);
        contentPanel.add(averageLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createHabitCharts() {
        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new BoxLayout(chartsPanel, BoxLayout.Y_AXIS));
        chartsPanel.setBackground(BACKGROUND);

        // Title
        JLabel titleLabel = new JLabel("Habit-wise Comparison (Last Week Average)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        chartsPanel.add(titleLabel);

        // Individual habit charts
        Map<String, Double> currentUserHabits = userHabitAverages.get(currentUserId);
        Map<String, Double> selectedUserHabits = userHabitAverages.get(selectedUserId);

        if (currentUserHabits == null) {
            currentUserHabits = new HashMap<>();
            for (String habit : allHabits) {
                currentUserHabits.put(habit, 0.0);
            }
        }
        
        if (selectedUserHabits == null) {
            selectedUserHabits = new HashMap<>();
            for (String habit : allHabits) {
                selectedUserHabits.put(habit, 0.0);
            }
        }

        for (String habit : allHabits) {
            double currentUserValue = currentUserHabits.getOrDefault(habit, 0.0);
            double selectedUserValue = selectedUserHabits.getOrDefault(habit, 0.0);

            JPanel habitChart = createHabitChart(habit, currentUserValue, selectedUserValue);
            chartsPanel.add(habitChart);
            chartsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        return chartsPanel;
    }

    private JPanel createHabitChart(String habitName, double currentUserPercentage, double selectedUserPercentage) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
            }
        };
        
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        chartPanel.setPreferredSize(new Dimension(0, 160));
        chartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Left side - Habit info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BACKGROUND);
        infoPanel.setPreferredSize(new Dimension(200, 0));

        JLabel habitLabel = new JLabel(getHabitIcon(habitName) + " " + habitName);
        habitLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        habitLabel.setForeground(TEXT_PRIMARY);

        String unit = habitName.equals("Problem Solving") ? " problems" : " hours";
        JLabel comparisonLabel = new JLabel(String.format("You: %.1f%s vs Them: %.1f%s", 
                                                          currentUserPercentage, unit, 
                                                          selectedUserPercentage, unit));
        comparisonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comparisonLabel.setForeground(TEXT_SECONDARY);
        comparisonLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        // Difference indicator
        double difference = currentUserPercentage - selectedUserPercentage;
        String diffText = String.format("%+.1f%s", difference, unit);
        JLabel diffLabel = new JLabel(diffText);
        diffLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        diffLabel.setForeground(difference >= 0 ? ACCENT_GREEN : ACCENT_RED);

        infoPanel.add(habitLabel);
        infoPanel.add(comparisonLabel);
        infoPanel.add(diffLabel);

        // Right side - Chart
        JPanel chartAreaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth() - 40;
                int height = 80;
                int barHeight = 25;
                int x = 20;
                int y1 = 20;
                int y2 = y1 + barHeight + 15;

                // Find max value for scaling
                double maxValue = Math.max(currentUserPercentage, selectedUserPercentage);
                if (maxValue == 0) maxValue = 1; // Prevent division by zero
                
                // Scale to reasonable width (max 8 hours for most habits, 10 for problem solving)
                double scaleMax = habitName.equals("Problem Solving") ? 10.0 : 8.0;
                maxValue = Math.max(maxValue, scaleMax);

                // Current user bar
                drawProgressBar(g2d, x, y1, width, barHeight, currentUserPercentage, maxValue, 
                               ACCENT_BLUE, currentUserName + " (You)");

                // Selected user bar
                drawProgressBar(g2d, x, y2, width, barHeight, selectedUserPercentage, maxValue, 
                               ACCENT_PURPLE, selectedUserName);
            }

            private void drawProgressBar(Graphics2D g2d, int x, int y, int width, int height, 
                                       double value, double maxValue, Color color, String label) {
                // Background
                g2d.setColor(new Color(229, 231, 235));
                g2d.fill(new RoundRectangle2D.Float(x, y, width, height, height/2, height/2));
                
                // Progress fill
                int fillWidth = (int) (width * (value / maxValue));
                if (fillWidth > 0) {
                    g2d.setColor(color);
                    g2d.fill(new RoundRectangle2D.Float(x, y, fillWidth, height, height/2, height/2));
                }
                
                // Value text inside or next to bar
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2d.getFontMetrics();
                String valueText = String.format("%.1f", value);
                int textWidth = fm.stringWidth(valueText);
                
                if (fillWidth > textWidth + 16) {
                    // Text inside bar
                    g2d.setColor(Color.WHITE);
                    int textX = x + fillWidth - textWidth - 8;
                    int textY = y + (height + fm.getAscent()) / 2 - 2;
                    g2d.drawString(valueText, textX, textY);
                } else {
                    // Text next to bar
                    g2d.setColor(TEXT_PRIMARY);
                    int textX = x + fillWidth + 8;
                    int textY = y + (height + fm.getAscent()) / 2 - 2;
                    g2d.drawString(valueText, textX, textY);
                }
                
                // Label above bar
                g2d.setColor(TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2d.drawString(label, x, y - 5);
            }
        };

        chartAreaPanel.setBackground(CARD_BACKGROUND);
        chartAreaPanel.setPreferredSize(new Dimension(0, 100));

        chartPanel.add(infoPanel, BorderLayout.WEST);
        chartPanel.add(chartAreaPanel, BorderLayout.CENTER);

        return chartPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        navPanel.setBackground(BACKGROUND);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        if (comparisonMode) {
            JButton backToListBtn = createModernButton("Back to User List", ACCENT_ORANGE);
            backToListBtn.addActionListener(e -> {
                comparisonMode = false;
                selectedUserId = null;
                selectedUserName = null;
                refreshToUserList();
            });
            navPanel.add(backToListBtn);
        }

        JButton backToDashboardBtn = createModernButton("Back to Dashboard", ACCENT_RED);
        backToDashboardBtn.addActionListener(e -> {
            new DashboardFrame(currentUserId, currentUserName).setVisible(true);
            dispose();
        });
        navPanel.add(backToDashboardBtn);

        return navPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BACKGROUND);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));
        
        JLabel footerLabel = new JLabel("Compare, learn, and grow together with the community");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    // Helper methods
    private String getHabitIcon(String habitName) {
        switch (habitName) {
            case "Run": return "🏃";
            case "Walking": return "🚶";
            case "Study": return "📚";
            case "Playing": return "🎮";
            case "Online Gaming": return "🎯";
            case "Programming": return "💻";
            case "Problem Solving": return "🧩";
            case "Sleeping": return "💤";
            default: return "📋";
        }
    }

    private double calculateOverallAverage(String userId) {
        Map<String, Double> userHabits = userHabitAverages.get(userId);
        if (userHabits == null || userHabits.isEmpty()) {
            return 0.0;
        }
        
        double total = userHabits.values().stream()
            .filter(value -> value != null && value > 0)
            .mapToDouble(Double::doubleValue)
            .sum();
        
        long count = userHabits.values().stream()
            .filter(value -> value != null && value > 0)
            .count();
            
        return count > 0 ? total / count : 0.0;
    }

    private JButton createActionButton(String text, Color bgColor) {
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
                        Math.min(255, bgColor.getRed() + 25),
                        Math.min(255, bgColor.getGreen() + 25),
                        Math.min(255, bgColor.getBlue() + 25)
                    );
                }
                
                g2d.setColor(currentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 32));
        button.setMaximumSize(new Dimension(100, 32));
        
        return button;
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
        button.setPreferredSize(new Dimension(180, 40));
        
        return button;
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Custom scroll bar styling
        scrollPane.getVerticalScrollBar().setBackground(BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(190, 190, 190);
                this.trackColor = BACKGROUND;
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
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new CompareFrame("user1", "Test User").setVisible(true);
        });
    }
}