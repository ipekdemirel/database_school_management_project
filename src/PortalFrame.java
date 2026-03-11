package ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(String username) {
        setTitle("School System • Dashboard");
        setSize(720, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(18, 18, 22));

        // Top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(18, 18, 22));
        top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(235, 235, 235));

        JLabel user = new JLabel("Signed in: " + username);
        user.setForeground(new Color(170, 170, 170));

        top.add(title, BorderLayout.WEST);
        top.add(user, BorderLayout.EAST);

        // Cards area
        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setBackground(new Color(18, 18, 22));
        grid.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        grid.add(menuCard("Reports", "Stored procedures & analytics", () -> new ReportFrame().setVisible(true)));
        grid.add(menuCard("Students", "CRUD operations", () -> new StudentFrame().setVisible(true)));
        grid.add(menuCard("Teachers", "CRUD operations", () -> new TeacherFrame().setVisible(true)));
        grid.add(menuCard("Exit", "Close application", () -> System.exit(0)));

        root.add(top, BorderLayout.NORTH);
        root.add(grid, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel menuCard(String title, String desc, Runnable action) {
        JPanel card = AppTheme.card();
        card.setLayout(new BorderLayout(10, 10));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(new Color(235, 235, 235));

        JLabel d = new JLabel(desc);
        d.setForeground(new Color(170, 170, 170));

        JButton go = AppTheme.primaryButton("Open");
        go.addActionListener(e -> action.run());

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        text.add(t);
        text.add(d);

        card.add(text, BorderLayout.CENTER);
        card.add(go, BorderLayout.EAST);
        return card;
    }
}
