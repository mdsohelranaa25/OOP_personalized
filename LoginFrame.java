import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField idField;
    private JPasswordField passField;
    private JButton loginBtn, regBtn, forgotBtn;
    private JLabel showPasswordLabel;
    private int wrongAttempts = 0;
    private boolean passwordVisible = false;

    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);
    private static final Color PRIMARY_HOVER = new Color(67, 56, 202);
    private static final Color SECONDARY_COLOR = new Color(139, 92, 246);
    private static final Color BACKGROUND_COLOR = new Color(15, 23, 42);
    private static final Color CARD_COLOR = new Color(30, 41, 59);
    private static final Color INPUT_COLOR = new Color(51, 65, 85);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color ACCENT_COLOR = new Color(14, 165, 233);

    public LoginFrame() {
        setTitle("Personalized Habit Tracker - Login");
        setSize(520, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_COLOR;
                this.trackColor = BACKGROUND_COLOR;
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

        add(scrollPane);

        loginBtn.addActionListener(e -> loginUser());
        regBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        getRootPane().setDefaultButton(loginBtn);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 40, 0));

        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Personalized Habit Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Track your habits, Transform your life");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, CARD_COLOR, 0, getHeight(), CARD_COLOR.darker());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);

                g2d.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Please sign in to your account");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        instructionLabel.setForeground(TEXT_SECONDARY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel idPanel = createInputPanel("User ID", "Enter your user ID");
        JPanel passPanel = createPasswordPanel();

        loginBtn = createPrimaryButton("Sign In", PRIMARY_COLOR);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(welcomeLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(instructionLabel);
        cardPanel.add(Box.createVerticalStrut(40));
        cardPanel.add(idPanel);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(passPanel);
        cardPanel.add(Box.createVerticalStrut(40));
        cardPanel.add(loginBtn);

        return cardPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        noAccountLabel.setForeground(TEXT_SECONDARY);

        regBtn = createTextButton("Create Account", ACCENT_COLOR);

        footerPanel.add(noAccountLabel);
        footerPanel.add(Box.createHorizontalStrut(8));
        footerPanel.add(regBtn);

        return footerPanel;
    }

    private JPanel createInputPanel(String labelText, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel fieldPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(INPUT_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(BORDER_COLOR);
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2d.dispose();
            }
        };
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(new EmptyBorder(15, 20, 15, 20));
        field.setOpaque(false);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);

        if (labelText.equals("User ID")) {
            idField = field;
        }

        field.setText(placeholder);
        field.setForeground(TEXT_SECONDARY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
                fieldPanel.repaint();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
                fieldPanel.repaint();
            }
        });

        fieldPanel.add(field);

        panel.add(label);
        panel.add(Box.createVerticalStrut(12));
        panel.add(fieldPanel);

        return panel;
    }

    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel label = new JLabel("Password");
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel fieldPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(INPUT_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(BORDER_COLOR);
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2d.dispose();
            }
        };
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passField.setBorder(new EmptyBorder(15, 20, 15, 50));
        passField.setOpaque(false);
        passField.setForeground(TEXT_PRIMARY);
        passField.setCaretColor(TEXT_PRIMARY);

        showPasswordLabel = new JLabel("👁") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getMousePosition() != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(255, 255, 255, 20));
                    g2d.fillOval(2, 2, getWidth()-4, getHeight()-4);
                    g2d.dispose();
                }
            }
        };
        showPasswordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        showPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordLabel.setBorder(new EmptyBorder(0, 0, 0, 18));
        showPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordVisible = !passwordVisible;
                if (passwordVisible) {
                    passField.setEchoChar((char) 0);
                    showPasswordLabel.setText("🙈");
                } else {
                    passField.setEchoChar('•');
                    showPasswordLabel.setText("👁");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                showPasswordLabel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                showPasswordLabel.repaint();
            }
        });

        fieldPanel.add(passField, BorderLayout.CENTER);
        fieldPanel.add(showPasswordLabel, BorderLayout.EAST);

        panel.add(label);
        panel.add(Box.createVerticalStrut(12));
        panel.add(fieldPanel);

        return panel;
    }

    private JButton createPrimaryButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = PRIMARY_HOVER.darker();
                } else if (getModel().isRollover()) {
                    buttonColor = PRIMARY_HOVER;
                } else {
                    buttonColor = bgColor;
                }

                GradientPaint gradient = new GradientPaint(0, 0, buttonColor, 0, getHeight(), buttonColor.darker());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 12, 12);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 12, 12);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(320, 52));
        button.setMaximumSize(new Dimension(320, 52));

        return button;
    }

    private JButton createTextButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(color);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(color);
            }
        });

        return button;
    }

    private void loginUser() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        if (id.equals("Enter your user ID") || id.isEmpty()) {
            showErrorMessage("Please enter your User ID");
            idField.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            showErrorMessage("Please enter your password");
            passField.requestFocus();
            return;
        }

        loginBtn.setText("Signing in...");
        loginBtn.setEnabled(false);

        Timer timer = new Timer(500, e -> {
            String userName = UserFileHandler.validateLogin(id, pass);
            if (userName != null) {
                showSuccessMessage("Login successful!");
                Timer successTimer = new Timer(800, _ -> {
                    new DashboardFrame(id, userName).setVisible(true);
                    dispose();
                });
                successTimer.setRepeats(false);
                successTimer.start();
            } else {
                wrongAttempts++;
                showErrorMessage("Invalid User ID or Password");
                loginBtn.setText("Sign In");
                loginBtn.setEnabled(true);

                if (wrongAttempts >= 1) {
                    forgotBtn.setVisible(true);
                    revalidate();
                    repaint();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showErrorMessage(String message) {
        JDialog dialog = new JDialog(this, "Login Error", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_COLOR);

        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(ERROR_COLOR);
        messageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton okButton = new JButton("OK");
        okButton.setBackground(ERROR_COLOR);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(okButton);

        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showSuccessMessage(String message) {
        JDialog dialog = new JDialog(this, "Success", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_COLOR);

        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(SUCCESS_COLOR);
        messageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton okButton = new JButton("OK");
        okButton.setBackground(SUCCESS_COLOR);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(okButton);

        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
