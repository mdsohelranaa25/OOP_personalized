import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResetPasswordFrame extends JFrame {
    private JTextField idField, mobileField;
    private JPasswordField newPassField, confirmPassField;
    private JButton updateBtn, backBtn;
    private JLabel showNewPasswordLabel, showConfirmPasswordLabel;
    private boolean newPasswordVisible = false;
    private boolean confirmPasswordVisible = false;

    // Modern Color Palette (same as LoginFrame)
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);      // Blue-600
    private static final Color SECONDARY_COLOR = new Color(99, 102, 241);   // Indigo-500
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Slate-50
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);        // Slate-900
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);     // Slate-600
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);      // Green-500
    private static final Color WARNING_COLOR = new Color(245, 158, 11);     // Amber-500
    private static final Color ERROR_COLOR = new Color(239, 68, 68);        // Red-500

    public ResetPasswordFrame() {
        setTitle("Reset Password - HabitTracker Pro");
        setSize(500, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set custom background
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Main container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Actions
        updateBtn.addActionListener(e -> updatePassword());
        backBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            dispose();
        });

        // Enter key support
        getRootPane().setDefaultButton(updateBtn);
        
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // App Icon
        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Enter your details to reset your password");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(40, 30, 40, 30)
        ));

        // User ID Field
        JPanel idPanel = createInputPanel("User ID", "Enter your user ID");
        
        // Mobile Field
        JPanel mobilePanel = createInputPanel("Mobile Number", "Enter your mobile number");

        // New Password Field
        JPanel newPassPanel = createPasswordPanel("New Password", true);
        
        // Confirm Password Field
        JPanel confirmPassPanel = createPasswordPanel("Confirm Password", false);

        // Update Button
        updateBtn = createPrimaryButton("Reset Password", PRIMARY_COLOR);
        updateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with spacing
        cardPanel.add(idPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(mobilePanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(newPassPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(confirmPassPanel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(updateBtn);

        return cardPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel rememberLabel = new JLabel("Remember your password?");
        rememberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rememberLabel.setForeground(TEXT_SECONDARY);

        backBtn = createTextButton("Back to Login", SECONDARY_COLOR);

        footerPanel.add(rememberLabel);
        footerPanel.add(Box.createHorizontalStrut(5));
        footerPanel.add(backBtn);

        return footerPanel;
    }

    private JPanel createInputPanel(String labelText, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            new EmptyBorder(12, 16, 12, 16)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);

        // Store references
        if (labelText.equals("User ID")) {
            idField = field;
        } else if (labelText.equals("Mobile Number")) {
            mobileField = field;
        }

        // Placeholder effect
        field.setText(placeholder);
        field.setForeground(TEXT_SECONDARY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
            }
        });

        fieldPanel.add(field);
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(fieldPanel);

        return panel;
    }

    private JPanel createPasswordPanel(String labelText, boolean isNewPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            new EmptyBorder(12, 16, 12, 45)
        ));
        passField.setBackground(Color.WHITE);
        passField.setForeground(TEXT_PRIMARY);

        // Store references
        if (isNewPassword) {
            newPassField = passField;
        } else {
            confirmPassField = passField;
        }

        // Show/Hide password toggle
        JLabel showPasswordLabel = new JLabel("👁");
        showPasswordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
        
        if (isNewPassword) {
            showNewPasswordLabel = showPasswordLabel;
            showPasswordLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    newPasswordVisible = !newPasswordVisible;
                    if (newPasswordVisible) {
                        newPassField.setEchoChar((char) 0);
                        showNewPasswordLabel.setText("🙈");
                    } else {
                        newPassField.setEchoChar('•');
                        showNewPasswordLabel.setText("👁");
                    }
                }
            });
        } else {
            showConfirmPasswordLabel = showPasswordLabel;
            showPasswordLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    confirmPasswordVisible = !confirmPasswordVisible;
                    if (confirmPasswordVisible) {
                        confirmPassField.setEchoChar((char) 0);
                        showConfirmPasswordLabel.setText("🙈");
                    } else {
                        confirmPassField.setEchoChar('•');
                        showConfirmPasswordLabel.setText("👁");
                    }
                }
            });
        }

        fieldPanel.add(passField, BorderLayout.CENTER);
        fieldPanel.add(showPasswordLabel, BorderLayout.EAST);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(fieldPanel);

        return panel;
    }

    private JButton createPrimaryButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 48));
        button.setMaximumSize(new Dimension(300, 48));
        
        return button;
    }

    private JButton createTextButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(color);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(color);
            }
        });
        
        return button;
    }

    private void updatePassword() {
        String id = idField.getText().trim();
        String mobile = mobileField.getText().trim();
        String newPass = new String(newPassField.getPassword()).trim();
        String confirmPass = new String(confirmPassField.getPassword()).trim();
        
        // Check for placeholder text and empty fields
        if (id.equals("Enter your user ID") || id.isEmpty()) {
            showErrorMessage("Please enter your User ID");
            idField.requestFocus();
            return;
        }
        
        if (mobile.equals("Enter your mobile number") || mobile.isEmpty()) {
            showErrorMessage("Please enter your mobile number");
            mobileField.requestFocus();
            return;
        }
        
        if (newPass.isEmpty()) {
            showErrorMessage("Please enter a new password");
            newPassField.requestFocus();
            return;
        }
        
        if (confirmPass.isEmpty()) {
            showErrorMessage("Please confirm your password");
            confirmPassField.requestFocus();
            return;
        }
        
        // Password validation
        if (newPass.length() < 4) {
            showErrorMessage("Password must be at least 4 characters long");
            newPassField.requestFocus();
            return;
        }
        
        if (!newPass.equals(confirmPass)) {
            showErrorMessage("Passwords do not match");
            confirmPassField.requestFocus();
            return;
        }
        
        // Show loading state
        updateBtn.setText("Updating...");
        updateBtn.setEnabled(false);
        
        // Simulate loading delay
        Timer timer = new Timer(500, e -> {
            boolean validUser = UserFileHandler.validateUserForReset(id, mobile);
            if (!validUser) {
                showErrorMessage("User ID or Mobile number not found or do not match");
                updateBtn.setText("Reset Password");
                updateBtn.setEnabled(true);
                return;
            }
            
            boolean updated = UserFileHandler.updatePassword(id, newPass);
            if (updated) {
                showSuccessMessage("Password successfully updated!");
                Timer successTimer = new Timer(1000, _ -> {
                    SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
                    dispose();
                });
                successTimer.setRepeats(false);
                successTimer.start();
            } else {
                showErrorMessage("Error updating password. Please try again.");
                updateBtn.setText("Reset Password");
                updateBtn.setEnabled(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void showErrorMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        optionPane.setBackground(BACKGROUND_COLOR);
        JDialog dialog = optionPane.createDialog(this, "Reset Password Error");
        dialog.setVisible(true);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        optionPane.setBackground(BACKGROUND_COLOR);
        JDialog dialog = optionPane.createDialog(this, "Success");
        dialog.setVisible(true);
    }
}