import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ProfileFrame extends JFrame {
    private String userId;
    private JLabel picLabel;
    private JTextField nameField, mobileField, deptField;
    private JButton uploadBtn, saveBtn, backBtn;
    private String currentPicPath = "";

    public ProfileFrame(String userId) {
        this.userId = userId;

        setTitle("Profile - " + userId);
        setSize(450, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));

        picLabel = new JLabel();
        picLabel.setBounds(150, 20, 150, 150);
        picLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        picLabel.setHorizontalAlignment(SwingConstants.CENTER);
        picLabel.setText("No Pic");
        panel.add(picLabel);

        uploadBtn = new JButton("Upload Picture");
        uploadBtn.setBounds(150, 180, 150, 30);
        panel.add(uploadBtn);

        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setBounds(50, 230, 100, 25);
        panel.add(nameLbl);
        nameField = new JTextField();
        nameField.setBounds(150, 230, 200, 25);
        panel.add(nameField);

        JLabel mobileLbl = new JLabel("Mobile:");
        mobileLbl.setBounds(50, 270, 100, 25);
        panel.add(mobileLbl);
        mobileField = new JTextField();
        mobileField.setBounds(150, 270, 200, 25);
        panel.add(mobileField);

        JLabel deptLbl = new JLabel("Dept:");
        deptLbl.setBounds(50, 310, 100, 25);
        panel.add(deptLbl);
        deptField = new JTextField();
        deptField.setBounds(150, 310, 200, 25);
        panel.add(deptField);

        saveBtn = new JButton("Save Changes");
        saveBtn.setBounds(50, 360, 140, 35);
        panel.add(saveBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(210, 360, 140, 35);
        panel.add(backBtn);

        add(panel, BorderLayout.CENTER);

        loadUserData();
        loadProfilePicture();

        uploadBtn.addActionListener(e -> uploadPicture());
        saveBtn.addActionListener(e -> saveProfile());
        backBtn.addActionListener(e -> {
            new DashboardFrame(userId, nameField.getText()).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void loadUserData() {
        try {
            File file = new File("users.txt");
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(userId)) {
                    nameField.setText(parts[2]);
                    mobileField.setText(parts.length > 7 ? parts[7] : "");
                    deptField.setText(parts.length > 4 ? parts[4] : "");
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProfilePicture() {
        File picDir = new File("profile_pics");
        if (!picDir.exists()) {
            picDir.mkdir();
        }

        File picFile = new File("profile_pics/" + userId + "_profile.jpg");
        if (picFile.exists()) {
            currentPicPath = picFile.getAbsolutePath();
            ImageIcon icon = new ImageIcon(currentPicPath);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            picLabel.setIcon(new ImageIcon(img));
            picLabel.setText("");
        }
    }

    private void uploadPicture() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                       name.endsWith(".png") || name.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });

        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();
                
                File picDir = new File("profile_pics");
                if (!picDir.exists()) {
                    picDir.mkdir();
                }

                String extension = getFileExtension(selectedFile.getName());
                File destFile = new File("profile_pics/" + userId + "_profile" + extension);
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                currentPicPath = destFile.getAbsolutePath();

                ImageIcon icon = new ImageIcon(currentPicPath);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                picLabel.setIcon(new ImageIcon(img));
                picLabel.setText("");

                JOptionPane.showMessageDialog(this, "Picture uploaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error uploading picture: " + e.getMessage());
            }
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileName.substring(lastDot);
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String dept = deptField.getText().trim();

        if (name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Department are required!");
            return;
        }

        try {
            File file = new File("users.txt");
            List<String> lines = new ArrayList<>();
            boolean userFound = false;

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3 && parts[0].equals(userId)) {
                        String updatedLine = parts[0] + "," + parts[1] + "," + name + "," + 
                                           (parts.length > 3 ? parts[3] : "") + "," + dept + "," +
                                           (parts.length > 5 ? parts[5] : "") + "," + 
                                           (parts.length > 6 ? parts[6] : "") + "," + mobile;
                        lines.add(updatedLine);
                        userFound = true;
                    } else {
                        lines.add(line);
                    }
                }
                reader.close();
            }

            if (userFound) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found in file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving profile: " + e.getMessage());
        }
    }
}
