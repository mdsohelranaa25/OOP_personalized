import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterFrame extends JFrame {
    private JTextField nameField, idField, hallField, deptField, batchField, mobileField;
    private JPasswordField passField;
    private JButton registerBtn, backBtn;
    
   
    private static final Color PRIMARY_DARK = new Color(17, 24, 39);
    private static final Color PRIMARY_LIGHT = new Color(31, 41, 55);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color INPUT_BACKGROUND = new Color(249, 250, 251);
    private static final Color SUCCESS_COLOR = new Color(16, 185, 129);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);

    public RegisterFrame() {
        setTitle("Personalized Habit Tracker- Create Account");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(550, 600));
        
       
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
        
        // Add subtle window border
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));

        initializeComponents();
    }

    private void initializeComponents() {
        // Header Panel with Logo and Branding
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Main Form Panel
        add(createMainFormPanel(), BorderLayout.CENTER);
        
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
                
                // Subtle pattern overlay
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < getWidth(); i += 20) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
            }
        };
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 140));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Logo and App Name
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🎯");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JLabel appNameLabel = new JLabel("HabitTracker");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        JLabel proLabel = new JLabel("PRO");
        proLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        proLabel.setForeground(ACCENT_BLUE);
        proLabel.setOpaque(true);
        proLabel.setBackground(Color.WHITE);
        proLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        proLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        
        logoPanel.add(logoLabel);
        logoPanel.add(appNameLabel);
        logoPanel.add(proLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Create Your Account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(209, 213, 219));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        headerPanel.add(logoPanel);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createMainFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setLayout(new BorderLayout());

        // Form Card with Scroll Support
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 8));
                for (int i = 0; i < 5; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i, getWidth() - (i * 2), getHeight() - (i * 2), 15 + i, 15 + i));
                }
                
                // Card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Card border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            }
        };
        
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Form Title
        JLabel formTitle = new JLabel("Personal Information");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TEXT_PRIMARY);
        formTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(formTitle, gbc);

        // Form Fields
        gbc.gridwidth = 1;
        int row = 1;
        
        nameField = addModernField(formCard, "👤", "Full Name", "Enter your full name", row++, gbc);
        idField = addModernField(formCard, "🆔", "User ID", "Choose a unique user ID", row++, gbc);
        passField = addModernPasswordField(formCard, "🔒", "Password", "Create a secure password", row++, gbc);
        mobileField = addModernField(formCard, "📱", "Mobile Number", "Your contact number", row++, gbc);
        hallField = addModernField(formCard, "🏢", "Hall/Residence", "Your residence hall", row++, gbc);
        deptField = addModernField(formCard, "🎓", "Department", "Your department/major", row++, gbc);
        batchField = addModernField(formCard, "📅", "Batch", "Your batch/year", row++, gbc);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        formCard.add(buttonPanel, gbc);

        // Add scroll capability
        JScrollPane scrollPane = new JScrollPane(formCard);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Custom scroll bar styling
        scrollPane.getVerticalScrollBar().setBackground(BACKGROUND);
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

    private JTextField addModernField(JPanel parent, String icon, String label, String placeholder, 
                                    int row, GridBagConstraints gbc) {
        JPanel fieldPanel = createFieldPanel(icon, label);
        
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Field background
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(INPUT_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        field.setPreferredSize(new Dimension(0, 45));
        
        // Placeholder effect
        field.setText(placeholder);
        field.setForeground(TEXT_SECONDARY);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                    BorderFactory.createEmptyBorder(11, 15, 11, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        fieldPanel.add(field, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = row;
        parent.add(fieldPanel, gbc);
        
        return field;
    }

    private JPasswordField addModernPasswordField(JPanel parent, String icon, String label, 
                                                String placeholder, int row, GridBagConstraints gbc) {
        JPanel fieldPanel = createFieldPanel(icon, label);
        
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(INPUT_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        field.setPreferredSize(new Dimension(0, 45));
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setForeground(TEXT_SECONDARY);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('●');
                    field.setForeground(TEXT_PRIMARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                    BorderFactory.createEmptyBorder(11, 15, 11, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).trim().isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        fieldPanel.add(field, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = row;
        parent.add(fieldPanel, gbc);
        
        return field;
    }

    private JPanel createFieldPanel(String icon, String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        
        // Label with icon
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setBackground(CARD_BACKGROUND);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(TEXT_PRIMARY);
        
        labelPanel.add(iconLabel);
        labelPanel.add(textLabel);
        
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(CARD_BACKGROUND);
        
        registerBtn = createModernButton("Create Account", ACCENT_GREEN, "✅");
        backBtn = createModernButton("Back to Login", ACCENT_RED, "←");
        
        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        
        panel.add(backBtn);
        panel.add(registerBtn);
        
        return panel;
    }

    private JButton createModernButton(String text, Color bgColor, String icon) {
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
                
                // Button text with icon
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String buttonText = icon + " " + text;
                int x = (getWidth() - fm.stringWidth(buttonText)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(buttonText, x, y);
            }
        };
        
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 50));
        
        // Hover animation
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(249, 250, 251));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));
        
        JLabel footerLabel = new JLabel("🚀 Join thousands of users building better habits");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private void registerUser() {
        // Get field values, checking for placeholders
        String name = getFieldValue(nameField, "Enter your full name");
        String id = getFieldValue(idField, "Choose a unique user ID");
        String pass = getPasswordValue(passField, "Create a secure password");
        String mobile = getFieldValue(mobileField, "Your contact number");
        String hall = getFieldValue(hallField, "Your residence hall");
        String dept = getFieldValue(deptField, "Your department/major");
        String batch = getFieldValue(batchField, "Your batch/year");

        // Validation
        if (id.isEmpty() || pass.isEmpty()) {
            showModernDialog("⚠️ Validation Error", "User ID and Password are required!", WARNING_COLOR);
            return;
        }
        
        if (name.isEmpty()) {
            showModernDialog("⚠️ Validation Error", "Please enter your full name!", WARNING_COLOR);
            return;
        }

        if (UserFileHandler.idExists(id)) {
            showModernDialog("❌ Registration Failed", "This User ID already exists!\nPlease choose a different one.", ACCENT_RED);
            return;
        }

        // Save user
        if (UserFileHandler.saveUser(id, pass, name, mobile, hall, dept, batch, "")) {
            showModernDialog("🎉 Success!", "Account created successfully!\nYou can now login with your credentials.", SUCCESS_COLOR);
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            showModernDialog("❌ Error", "Failed to create account!\nPlease try again.", ACCENT_RED);
        }
    }

    private String getFieldValue(JTextField field, String placeholder) {
        String value = field.getText().trim();
        return value.equals(placeholder) ? "" : value;
    }

    private String getPasswordValue(JPasswordField field, String placeholder) {
        String value = new String(field.getPassword()).trim();
        return value.equals(placeholder) ? "" : value;
    }

    private void showModernDialog(String title, String message, Color accentColor) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(CARD_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_PRIMARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton okButton = createModernButton("OK", accentColor, "✓");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dialog.dispose());
        
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(okButton);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}