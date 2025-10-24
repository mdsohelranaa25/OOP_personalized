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
    
    // Enhanced Professional Dark Theme Color Palette
    private static final Color BACKGROUND_PRIMARY = new Color(8, 14, 31);         // Deep navy background
    private static final Color BACKGROUND_SECONDARY = new Color(17, 24, 47);      // Card background
    private static final Color BACKGROUND_ELEVATED = new Color(25, 35, 64);       // Elevated elements
    
    private static final Color ACCENT_ELECTRIC_BLUE = new Color(64, 145, 255);    // Primary electric blue
    private static final Color ACCENT_CYBER_PURPLE = new Color(139, 69, 255);     // Vibrant purple
    private static final Color ACCENT_NEON_GREEN = new Color(52, 211, 153);       // Success/positive
    private static final Color ACCENT_CORAL = new Color(255, 107, 107);           // Warning/negative
    private static final Color ACCENT_GOLD = new Color(255, 193, 7);              // Highlight
    private static final Color ACCENT_PINK = new Color(255, 64, 129);             // Secondary accent
    
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);           // Pure white text
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);         // Muted text
    private static final Color TEXT_TERTIARY = new Color(100, 116, 139);          // Subtle text
    
    private static final Color BORDER_SUBTLE = new Color(30, 41, 59, 120);        // Subtle borders
    private static final Color BORDER_ACCENT = new Color(64, 145, 255, 80);       // Glowing borders
    
    private static final Color GLASS_OVERLAY = new Color(255, 255, 255, 10);      // Glass morphism
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 25);             // Drop shadows

    // Data structures for habit tracking
    private Map<String, Map<String, Double>> userHabitAverages = new HashMap<>();
    private Set<String> allHabits = new HashSet<>();

    public CompareFrame(String userId, String userName) {
        this.currentUserId = userId;
        this.currentUserName = userName;

        setTitle("Personalized Habit Tracker - Compare Habits");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(900, 650));
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_PRIMARY);
        
        // Enhanced window styling
        getRootPane().setBorder(BorderFactory.createLineBorder(BORDER_ACCENT, 1));

        loadRealUserData();
        initializeComponents();
    }

    private void loadRealUserData() {
        allHabits.addAll(Arrays.asList("Run", "Walking", "Study", "Playing", "Online Gaming", 
                                     "Programming", "Problem Solving", "Sleeping"));

        List<String[]> allUsers = UserFileHandler.getAllUsers();
        
        for (String[] user : allUsers) {
            String uid = user[0];
            calculateUserWeeklyAverages(uid);
        }
        
        calculateUserWeeklyAverages(currentUserId);
    }

    private void calculateUserWeeklyAverages(String userId) {
        Map<String, Double> userAverages = new HashMap<>();
        
        List<Map<String, Object>> weekData = UserFileHandler.readHabitsWithDate(userId, 7);
        
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
            
            double average = validDays > 0 ? (total / validDays) : 0.0;
            userAverages.put(habit, average);
        }
        
        userHabitAverages.put(userId, userAverages);
    }

    private void initializeComponents() {
        add(createInitialPanel(), BorderLayout.CENTER);
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInitialPanel() {
        JPanel initialPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Animated gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_PRIMARY,
                    getWidth(), getHeight(), BACKGROUND_SECONDARY
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle geometric pattern
                g2d.setColor(GLASS_OVERLAY);
                int size = 60;
                for (int x = 0; x < getWidth(); x += size) {
                    for (int y = 0; y < getHeight(); y += size) {
                        g2d.drawOval(x, y, size/3, size/3);
                        g2d.drawRect(x + size/2, y + size/2, size/4, size/4);
                    }
                }
                g2d.dispose();
            }
        };
        initialPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        
        JPanel selfCard = createModernOptionCard(
            "Compare with Yourself", 
            "Analyze your habit trends and personal growth over time",
            "📊", 
            ACCENT_ELECTRIC_BLUE,
            e -> showSelfComparisonDialog()
        );
        
        JPanel othersCard = createModernOptionCard(
            "Compare with Others",
            "Discover how you measure against the community and find inspiration", 
            "🚀",
            ACCENT_CYBER_PURPLE,
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

    private JPanel createModernOptionCard(String title, String description, String icon, Color accentColor, 
                                        java.awt.event.ActionListener action) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced card shadow with glow effect
                if (isHovered) {
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                    for (int i = 0; i < 15; i++) {
                        g2d.fill(new RoundRectangle2D.Float(i-7, i-5, getWidth() - (i-7) * 2, 
                                 getHeight() - (i-5) * 2, 24, 24));
                    }
                }
                
                // Card background with glass morphism
                GradientPaint cardGradient = new GradientPaint(
                    0, 0, BACKGROUND_SECONDARY,
                    getWidth(), getHeight(), BACKGROUND_ELEVATED
                );
                g2d.setPaint(cardGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Glass overlay
                g2d.setColor(GLASS_OVERLAY);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Accent border with glow
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 20, 20));
                
                // Accent stripe with gradient
                GradientPaint stripeGradient = new GradientPaint(
                    0, 0, accentColor,
                    getWidth(), 0, accentColor.brighter()
                );
                g2d.setPaint(stripeGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 8, 20, 20));
                
                g2d.dispose();
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(40, 35, 40, 35));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(400, 220));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 25, 0));

        JButton actionBtn = createGlowButton("Explore", accentColor);
        actionBtn.addActionListener(action);
        actionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(iconLabel);
        contentPanel.add(titleLabel);
        contentPanel.add(descLabel);
        contentPanel.add(actionBtn);

        card.add(contentPanel, BorderLayout.CENTER);

        // Enhanced hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getSource()).putClientProperty("isHovered", true);
                titleLabel.setForeground(accentColor);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel)e.getSource()).putClientProperty("isHovered", false);
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
        // Create custom styled dialog
        JDialog dialog = new JDialog(this, "Personal Habit Analysis", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BACKGROUND_PRIMARY);
        
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background with gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_SECONDARY,
                    getWidth(), getHeight(), BACKGROUND_ELEVATED
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.dispose();
            }
        };
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Analysis content
        StringBuilder analysis = new StringBuilder();
        analysis.append("Personal Habit Analysis - Last Week\n\n");
        
        Map<String, Double> myHabits = userHabitAverages.get(currentUserId);
        if (myHabits == null || myHabits.isEmpty()) {
            analysis.append("No habit data found for the last week.\n");
            analysis.append("Start tracking your habits to see your progress!");
        } else {
            double totalAvg = myHabits.values().stream()
                .filter(v -> v > 0)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
            analysis.append(String.format("Overall Performance Score: %.1f/5.0\n\n", Math.min(5.0, totalAvg)));
            
            myHabits.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    String habit = entry.getKey();
                    double avg = entry.getValue();
                    String unit = habit.equals("Problem Solving") ? "problems" : "hours";
                    String performance = avg >= 3.0 ? "Excellent" : avg >= 2.0 ? "Great" : avg >= 1.0 ? "Good" : "Needs Focus";
                    analysis.append(String.format("• %s: %.1f %s/day - %s\n", habit, avg, unit, performance));
                });
        }
        
        JTextArea textArea = new JTextArea(analysis.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textArea.setBackground(BACKGROUND_ELEVATED);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBackground(BACKGROUND_ELEVATED);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_ACCENT, 1));
        styleScrollPane(scrollPane);
        
        JButton closeBtn = createGlowButton("Close", ACCENT_ELECTRIC_BLUE);
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeBtn);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void refreshToUserList() {
        getContentPane().removeAll();
        add(createEnhancedHeaderPanel(), BorderLayout.NORTH);
        add(createUserListPanel(), BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private JPanel createEnhancedHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dynamic gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_ELECTRIC_BLUE,
                    getWidth(), getHeight(), ACCENT_CYBER_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Animated pattern overlay
                g2d.setColor(new Color(255, 255, 255, 15));
                int waveOffset = (int)(System.currentTimeMillis() / 50) % 100;
                for (int x = -100; x < getWidth() + 100; x += 50) {
                    int y = (int)(Math.sin((x + waveOffset) * 0.02) * 20) + getHeight()/2;
                    g2d.fillOval(x, y, 4, 4);
                }
                g2d.dispose();
            }
        };
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 140));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Community Comparison Hub");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel(comparisonMode ? 
            "Comparing " + currentUserName + " vs " + selectedUserName : 
            "Discover and connect with fellow habit builders");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(240, 245, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        headerPanel.add(titlePanel);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createHeaderPanel() {
        return createEnhancedHeaderPanel();
    }

    private JPanel createUserListPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(BACKGROUND_PRIMARY);

        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(BACKGROUND_PRIMARY);
        userListPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Enhanced info header
        JPanel headerInfo = new JPanel();
        headerInfo.setLayout(new BoxLayout(headerInfo, BoxLayout.Y_AXIS));
        headerInfo.setOpaque(false);
        headerInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        List<String[]> allUsers = UserFileHandler.getAllUsers();
        int otherUsersCount = (int) allUsers.stream()
            .filter(user -> !user[0].equals(currentUserId))
            .count();

        JLabel statsLabel = new JLabel("Active Community Members (" + otherUsersCount + " users)");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        statsLabel.setForeground(TEXT_PRIMARY);
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel("Select any member to compare your habit performance and discover insights");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 20, 0));

        headerInfo.add(statsLabel);
        headerInfo.add(infoLabel);

        userListPanel.add(headerInfo);
        userListPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Enhanced user grid
        JPanel userGrid = new JPanel(new GridLayout(0, 2, 25, 25));
        userGrid.setBackground(BACKGROUND_PRIMARY);

        Color[] cardColors = {
            ACCENT_ELECTRIC_BLUE, ACCENT_CYBER_PURPLE, ACCENT_NEON_GREEN, 
            ACCENT_CORAL, ACCENT_GOLD, ACCENT_PINK
        };
        int colorIndex = 0;

        for (String[] user : allUsers) {
            String uid = user[0];
            String uname = user[1];
            
            if (uid.equals(currentUserId)) {
                continue;
            }

            Color cardColor = cardColors[colorIndex % cardColors.length];
            
            Map<String, Double> userHabits = userHabitAverages.get(uid);
            double overallAvg = 0.0;
            if (userHabits != null && !userHabits.isEmpty()) {
                overallAvg = userHabits.values().stream()
                    .filter(v -> v > 0)
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            }
            
            String badge = overallAvg >= 3.0 ? "Elite Performer" : 
                          overallAvg >= 2.0 ? "Consistent Builder" :
                          overallAvg >= 1.0 ? "Rising Star" : "New Explorer";
            
            String rating = String.format("%.1f/5.0", Math.min(5.0, overallAvg));

            JPanel userCard = createEnhancedUserCard(uid, uname, badge, rating, cardColor);
            userGrid.add(userCard);
            colorIndex++;
        }

        userListPanel.add(userGrid);

        userScrollPane = new JScrollPane(userListPanel);
        userScrollPane.setBackground(BACKGROUND_PRIMARY);
        userScrollPane.setBorder(BorderFactory.createEmptyBorder());
        userScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollPane(userScrollPane);

        containerPanel.add(userScrollPane, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createEnhancedUserCard(String userId, String userName, String badge, String rating, Color accentColor) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glowing hover effect
                if (isHovered) {
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
                    for (int i = 0; i < 12; i++) {
                        g2d.fill(new RoundRectangle2D.Float(i-6, i-4, getWidth() - (i-6) * 2, 
                                 getHeight() - (i-4) * 2, 20, 20));
                    }
                }
                
                // Card background with depth
                GradientPaint cardGradient = new GradientPaint(
                    0, 0, BACKGROUND_SECONDARY,
                    getWidth(), getHeight(), BACKGROUND_ELEVATED
                );
                g2d.setPaint(cardGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                
                // Glass morphism overlay
                g2d.setColor(GLASS_OVERLAY);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                
                // Accent border
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 18, 18));
                
                // Top accent stripe
                GradientPaint stripeGradient = new GradientPaint(
                    0, 0, accentColor,
                    getWidth(), 0, accentColor.brighter()
                );
                g2d.setPaint(stripeGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 6, 18, 18));
                
                g2d.dispose();
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(350, 160));

        // User info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badgeLabel = new JLabel(badge);
        badgeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        badgeLabel.setForeground(TEXT_SECONDARY);
        badgeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        badgeLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 10, 0));

        JLabel ratingLabel = new JLabel("Performance Score: " + rating);
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ratingLabel.setForeground(accentColor);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(badgeLabel);
        infoPanel.add(ratingLabel);

        // Compare button
        JButton compareBtn = createGlowButton("Compare", accentColor);
        compareBtn.addActionListener(e -> {
            selectedUserId = userId;
            selectedUserName = userName;
            comparisonMode = true;
            showComparison();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(compareBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Enhanced hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getSource()).putClientProperty("isHovered", true);
                nameLabel.setForeground(accentColor.brighter());
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel)e.getSource()).putClientProperty("isHovered", false);
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
        add(createEnhancedHeaderPanel(), BorderLayout.NORTH);
        add(createComparisonPanel(), BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private JPanel createComparisonPanel() {
        comparisonPanel = new JPanel();
        comparisonPanel.setLayout(new BoxLayout(comparisonPanel, BoxLayout.Y_AXIS));
        comparisonPanel.setBackground(BACKGROUND_PRIMARY);
        comparisonPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JPanel summaryPanel = createEnhancedSummaryCards();
        comparisonPanel.add(summaryPanel);
        comparisonPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel chartsPanel = createEnhancedHabitCharts();
        comparisonPanel.add(chartsPanel);

        comparisonScrollPane = new JScrollPane(comparisonPanel);
        comparisonScrollPane.setBackground(BACKGROUND_PRIMARY);
        comparisonScrollPane.setBorder(BorderFactory.createEmptyBorder());
        comparisonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        comparisonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleScrollPane(comparisonScrollPane);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(BACKGROUND_PRIMARY);
        wrapperPanel.add(comparisonScrollPane, BorderLayout.CENTER);
        return wrapperPanel;
    }

    private JPanel createEnhancedSummaryCards() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        summaryPanel.setBackground(BACKGROUND_PRIMARY);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JPanel currentUserCard = createModernSummaryCard(
            currentUserName + " (You)",
            calculateOverallAverage(currentUserId),
            "Your Performance",
            ACCENT_ELECTRIC_BLUE,
            "🎯"
        );

        JPanel selectedUserCard = createModernSummaryCard(
            selectedUserName,
            calculateOverallAverage(selectedUserId),
            "Their Performance",
            ACCENT_CYBER_PURPLE,
            "👤"
        );

        summaryPanel.add(currentUserCard);
        summaryPanel.add(selectedUserCard);

        return summaryPanel;
    }

    private JPanel createModernSummaryCard(String title, double average, String subtitle, Color accentColor, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glowing shadow effect
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                for (int i = 0; i < 10; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i-5, i-3, getWidth() - (i-5) * 2, 
                             getHeight() - (i-3) * 2, 24, 24));
                }
                
                // Card background with glass morphism
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_SECONDARY,
                    getWidth(), getHeight(), BACKGROUND_ELEVATED
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                
                // Glass overlay
                g2d.setColor(GLASS_OVERLAY);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                
                // Accent border with glow
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(new RoundRectangle2D.Float(2, 2, getWidth() - 5, getHeight() - 5, 24, 24));
                
                // Inner glow
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(4, 4, getWidth() - 9, getHeight() - 9, 20, 20));
                
                g2d.dispose();
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(0, 180));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(accentColor);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JLabel averageLabel = new JLabel(String.format("%.1f avg/day", average));
        averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        averageLabel.setForeground(accentColor);
        averageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(subtitleLabel);
        contentPanel.add(averageLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createEnhancedHabitCharts() {
        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new BoxLayout(chartsPanel, BoxLayout.Y_AXIS));
        chartsPanel.setBackground(BACKGROUND_PRIMARY);

        JLabel titleLabel = new JLabel("Detailed Habit Comparison (Weekly Averages)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        chartsPanel.add(titleLabel);

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

            JPanel habitChart = createModernHabitChart(habit, currentUserValue, selectedUserValue);
            chartsPanel.add(habitChart);
            chartsPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        return chartsPanel;
    }

    private JPanel createModernHabitChart(String habitName, double currentUserValue, double selectedUserValue) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background with glass morphism
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_SECONDARY,
                    getWidth(), getHeight(), BACKGROUND_ELEVATED
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Glass overlay
                g2d.setColor(GLASS_OVERLAY);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Subtle border
                g2d.setColor(BORDER_SUBTLE);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                
                g2d.dispose();
            }
        };
        
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        chartPanel.setPreferredSize(new Dimension(0, 180));
        chartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Left info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setPreferredSize(new Dimension(220, 0));

        JLabel habitLabel = new JLabel(getHabitIcon(habitName) + " " + habitName);
        habitLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        habitLabel.setForeground(TEXT_PRIMARY);

        String unit = habitName.equals("Problem Solving") ? " problems" : " hours";
        JLabel comparisonLabel = new JLabel(String.format("You: %.1f%s | Them: %.1f%s", 
                                                          currentUserValue, unit, 
                                                          selectedUserValue, unit));
        comparisonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comparisonLabel.setForeground(TEXT_SECONDARY);
        comparisonLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 15, 0));

        double difference = currentUserValue - selectedUserValue;
        String diffText = String.format("%+.1f%s", difference, unit);
        JLabel diffLabel = new JLabel(diffText);
        diffLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        diffLabel.setForeground(difference >= 0 ? ACCENT_NEON_GREEN : ACCENT_CORAL);

        infoPanel.add(habitLabel);
        infoPanel.add(comparisonLabel);
        infoPanel.add(diffLabel);

        // Right chart panel
        JPanel chartAreaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth() - 40;
                int height = 100;
                int barHeight = 35;
                int x = 20;
                int y1 = 15;
                int y2 = y1 + barHeight + 20;

                double maxValue = Math.max(currentUserValue, selectedUserValue);
                if (maxValue == 0) maxValue = 1;
                
                double scaleMax = habitName.equals("Problem Solving") ? 10.0 : 8.0;
                maxValue = Math.max(maxValue, scaleMax);

                drawEnhancedProgressBar(g2d, x, y1, width, barHeight, currentUserValue, maxValue, 
                                      ACCENT_ELECTRIC_BLUE, currentUserName + " (You)");

                drawEnhancedProgressBar(g2d, x, y2, width, barHeight, selectedUserValue, maxValue, 
                                      ACCENT_CYBER_PURPLE, selectedUserName);
            }

            private void drawEnhancedProgressBar(Graphics2D g2d, int x, int y, int width, int height, 
                                               double value, double maxValue, Color color, String label) {
                // Background with gradient
                GradientPaint bgGradient = new GradientPaint(
                    x, y, new Color(30, 41, 59),
                    x + width, y + height, new Color(15, 23, 42)
                );
                g2d.setPaint(bgGradient);
                g2d.fill(new RoundRectangle2D.Float(x, y, width, height, height/2, height/2));
                
                // Progress fill with glow
                int fillWidth = (int) (width * (value / maxValue));
                if (fillWidth > 0) {
                    // Glow effect
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                    g2d.fill(new RoundRectangle2D.Float(x-2, y-2, fillWidth+4, height+4, height/2, height/2));
                    
                    // Main fill with gradient
                    GradientPaint fillGradient = new GradientPaint(
                        x, y, color.brighter(),
                        x + fillWidth, y + height, color
                    );
                    g2d.setPaint(fillGradient);
                    g2d.fill(new RoundRectangle2D.Float(x, y, fillWidth, height, height/2, height/2));
                    
                    // Inner highlight
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fill(new RoundRectangle2D.Float(x, y, fillWidth, height/3, height/2, height/2));
                }
                
                // Value text with shadow
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2d.getFontMetrics();
                String valueText = String.format("%.1f", value);
                int textWidth = fm.stringWidth(valueText);
                
                int textX = x + fillWidth + 12;
                int textY = y + (height + fm.getAscent()) / 2 - 2;
                
                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(valueText, textX + 1, textY + 1);
                
                // Main text
                g2d.setColor(TEXT_PRIMARY);
                g2d.drawString(valueText, textX, textY);
                
                // Label with shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2d.drawString(label, x + 1, y - 4);
                
                g2d.setColor(TEXT_SECONDARY);
                g2d.drawString(label, x, y - 5);
            }
        };

        chartAreaPanel.setOpaque(false);
        chartAreaPanel.setPreferredSize(new Dimension(0, 120));

        chartPanel.add(infoPanel, BorderLayout.WEST);
        chartPanel.add(chartAreaPanel, BorderLayout.CENTER);

        return chartPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_SECONDARY,
                    getWidth(), getHeight(), BACKGROUND_PRIMARY
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 25));
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        if (comparisonMode) {
            JButton backToListBtn = createGlowButton("← Back to Community", ACCENT_GOLD);
            backToListBtn.addActionListener(e -> {
                comparisonMode = false;
                selectedUserId = null;
                selectedUserName = null;
                refreshToUserList();
            });
            navPanel.add(backToListBtn);
        }

        JButton backToDashboardBtn = createGlowButton("← Dashboard", ACCENT_CORAL);
        backToDashboardBtn.addActionListener(e -> {
            new DashboardFrame(currentUserId, currentUserName).setVisible(true);
            dispose();
        });
        navPanel.add(backToDashboardBtn);

        return navPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_ELEVATED,
                    getWidth(), getHeight(), BACKGROUND_SECONDARY
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_SUBTLE),
            BorderFactory.createEmptyBorder(20, 0, 20, 0)
        ));
        
        JLabel footerLabel = new JLabel("Compare, Learn, and Grow Together with the Community");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
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

    private JButton createGlowButton(String text, Color bgColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color currentColor = bgColor;
                if (getModel().isPressed()) {
                    currentColor = bgColor.darker();
                } else if (getModel().isRollover()) {
                    currentColor = bgColor.brighter();
                    
                    // Glow effect on hover
                    g2d.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 40));
                    for (int i = 0; i < 8; i++) {
                        g2d.fill(new RoundRectangle2D.Float(i-4, i-2, getWidth() - (i-4) * 2, 
                                 getHeight() - (i-2) * 2, 12, 12));
                    }
                }
                
                // Button gradient
                GradientPaint buttonGradient = new GradientPaint(
                    0, 0, currentColor.brighter(),
                    0, getHeight(), currentColor
                );
                g2d.setPaint(buttonGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Inner highlight
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fill(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()/3, 12, 12));
                
                // Text with shadow
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                
                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(text, x + 1, y + 1);
                
                // Main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));
        
        return button;
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        scrollPane.getVerticalScrollBar().setBackground(BACKGROUND_PRIMARY);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(ACCENT_ELECTRIC_BLUE.getRed(), ACCENT_ELECTRIC_BLUE.getGreen(), ACCENT_ELECTRIC_BLUE.getBlue(), 120);
                this.trackColor = BACKGROUND_ELEVATED;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(thumbColor);
                g2d.fill(new RoundRectangle2D.Float(thumbBounds.x + 2, thumbBounds.y, 
                         thumbBounds.width - 4, thumbBounds.height, 6, 6));
                g2d.dispose();
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