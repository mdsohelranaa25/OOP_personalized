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

public class HabitViewer extends JFrame {
    private String userId;

    // Modern Green Color Palette
    private static final Color PRIMARY_GREEN = new Color(34, 197, 94);     // Emerald-500
    private static final Color SECONDARY_GREEN = new Color(16, 185, 129);  // Emerald-600  
    private static final Color ACCENT_TEAL = new Color(20, 184, 166);      // Teal-500
    private static final Color LIGHT_GREEN = new Color(187, 247, 208);     // Green-200
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);     // Green-500
    private static final Color MINT_50 = new Color(240, 253, 244);         // Green-50
    private static final Color MINT_100 = new Color(220, 252, 231);        // Green-100
    private static final Color SAGE_200 = new Color(187, 247, 208);        // Green-200
    private static final Color FOREST_600 = new Color(22, 163, 74);        // Green-600
    private static final Color FOREST_700 = new Color(21, 128, 61);        // Green-700
    private static final Color CHARCOAL = new Color(31, 41, 55);           // Gray-800
    private static final Color SLATE_600 = new Color(71, 85, 105);         // Slate-600
    private static final Color WHITE = Color.WHITE;

    public HabitViewer(String title, String userId, String viewType) {
        this.userId = userId;

        setTitle(title + " - " + UserFileHandler.getUserName(userId));
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true); // Enable resizing for better scroll experience
        getContentPane().setBackground(MINT_50);
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

        JPanel graphPanel = createGraphPanel(model, "📊 Today's Habits Overview");

        JPanel mainPanel = createScrollableMainPanel();
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel("🌱 Today's Activity", "Track your daily progress", LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createCardPanel(scroll, "📈 Data Table"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createCardPanel(graphPanel, "📊 Visual Chart"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Extra space at bottom

        // Wrap in scroll pane
        JScrollPane mainScrollPane = createMainScrollPane(mainPanel);
        add(mainScrollPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void showLast7Days() {
        List<String> last7Dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int d=6; d>=0; d--) last7Dates.add(today.minusDays(d).toString());

        Map<String, Map<String,Integer>> allData = UserFileHandler.loadHabitsWithDate(userId);

        Set<String> habitNames = new LinkedHashSet<>();
        for(Map<String,Integer> m : allData.values()) habitNames.addAll(m.keySet());

        String[] columns = new String[habitNames.size()+1];
        columns[0] = "📅 Date";
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

        JTable table = createModernTable(model);
        JScrollPane scroll = createModernScrollPane(table, 180);

        JPanel graphPanel = createGraphPanel(model, "📊 Weekly Progress Chart");

        JPanel mainPanel = createScrollableMainPanel();
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel("📈 Weekly Overview", "Your 7-day habit tracking summary", "Last 7 Days");
        
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createCardPanel(scroll, "📊 Weekly Data"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createCardPanel(graphPanel, "📈 Trend Analysis"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Extra space at bottom

        // Wrap in scroll pane
        JScrollPane mainScrollPane = createMainScrollPane(mainPanel);
        add(mainScrollPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private JScrollPane createMainScrollPane(JPanel mainPanel) {
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
        
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
        
        return scrollPane;
    }

    private JPanel createHeaderPanel(String title, String subtitle, String date) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, SAGE_200),
            BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(CHARCOAL);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(FOREST_600);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Date section
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateLabel.setForeground(PRIMARY_GREEN);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createScrollableMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(MINT_50);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        return mainPanel;
    }

    private JPanel createCardPanel(Component content, String cardTitle) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(WHITE);
        cardPanel.setBorder(new RoundedBorder(16, SAGE_200));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardPanel.getPreferredSize().height));

        // Card header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 0, 20));
        
        JLabel cardTitleLabel = new JLabel(cardTitle);
        cardTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardTitleLabel.setForeground(CHARCOAL);
        headerPanel.add(cardTitleLabel);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        contentPanel.add(content, BorderLayout.CENTER);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(contentPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    private JTable createModernTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        
        // Table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(36);
        table.setGridColor(SAGE_200);
        table.setSelectionBackground(LIGHT_GREEN);
        table.setSelectionForeground(CHARCOAL);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_GREEN);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Cell renderer for better appearance
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(WHITE);
        centerRenderer.setForeground(CHARCOAL);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return table;
    }

    private JScrollPane createModernScrollPane(JTable table, int preferredHeight) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, preferredHeight));
        scroll.setBorder(new RoundedBorder(12, SAGE_200));
        scroll.setBackground(WHITE);
        scroll.getViewport().setBackground(WHITE);
        
        // Custom scrollbar styling
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_GREEN;
                this.trackColor = MINT_100;
            }
        });
        
        return scroll;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(MINT_50);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonPanel.getPreferredSize().height));
        
        JButton closeBtn = createModernButton("✖ Close", SLATE_600);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(closeBtn);
        return buttonPanel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(120, 42));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new RoundedBorder(10, bgColor));

        // Hover effects
        Color originalBg = bgColor;
        Color hoverBg = bgColor.brighter();

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

    private JPanel createGraphPanel(DefaultTableModel model, String title) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth() - 60;
                int height = 220;
                int x0 = 30;
                int y0 = 40;

                // Background
                g2d.setColor(MINT_50);
                g2d.fillRoundRect(x0, y0, width, height, 12, 12);
                g2d.setColor(SAGE_200);
                g2d.drawRoundRect(x0, y0, width, height, 12, 12);

                // Chart area
                int chartWidth = width - 60;
                int chartHeight = height - 80;
                int chartX = x0 + 30;
                int chartY = y0 + 40;

                if (model.getRowCount() == 0 || model.getColumnCount() <= 1) {
                    g2d.setColor(SLATE_600);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    String noData = "No data to display";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = chartX + (chartWidth - fm.stringWidth(noData)) / 2;
                    int textY = chartY + chartHeight / 2;
                    g2d.drawString(noData, textX, textY);
                    return;
                }

                // Modern color palette for bars
                Color[] modernColors = {
                    PRIMARY_GREEN,      // Emerald
                    ACCENT_TEAL,        // Teal
                    new Color(139, 92, 246),   // Purple
                    new Color(249, 115, 22),   // Orange
                    new Color(236, 72, 153),   // Pink
                    new Color(59, 130, 246),   // Blue
                    new Color(245, 158, 11),   // Amber
                    new Color(239, 68, 68)     // Red
                };

                int totalBars = 0;
                int startCol = model.getColumnName(0).contains("Date") ? 1 : 0;
                
                for (int col = startCol; col < model.getColumnCount(); col++) {
                    totalBars++;
                }

                if (totalBars == 0) return;

                int barWidth = Math.max(25, (chartWidth - (totalBars - 1) * 10) / totalBars);
                int gap = 10;
                int maxValue = 0;

                // Find max value for scaling
                for (int col = startCol; col < model.getColumnCount(); col++) {
                    for (int row = 0; row < model.getRowCount(); row++) {
                        Object value = model.getValueAt(row, col);
                        if (value instanceof Number) {
                            maxValue = Math.max(maxValue, ((Number) value).intValue());
                        }
                    }
                }

                if (maxValue == 0) maxValue = 1;

                // Draw bars
                int barIndex = 0;
                for (int col = startCol; col < model.getColumnCount(); col++) {
                    Color barColor = modernColors[barIndex % modernColors.length];
                    
                    for (int row = 0; row < model.getRowCount(); row++) {
                        Object value = model.getValueAt(row, col);
                        int val = (value instanceof Number) ? ((Number) value).intValue() : 0;
                        
                        int barHeight = (int) ((double) val / maxValue * (chartHeight - 20));
                        int barX = chartX + barIndex * (barWidth + gap) + row * 2;
                        int barY = chartY + chartHeight - 20 - barHeight;

                        // Draw shadow
                        g2d.setColor(new Color(0, 0, 0, 30));
                        g2d.fillRoundRect(barX + 2, barY + 2, barWidth, barHeight, 6, 6);

                        // Draw bar
                        g2d.setColor(barColor);
                        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 6, 6);

                        // Draw value on top of bar
                        if (val > 0) {
                            g2d.setColor(CHARCOAL);
                            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                            FontMetrics fm = g2d.getFontMetrics();
                            String valStr = String.valueOf(val);
                            int textX = barX + (barWidth - fm.stringWidth(valStr)) / 2;
                            g2d.drawString(valStr, textX, barY - 5);
                        }
                    }
                    barIndex++;
                }

                // Draw labels
                g2d.setColor(FOREST_700);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                barIndex = 0;
                for (int col = startCol; col < model.getColumnCount(); col++) {
                    String label = model.getColumnName(col);
                    if (label.length() > 8) {
                        label = label.substring(0, 8) + "...";
                    }
                    
                    FontMetrics fm = g2d.getFontMetrics();
                    int labelX = chartX + barIndex * (barWidth + gap) + (barWidth - fm.stringWidth(label)) / 2;
                    int labelY = chartY + chartHeight + 15;
                    g2d.drawString(label, labelX, labelY);
                    barIndex++;
                }

                // Title
                g2d.setColor(CHARCOAL);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2d.drawString(title, x0 + 10, y0 + 25);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 280);
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
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }
}