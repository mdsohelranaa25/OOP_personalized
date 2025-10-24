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
    
    
    private static final Color PRIMARY_DARK = new Color(15, 23, 42);        // Slate 900
    private static final Color PRIMARY_LIGHT = new Color(30, 41, 59);       // Slate 800
    private static final Color ACCENT_BLUE = new Color(37, 99, 235);        // Blue 600
    private static final Color ACCENT_GREEN = new Color(5, 150, 105);       // Emerald 600
    private static final Color ACCENT_RED = new Color(220, 38, 38);         // Red 600
    private static final Color BACKGROUND = new Color(248, 250, 252);       // Slate 50
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);        // Slate 900
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);     // Slate 600
    private static final Color BORDER_COLOR = new Color(203, 213, 225);     // Slate 300
    private static final Color INPUT_BACKGROUND = new Color(248, 250, 252); // Slate 50
    private static final Color SUCCESS_COLOR = new Color(5, 150, 105);      // Emerald 600
    private static final Color WARNING_COLOR = new Color(217, 119, 6);      // Amber 600
    private static final Color PROFESSIONAL_PURPLE = new Color(99, 102, 241); // Indigo 500

    public RegisterFrame() {
        setTitle("Personalized Habit Tracker - Create Account");
        setSize(650, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(600, 650));
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
        
        // Professional window border
        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));

        initializeComponents();
    }

    private void initializeComponents() {
        // Professional Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Main Form Panel
        add(createMainFormPanel(), BorderLayout.CENTER);
        
        // Professional Footer Panel
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Professional gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_DARK,
                    getWidth(), getHeight(), PROFESSIONAL_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle geometric pattern overlay
                g2d.setColor(new Color(255, 255, 255, 8));
                for (int i = 0; i < getWidth(); i += 30) {
                    for (int j = 0; j < getHeight(); j += 30) {
                        g2d.fillOval(i, j, 2, 2);
                    }
                }
            }
        };
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 160));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Professional Logo and App Name
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        
        // Professional icon
        JLabel logoLabel = new JLabel("📊");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        
        JLabel appNameLabel = new JLabel("Personalized");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 5));
        
        JLabel trackerLabel = new JLabel("Habit Tracker");
        trackerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        trackerLabel.setForeground(new Color(226, 232, 240)); // Slate 200
        
        logoPanel.add(logoLabel);
        logoPanel.add(appNameLabel);
        logoPanel.add(trackerLabel);

        // Professional subtitle
        JLabel subtitleLabel = new JLabel("Create Your Professional Account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(new Color(203, 213, 225)); // Slate 300
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Professional tagline
        JLabel taglineLabel = new JLabel("Build Better Habits, Achieve Greater Success");
        taglineLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        taglineLabel.setForeground(new Color(148, 163, 184)); // Slate 400
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        headerPanel.add(logoPanel);
        headerPanel.add(subtitleLabel);
        headerPanel.add(taglineLabel);

        return headerPanel;
    }

    private JPanel createMainFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));
        mainPanel.setLayout(new BorderLayout());

        // Professional Form Card with enhanced shadow
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced card shadow
                g2d.setColor(new Color(15, 23, 42, 15)); // Darker shadow
                for (int i = 0; i < 8; i++) {
                    g2d.fill(new RoundRectangle2D.Float(i, i, getWidth() - (i * 2), getHeight() - (i * 2), 20 + i, 20 + i));
                }
                
                // Card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Professional border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
            }
        };
        
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Professional Form Title
        JLabel formTitle = new JLabel("Registration Form");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        formTitle.setForeground(TEXT_PRIMARY);
        formTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(formTitle, gbc);

        JLabel formSubtitle = new JLabel("Please fill in all required information");
        formSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formSubtitle.setForeground(TEXT_SECONDARY);
        formSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        formCard.add(formSubtitle, gbc);

        // Form Fields with professional icons
        gbc.gridwidth = 1;
        gbc.insets = new Insets(12, 0, 12, 0);
        int row = 2;
        
        nameField = addProfessionalField(formCard, "👤", "Full Name", "Enter your complete name", row++, gbc, true);
        idField = addProfessionalField(formCard, "🆔", "User ID", "Choose a unique identifier", row++, gbc, true);
        passField = addProfessionalPasswordField(formCard, "🔐", "Password", "Create a secure password", row++, gbc);
        mobileField = addProfessionalField(formCard, "📱", "Mobile Number", "Your contact number", row++, gbc, false);
        hallField = addProfessionalField(formCard, "🏢", "Hall/Residence", "Your residence information", row++, gbc, false);
        deptField = addProfessionalField(formCard, "🎓", "Department", "Your department/major", row++, gbc, false);
        batchField = addProfessionalField(formCard, "📅", "Batch/Year", "Your academic batch", row++, gbc, false);

        // Professional Button Panel
        JPanel buttonPanel = createProfessionalButtonPanel();
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        formCard.add(buttonPanel, gbc);

        // Enhanced scroll pane
        JScrollPane scrollPane = new JScrollPane(formCard);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Professional scroll bar styling
        scrollPane.getVerticalScrollBar().setBackground(BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(148, 163, 184); // Slate 400
                this.trackColor = BACKGROUND;
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JTextField addProfessionalField(JPanel parent, String icon, String label, String placeholder, 
                                          int row, GridBagConstraints gbc, boolean required) {
        JPanel fieldPanel = createProfessionalFieldPanel(icon, label, required);
        
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Professional field background
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(INPUT_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        field.setPreferredSize(new Dimension(0, 48));
        
        // Professional placeholder effect
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
                    BorderFactory.createEmptyBorder(13, 17, 13, 17)
                ));
                field.setBackground(Color.WHITE);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(14, 18, 14, 18)
                ));
                field.setBackground(INPUT_BACKGROUND);
            }
        });

        fieldPanel.add(field, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = row;
        parent.add(fieldPanel, gbc);
        
        return field;
    }

    private JPasswordField addProfessionalPasswordField(JPanel parent, String icon, String label, 
                                                       String placeholder, int row, GridBagConstraints gbc) {
        JPanel fieldPanel = createProfessionalFieldPanel(icon, label, true);
        
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setBackground(INPUT_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        field.setPreferredSize(new Dimension(0, 48));
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
                    BorderFactory.createEmptyBorder(13, 17, 13, 17)
                ));
                field.setBackground(Color.WHITE);
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
                    BorderFactory.createEmptyBorder(14, 18, 14, 18)
                ));
                field.setBackground(INPUT_BACKGROUND);
            }
        });

        fieldPanel.add(field, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = row;
        parent.add(fieldPanel, gbc);
        
        return field;
    }

    private JPanel createProfessionalFieldPanel(String icon, String label, boolean required) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        
        // Professional label with icon and required indicator
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setBackground(CARD_BACKGROUND);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(TEXT_PRIMARY);
        
        labelPanel.add(iconLabel);
        labelPanel.add(textLabel);
        
        if (required) {
            JLabel requiredLabel = new JLabel("*");
            requiredLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            requiredLabel.setForeground(ACCENT_RED);
            requiredLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
            labelPanel.add(requiredLabel);
        }
        
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        return panel;
    }

    private JPanel createProfessionalButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 25, 0));
        panel.setBackground(CARD_BACKGROUND);
        
        registerBtn = createProfessionalButton("Create Account", ACCENT_GREEN, "✓");
        backBtn = createProfessionalButton("Back to Login", TEXT_SECONDARY, "←");
        
        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        
        panel.add(backBtn);
        panel.add(registerBtn);
        
        return panel;
    }

    private JButton createProfessionalButton(String text, Color bgColor, String icon) {
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
                        Math.min(255, bgColor.getRed() + 15),
                        Math.min(255, bgColor.getGreen() + 15),
                        Math.min(255, bgColor.getBlue() + 15)
                    );
                }
                
                g2d.setColor(currentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Professional button text with icon
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String buttonText = icon + "  " + text;
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
        button.setPreferredSize(new Dimension(0, 52));
        
        // Professional hover animation
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
        footerPanel.setBackground(BACKGROUND);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 0, 20, 0)
        ));
        
        JLabel footerLabel = new JLabel("🎯 Transform Your Life Through Personalized Habit Tracking");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private void registerUser() {
        // Get field values, checking for placeholders
        String name = getFieldValue(nameField, "Enter your complete name");
        String id = getFieldValue(idField, "Choose a unique identifier");
        String pass = getPasswordValue(passField, "Create a secure password");
        String mobile = getFieldValue(mobileField, "Your contact number");
        String hall = getFieldValue(hallField, "Your residence information");
        String dept = getFieldValue(deptField, "Your department/major");
        String batch = getFieldValue(batchField, "Your academic batch");

        // Professional validation
        if (id.isEmpty() || pass.isEmpty()) {
            showProfessionalDialog("⚠️ Validation Required", "User ID and Password are mandatory fields!", WARNING_COLOR);
            return;
        }
        
        if (name.isEmpty()) {
            showProfessionalDialog("⚠️ Validation Required", "Please provide your complete name!", WARNING_COLOR);
            return;
        }

        if (UserFileHandler.idExists(id)) {
            showProfessionalDialog("❌ Registration Failed", "This User ID is already registered!\nPlease choose a different identifier.", ACCENT_RED);
            return;
        }

        // Save user
        if (UserFileHandler.saveUser(id, pass, name, mobile, hall, dept, batch, "")) {
            showProfessionalDialog("🎉 Account Created!", "Registration completed successfully!\nYou may now login with your credentials.", SUCCESS_COLOR);
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            showProfessionalDialog("❌ System Error", "Unable to create account at this time.\nPlease try again later.", ACCENT_RED);
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

    private void showProfessionalDialog(String title, String message, Color accentColor) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(420, 220);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(CARD_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(35, 35, 25, 35));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_PRIMARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton okButton = createProfessionalButton("OK", accentColor, "✓");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dialog.dispose());
        
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(okButton);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}