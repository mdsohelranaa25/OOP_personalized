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

 
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);      // Blue-600
    private static final Color SECONDARY_COLOR = new Color(99, 102, 241);   // Indigo-500
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Slate-50
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);        // Slate-900
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);     // Slate-600
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);      // Green-500
    private static final Color WARNING_COLOR = new Color(245, 158, 11);     // Amber-500
    private static final Color ERROR_COLOR = new Color(239, 68, 68);        // Red-500

    public LoginFrame() {
        setTitle("Personalized Habit Tracker - Login");
        setSize(500, 700);
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

        add(mainPanel);

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
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
      
        JLabel titleLabel = new JLabel("Personalized Habit Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
       
        JLabel subtitleLabel = new JLabel("Track your habits, Transform your life");
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

       
        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Please sign in to your account");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(TEXT_SECONDARY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        JPanel idPanel = createInputPanel("User ID", "Enter your user ID");
        
  
        JPanel passPanel = createPasswordPanel();

       
        loginBtn = createPrimaryButton("Sign In", PRIMARY_COLOR);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(welcomeLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(instructionLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(idPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(passPanel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(loginBtn);

        return cardPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noAccountLabel.setForeground(TEXT_SECONDARY);

        regBtn = createTextButton("Create Account", SECONDARY_COLOR);

        footerPanel.add(noAccountLabel);
        footerPanel.add(Box.createHorizontalStrut(5));
        footerPanel.add(regBtn);

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

    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel("Password");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            new EmptyBorder(12, 16, 12, 45)
        ));
        passField.setBackground(Color.WHITE);
        passField.setForeground(TEXT_PRIMARY);

        
        showPasswordLabel = new JLabel("👁");
        showPasswordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
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
        });

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
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        optionPane.setBackground(BACKGROUND_COLOR);
        JDialog dialog = optionPane.createDialog(this, "Login Error");
        dialog.setVisible(true);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        optionPane.setBackground(BACKGROUND_COLOR);
        JDialog dialog = optionPane.createDialog(this, "Success");
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