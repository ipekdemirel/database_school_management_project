package ui;

import db.DBConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    private final JTextField txtUser = new JTextField();
    private final JPasswordField txtPass = new JPasswordField();
    private final JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Student", "Teacher", "Admin"});
    private final JLabel lblStatus = new JLabel(" ");

    public LoginFrame() {
        setTitle("THK • School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));


        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 238), 1),
                BorderFactory.createEmptyBorder(28, 34, 28, 34)
        ));
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int cw = Math.min(860, (int)(scr.width * 0.78));
        int ch = Math.min(560, (int)(scr.height * 0.78));
        card.setPreferredSize(new Dimension(820, 560));



        JLabel logo = new JLabel(loadLogoIcon("/images/thu_logo.png", 110, 110));

        JLabel uni = new JLabel("Türk Hava Kurumu Üniversitesi");
        uni.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        uni.setForeground(new Color(35, 45, 60));

        JLabel title = new JLabel("School Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(20, 30, 45));

        JLabel sub = new JLabel("Please sign in to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(120, 130, 145));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 236, 244));

        JLabel lblRole = fieldLabel("Role");
        JLabel lblUser = fieldLabel("Username / ID");
        JLabel lblPass = fieldLabel("Password");

        styleField(txtUser, 520, 40);
        styleField(txtPass, 520, 40);

        cmbRole.setPreferredSize(new Dimension(240, 40));
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRole.putClientProperty("JComponent.roundRect", true);

        lblStatus.setForeground(new Color(200, 60, 60));
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton btnLogin = new JButton("Login");
        JButton btnExit = new JButton("Exit");
        JButton btnEdevlet = new JButton("E-Devlet ile Giriş");

        Dimension btnSize = new Dimension(160, 40);
        btnLogin.setPreferredSize(btnSize);
        btnExit.setPreferredSize(btnSize);
        btnEdevlet.setPreferredSize(new Dimension(200, 40));

        stylePrimary(btnLogin);
        styleGhost(btnExit);
        styleDanger(btnEdevlet);

        btnLogin.putClientProperty("JButton.arc", 18);
        btnExit.putClientProperty("JButton.arc", 18);
        btnEdevlet.putClientProperty("JButton.arc", 18);

        getRootPane().setDefaultButton(btnLogin);

        btnLogin.addActionListener(e -> doLogin());
        btnExit.addActionListener(e -> System.exit(0));
        btnEdevlet.addActionListener(e -> doEdevletLogin());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        card.add(logo, c);

        c.gridy++;
        card.add(uni, c);

        c.gridy++;
        card.add(title, c);

        c.gridy++;
        card.add(sub, c);

        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        card.add(sep, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridy++;
        card.add(lblRole, c);

        c.gridy++;
        card.add(cmbRole, c);

        c.gridy++;
        card.add(lblUser, c);

        c.gridy++;
        card.add(txtUser, c);

        c.gridy++;
        card.add(lblPass, c);

        c.gridy++;
        card.add(txtPass, c);

        c.gridy++;
        card.add(lblStatus, c);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnEdevlet);
        btnPanel.add(btnExit);
        btnPanel.add(btnLogin);

        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        card.add(btnPanel, c);

        root.add(card);
        setContentPane(root);
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(new Color(80, 90, 105));
        return l;
    }

    private void styleField(JTextField f, int w, int h) {
        f.setPreferredSize(new Dimension(w, h));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.putClientProperty("JComponent.roundRect", true);
    }

    private void stylePrimary(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBackground(new Color(0, 120, 212));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    private void styleGhost(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBackground(new Color(240, 242, 245));
        b.setForeground(new Color(55, 65, 80));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    private void styleDanger(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBackground(new Color(220, 53, 69));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    private Icon loadLogoIcon(String path, int w, int h) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return new ImageIcon();
            Image img = ImageIO.read(is).getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    private String mapRole(String uiRole) {
        if ("Teacher".equalsIgnoreCase(uiRole)) return "TEACHER";
        if ("Admin".equalsIgnoreCase(uiRole)) return "ADMIN";
        return "STUDENT";
    }

    private void doLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());
        String role = mapRole((String) cmbRole.getSelectedItem());

        if (u.isEmpty() || p.isEmpty()) {
            lblStatus.setText("Username/password required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT 1 FROM UserAccount WHERE Username=? AND PasswordHash=? AND Role=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u);
                ps.setString(2, p);
                ps.setString(3, role);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        openDashboard(u, role);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            lblStatus.setText("Database connection error.");
            return;
        }

        lblStatus.setText("Invalid credentials.");
    }

    private void doEdevletLogin() {
        JTextField tcField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JPanel p = new JPanel(new GridLayout(0, 1, 8, 8));
        p.add(new JLabel("TC Kimlik No"));
        p.add(tcField);
        p.add(new JLabel("E-Devlet Şifresi"));
        p.add(passField);

        int ok = JOptionPane.showConfirmDialog(
                this, p, "E-Devlet ile Giriş",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (ok != JOptionPane.OK_OPTION) return;

        String tc = tcField.getText().trim();
        String role = mapRole((String) cmbRole.getSelectedItem());

        if (tc.length() != 11) {
            lblStatus.setText("TC must be 11 digits.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT Username FROM UserAccount WHERE EDevletTC=? AND Role=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tc);
                ps.setString(2, role);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("Username");
                        openDashboard(username, role);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            lblStatus.setText("Database connection error.");
            return;
        }

        lblStatus.setText("E-Devlet login failed.");
    }

    private void openDashboard(String username, String role) {
        new PortalFrame(username).setVisible(true);
        dispose();
    }
}
