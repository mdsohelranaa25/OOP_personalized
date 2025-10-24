import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.HashMap;

public class HabitViewer extends JFrame {
    private String userId;
    
    // Dark Theme Color Palette (matching login frame)
    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);       // Indigo-600
    private static final Color PRIMARY_HOVER = new Color(67, 56, 202);       // Indigo-700
    private static final Color SECONDARY_COLOR = new Color(139, 92, 246);    // Violet-500
    private static final Color BACKGROUND_COLOR = new Color(15, 23, 42);     // Slate-900 (dark background)
    private static final Color CARD_COLOR = new Color(30, 41, 59);           // Slate-800 (dark card)
    private static final Color INPUT_COLOR = new Color(51, 65, 85);          // Slate-700 (input background)
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);      // Slate-50 (white text)
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);    // Slate-400 (gray text)
    private static final Color BORDER_COLOR = new Color(71, 85, 105);        // Slate-600 (borders)
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);       // Green-500
    private static final Color WARNING_COLOR = new Color(245, 158, 11);      // Amber-500
    private static final Color ERROR_COLOR = new Color(239, 68, 68);         // Red-500
    private static final Color ACCENT_COLOR = new Color(14, 165, 233);       // Sky-500
    
    // Additional colors for charts
    private static final Color ACCENT_EMERALD = new Color(34, 197, 94);      // Emerald-500
    private static final Color ACCENT_PURPLE = new Color(139, 92, 246);      // Purple-500
    private static final Color ACCENT_ORANGE = new Color(251, 146, 60);      // Orange-400
    private static final Color ACCENT_TEAL = new Color(20, 184, 166);        // Teal-500
    private static final Color ACCENT_PINK = new Color(236, 72, 153);        // Pink-500
    private static final Color ACCENT_AMBER = new Color(245, 158, 11);       // Amber-500
    
    private static final Color SURFACE_COLOR = new Color(41, 50, 65);        // Slightly lighter than card
    private static final Color HOVER_COLOR = new Color(55, 65, 81);          // Slate-700
    
    // Data for average calculation
    private Map<String, Double> habitAverages = new HashMap<>();
    private Map<String, Integer> habitValidDays = new HashMap<>();

    public HabitViewer(String title, String userId, String viewType) {
        this.userId = userId;

        setTitle(title + " - " + UserFileHandler.getUserName(userId));
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        if(viewType.equalsIgnoreCase("today")) showToday();
        else showLast7Days();
    }

    private void showToday() {
        Map<String,Integer> todayMap = UserFileHandler.loadHabit(userId, LocalDate.now().toString());
        Map<String, Map<String,Integer>> allData = UserFileHandler.loadHabitsWithDate(userId);

        Set<String> habitNames = new LinkedHashSet<>();
        for(Map<String,Integer> m : allData.values()) habitNames.addAll(m.keySet());

        String[] columns = habitNames.toArray(new String[0]);
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Object[] row = new Object[columns.length];
        int i=0;
        for(String h : habitNames) row[i++] = todayMap.getOrDefault(h, 0);
        model.addRow(row);

        JTable table = createModernTable(model);
        JScrollPane scroll = createModernScrollPane(table, 120);

        JPanel graphPanel = createGraphPanel(model, "Today's Activity Overview", false);

        JPanel mainPanel = createScrollableMainPanel();
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel("Today's Progress", "Track your daily achievements", 
            LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")));
        
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createCardPanel(scroll, "Data Summary"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createCardPanel(graphPanel, "Visual Analysis"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane mainScrollPane = createMainScrollPane(mainPanel);
        add(mainScrollPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void showLast7Days() {
        // Calculate 7-day averages first
        calculateSevenDayAverages();
        
        List<String> last7Dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int d=6; d>=0; d--) last7Dates.add(today.minusDays(d).toString());

        Map<String, Map<String,Integer>> allData = UserFileHandler.loadHabitsWithDate(userId);

        Set<String> habitNames = new LinkedHashSet<>();
        for(Map<String,Integer> m : allData.values()) habitNames.addAll(m.keySet());

        String[] columns = new String[habitNames.size()+1];
        columns[0] = "Date";
        int c=1;
        for(String h : habitNames) columns[c++] = h;

        DefaultTableModel model = new DefaultTableModel(columns,0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        for(String date : last7Dates){
            Object[] row = new Object[columns.length];
            try {
                LocalDate ld = LocalDate.parse(date);
                row[0] = ld.format(formatter);
            } catch (Exception e) {
                row[0] = date;
            }
            
            for(int i=1;i<columns.length;i++) row[i]=0;
            if(allData.containsKey(date)){
                Map<String,Integer> hm = allData.get(date);
                for(int i=1;i<columns.length;i++){
                    if(hm.containsKey(columns[i])) row[i] = hm.get(columns[i]);
                }
            }
            model.addRow(row);
        }

        // Add average row
        Object[] avgRow = new Object[columns.length];
        avgRow[0] = "7-Day Avg";
        for(int i=1; i<columns.length; i++) {
            String habitName = columns[i];
            double avg = habitAverages.getOrDefault(habitName, 0.0);
            avgRow[i] = String.format("%.1f", avg);
        }
        model.addRow(avgRow);

        JTable table = createModernTable(model);
        JScrollPane scroll = createModernScrollPane(table, 220);

        JPanel graphPanel = createGraphPanel(model, "7-Day Average Performance", true);
        JPanel statsPanel = createStatsPanel();

        JPanel mainPanel = createScrollableMainPanel();
        
        // Enhanced Header Panel
        JPanel headerPanel = createHeaderPanel("Weekly Analytics", "Your 7-day habit performance with smart averages", 
            "Last 7 Days Analysis");
        
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(statsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createCardPanel(scroll, "Weekly Data Table"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createCardPanel(graphPanel, "Average Performance Chart"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane mainScrollPane = createMainScrollPane(mainPanel);
        add(mainScrollPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void calculateSevenDayAverages() {
        habitAverages.clear();
        habitValidDays.clear();
        
        List<String> last7Dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int d=6; d>=0; d--) last7Dates.add(today.minusDays(d).toString());

        Map<String, Map<String,Integer>> allData = UserFileHandler.loadHabitsWithDate(userId);
        
        // Get all unique habit names
        Set<String> allHabits = new LinkedHashSet<>();
        for(Map<String,Integer> dayData : allData.values()) {
            allHabits.addAll(dayData.keySet());
        }
        
        // Calculate averages for each habit
        for(String habit : allHabits) {
            double total = 0.0;
            int validDays = 0;
            
            for(String date : last7Dates) {
                if(allData.containsKey(date)) {
                    Map<String,Integer> dayHabits = allData.get(date);
                    
                    // Check if this day has ANY habit data
                    boolean hasDayData = false;
                    for(Integer value : dayHabits.values()) {
                        if(value != null && value > 0) {
                            hasDayData = true;
                            break;
                        }
                    }
                    
                    // Only count days that have some habit data
                    if(hasDayData && dayHabits.containsKey(habit)) {
                        Integer value = dayHabits.get(habit);
                        if(value != null) {
                            total += value;
                            validDays++;
                        }
                    }
                }
            }
            
            double average = validDays > 0 ? (total / validDays) : 0.0;
            habitAverages.put(habit, average);
            habitValidDays.put(habit, validDays);
        }
    }

    private JPanel createStatsPanel() {
        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.setBackground(BACKGROUND_COLOR);
        statsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Overall average
        double overallAvg = habitAverages.values().stream()
            .filter(v -> v > 0)
            .mapToDouble(Double::doubleValue)
            .average().orElse(0.0);

        // Active habits count
        long activeHabits = habitAverages.values().stream()
            .filter(v -> v > 0)
            .count();

        // Most tracked days
        int maxDays = habitValidDays.values().stream()
            .mapToInt(Integer::intValue)
            .max().orElse(0);

        JPanel overallCard = createStatCard("Overall Average", String.format("%.1f", overallAvg), 
            "units/day", PRIMARY_COLOR, "🎯");
        JPanel activeCard = createStatCard("Active Habits", String.valueOf(activeHabits), 
            "out of " + habitAverages.size(), SUCCESS_COLOR, "📈");
        JPanel consistencyCard = createStatCard("Max Consistency", String.valueOf(maxDays), 
            "days tracked", SECONDARY_COLOR, "🔥");

        statsContainer.add(overallCard);
        statsContainer.add(activeCard);
        statsContainer.add(consistencyCard);

        return statsContainer;
    }

    private JPanel createStatCard(String title, String value, String subtitle, Color accentColor, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                for (int i = 0; i < 8; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i + 1, getWidth() - (i * 2), 
                             getHeight() - (i * 2) - 1, 16, 16));
                }
                
                // Card background with subtle gradient
                GradientPaint gradient = new GradientPaint(0, 0, CARD_COLOR, 0, getHeight(), CARD_COLOR.darker());
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                // Accent border
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 16, 16));
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(0, 120));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(accentColor);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(valueLabel);
        contentPanel.add(subtitleLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JScrollPane createMainScrollPane(JPanel mainPanel) {
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
        
        // Enhanced scrollbar styling
        scrollPane.getVerticalScrollBar().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_COLOR;
                this.trackColor = BACKGROUND_COLOR;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(thumbColor);
                g2d.fill(new RoundRectangle2D.Float(thumbBounds.x + 2, thumbBounds.y + 2, 
                         thumbBounds.width - 4, thumbBounds.height - 4, 8, 8));
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
        
        return scrollPane;
    }

    private JPanel createHeaderPanel(String title, String subtitle, String date) {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), SECONDARY_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Subtle pattern
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int x = 0; x < getWidth(); x += 50) {
                    for (int y = 0; y < getHeight(); y += 50) {
                        g2d.fillOval(x, y, 3, 3);
                        g2d.fillOval(x + 25, y + 25, 3, 3);
                    }
                }
            }
        };
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        headerPanel.setPreferredSize(new Dimension(0, 140));

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(220, 230, 255));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Date section
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setForeground(new Color(200, 220, 255));
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createScrollableMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        return mainPanel;
    }

    private JPanel createCardPanel(Component content, String cardTitle) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 25));
                for (int i = 0; i < 8; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i + 1, getWidth() - (i * 2), 
                             getHeight() - (i * 2) - 1, 18, 18));
                }
                
                // Card background with gradient
                GradientPaint gradient = new GradientPaint(0, 0, CARD_COLOR, 0, getHeight(), CARD_COLOR.darker());
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                
                // Subtle border
                g2d.setColor(BORDER_COLOR);
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
            }
        };
        
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardPanel.getPreferredSize().height));

        // Card header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 25));
        
        JLabel cardTitleLabel = new JLabel(cardTitle);
        cardTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        cardTitleLabel.setForeground(TEXT_PRIMARY);
        
        // Decorative line
        JPanel decorLine = new JPanel();
        decorLine.setBackground(PRIMARY_COLOR);
        decorLine.setPreferredSize(new Dimension(50, 3));
        decorLine.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.add(cardTitleLabel, BorderLayout.NORTH);
        titleContainer.add(decorLine, BorderLayout.SOUTH);
        
        headerPanel.add(titleContainer, BorderLayout.WEST);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));
        contentPanel.add(content, BorderLayout.CENTER);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(contentPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    private JTable createModernTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Highlight average row
                if (row == getRowCount() - 1 && model.getValueAt(row, 0).toString().contains("Avg")) {
                    c.setBackground(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 30));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setForeground(TEXT_PRIMARY);
                } else if (row % 2 == 0) {
                    c.setBackground(CARD_COLOR);
                    c.setForeground(TEXT_PRIMARY);
                } else {
                    c.setBackground(SURFACE_COLOR);
                    c.setForeground(TEXT_PRIMARY);
                }
                
                if (isRowSelected(row)) {
                    c.setBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
                    c.setForeground(TEXT_PRIMARY);
                }
                
                return c;
            }
        };
        
        // Table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.setBackground(CARD_COLOR);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 40));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));

        // Cell renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return table;
    }

    private JScrollPane createModernScrollPane(JTable table, int preferredHeight) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(900, preferredHeight));
        scroll.setBorder(new RoundedBorder(12, BORDER_COLOR));
        scroll.setBackground(CARD_COLOR);
        scroll.getViewport().setBackground(CARD_COLOR);
        
        // Custom scrollbar styling
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_COLOR;
                this.trackColor = CARD_COLOR;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(thumbColor);
                g2d.fill(new RoundRectangle2D.Float(thumbBounds.x + 2, thumbBounds.y + 2, 
                         thumbBounds.width - 4, thumbBounds.height - 4, 8, 8));
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
        
        return scroll;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonPanel.getPreferredSize().height));
        
        JButton closeBtn = createModernButton("Close", TEXT_SECONDARY);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(closeBtn);
        return buttonPanel;
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
                    currentColor = bgColor.brighter();
                }
                
                // Button shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fill(new RoundRectangle2D.Float(1, 2, getWidth() - 1, getHeight() - 2, 12, 12));
                
                // Button background with gradient
                GradientPaint gradient = new GradientPaint(0, 0, currentColor, 0, getHeight(), currentColor.darker());
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Button text
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
        button.setPreferredSize(new Dimension(140, 42));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JPanel createGraphPanel(DefaultTableModel model, String title, boolean showAverages) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth() - 60;
                int height = 280;
                int x0 = 30;
                int y0 = 20;

                // Background with dark theme
                g2d.setColor(SURFACE_COLOR);
                g2d.fillRoundRect(x0, y0, width, height, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.drawRoundRect(x0, y0, width, height, 16, 16);

                // Chart area
                int chartWidth = width - 80;
                int chartHeight = height - 100;
                int chartX = x0 + 40;
                int chartY = y0 + 60;

                if (model.getRowCount() == 0 || model.getColumnCount() <= 1) {
                    g2d.setColor(TEXT_SECONDARY);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    String noData = "No data available to display";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = chartX + (chartWidth - fm.stringWidth(noData)) / 2;
                    int textY = chartY + chartHeight / 2;
                    g2d.drawString(noData, textX, textY);
                    return;
                }

                // Professional color palette for bars
                Color[] colors = {
                    PRIMARY_COLOR,       // Blue
                    ACCENT_EMERALD,     // Emerald  
                    SECONDARY_COLOR,    // Purple
                    ACCENT_ORANGE,      // Orange
                    ACCENT_PINK,        // Pink
                    ACCENT_TEAL,        // Teal
                    ACCENT_AMBER,       // Amber
                    ACCENT_COLOR        // Sky blue
                };

                int startCol = model.getColumnName(0).contains("Date") ? 1 : 0;
                int totalBars = model.getColumnCount() - startCol;

                if (totalBars == 0) return;

                int barWidth = Math.max(30, (chartWidth - (totalBars - 1) * 15) / totalBars);
                int gap = 15;
                double maxValue = 1.0;

                if (showAverages) {
                    // For 7-day averages, use the calculated averages
                    maxValue = habitAverages.values().stream()
                        .mapToDouble(Double::doubleValue)
                        .max().orElse(1.0);
                } else {
                    // For today's data, find max from table
                    for (int col = startCol; col < model.getColumnCount(); col++) {
                        for (int row = 0; row < model.getRowCount(); row++) {
                            Object value = model.getValueAt(row, col);
                            if (value instanceof Number) {
                                maxValue = Math.max(maxValue, ((Number) value).doubleValue());
                            }
                        }
                    }
                }

                if (maxValue == 0) maxValue = 1.0;

                // Draw bars
                int barIndex = 0;
                for (int col = startCol; col < model.getColumnCount(); col++) {
                    String habitName = model.getColumnName(col);
                    Color barColor = colors[barIndex % colors.length];
                    
                    double value;
                    if (showAverages) {
                        // Use calculated average
                        value = habitAverages.getOrDefault(habitName, 0.0);
                    } else {
                        // Use today's value
                        Object cellValue = model.getValueAt(0, col);
                        value = (cellValue instanceof Number) ? ((Number) cellValue).doubleValue() : 0.0;
                    }
                    
                    int barHeight = (int) ((value / maxValue) * (chartHeight - 40));
                    int barX = chartX + barIndex * (barWidth + gap);
                    int barY = chartY + chartHeight - 40 - barHeight;

                    // Draw shadow
                    g2d.setColor(new Color(0, 0, 0, 40));
                    g2d.fillRoundRect(barX + 3, barY + 3, barWidth, barHeight, 8, 8);

                    // Draw bar with gradient
                    GradientPaint gradient = new GradientPaint(
                        barX, barY, barColor,
                        barX, barY + barHeight, new Color(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), 180)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(barX, barY, barWidth, barHeight, 8, 8);

                    // Draw bar border
                    g2d.setColor(barColor.darker());
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(barX, barY, barWidth, barHeight, 8, 8);

                    // Draw value on top of bar
                    if (value > 0) {
                        g2d.setColor(TEXT_PRIMARY);
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        FontMetrics fm = g2d.getFontMetrics();
                        String valueStr = showAverages ? String.format("%.1f", value) : String.valueOf((int)value);
                        int textX = barX + (barWidth - fm.stringWidth(valueStr)) / 2;
                        g2d.drawString(valueStr, textX, barY - 8);
                        
                        // Show valid days count for averages
                        if (showAverages) {
                            int validDays = habitValidDays.getOrDefault(habitName, 0);
                            if (validDays > 0 && validDays < 7) {
                                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                                g2d.setColor(TEXT_SECONDARY);
                                String daysStr = "(" + validDays + "d)";
                                int daysX = barX + (barWidth - g2d.getFontMetrics().stringWidth(daysStr)) / 2;
                                g2d.drawString(daysStr, daysX, barY - 22);
                            }
                        }
                    }
                    barIndex++;
                }

                // Draw habit labels
                g2d.setColor(TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                barIndex = 0;
                for (int col = startCol; col < model.getColumnCount(); col++) {
                    String label = model.getColumnName(col);
                    if (label.length() > 10) {
                        label = label.substring(0, 10) + "...";
                    }
                    
                    FontMetrics fm = g2d.getFontMetrics();
                    int labelX = chartX + barIndex * (barWidth + gap) + (barWidth - fm.stringWidth(label)) / 2;
                    int labelY = chartY + chartHeight - 15;
                    g2d.drawString(label, labelX, labelY);
                    barIndex++;
                }

                // Draw title
                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2d.drawString(title, x0 + 15, y0 + 35);
                
                // Draw subtitle for averages
                if (showAverages) {
                    g2d.setColor(TEXT_SECONDARY);
                    g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                    g2d.drawString("Calculated from days with habit data only", x0 + 15, y0 + 55);
                }

                // Draw Y-axis labels
                g2d.setColor(TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                for (int i = 0; i <= 4; i++) {
                    double yValue = (maxValue / 4) * i;
                    int yPos = chartY + chartHeight - 40 - (int)((yValue / maxValue) * (chartHeight - 40));
                    g2d.drawString(String.format("%.1f", yValue), x0 + 5, yPos + 4);
                    
                    // Grid lines
                    g2d.setColor(new Color(BORDER_COLOR.getRed(), BORDER_COLOR.getGreen(), BORDER_COLOR.getBlue(), 60));
                    g2d.drawLine(chartX, yPos, chartX + chartWidth, yPos);
                    g2d.setColor(TEXT_SECONDARY);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(900, 320);
            }
        };
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
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }
}