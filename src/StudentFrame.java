package ui;

import ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class PortalFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    public PortalFrame(String username) {
        setTitle("School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(15, 15, 18));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(20, 20, 24));
        top.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(235, 235, 235));

        JLabel lblUser = new JLabel("User: " + username);
        lblUser.setForeground(new Color(170, 170, 170));

        top.add(lblTitle, BorderLayout.WEST);
        top.add(lblUser, BorderLayout.EAST);

        JPanel side = new JPanel();
        side.setBackground(new Color(18, 18, 22));
        side.setPreferredSize(new Dimension(260, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        JLabel brand = new JLabel("THK • SMS");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(new Color(235, 235, 235));
        brand.setBorder(BorderFactory.createEmptyBorder(0, 6, 18, 6));

        SidebarItem btnDash = new SidebarItem("Dashboard");
        SidebarItem btnStudents = new SidebarItem("Students");
        SidebarItem btnTeachers = new SidebarItem("Teachers");
        SidebarItem btnCourses = new SidebarItem("Courses");
        SidebarItem btnReports = new SidebarItem("Reports");

        JButton btnLogout = AppTheme.primaryButton("Logout");
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(brand);
        side.add(btnDash);
        side.add(Box.createVerticalStrut(8));
        side.add(btnStudents);
        side.add(Box.createVerticalStrut(8));
        side.add(btnTeachers);
        side.add(Box.createVerticalStrut(8));
        side.add(btnCourses);
        side.add(Box.createVerticalStrut(8));
        side.add(btnReports);
        side.add(Box.createVerticalGlue());
        side.add(btnLogout);

        content.setBackground(new Color(15, 15, 18));
        content.add(new DashboardPanel(), "dashboard");
        content.add(new StudentsPanel(), "students");
        content.add(new TeachersPanel(), "teachers");
        content.add(new CoursesPanel(), "courses");
        content.add(new ReportsPanel(), "reports");

        setActive(btnDash, lblTitle, "Dashboard", "dashboard");

        btnDash.addActionListener(e -> setActive(btnDash, lblTitle, "Dashboard", "dashboard"));
        btnStudents.addActionListener(e -> setActive(btnStudents, lblTitle, "Students", "students"));
        btnTeachers.addActionListener(e -> setActive(btnTeachers, lblTitle, "Teachers", "teachers"));
        btnCourses.addActionListener(e -> setActive(btnCourses, lblTitle, "Courses", "courses"));
        btnReports.addActionListener(e -> setActive(btnReports, lblTitle, "Reports", "reports"));

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        root.add(top, BorderLayout.NORTH);
        root.add(side, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void setActive(SidebarItem active, JLabel topTitle, String title, String cardName) {
        for (Component c : active.getParent().getComponents()) {
            if (c instanceof SidebarItem item) item.setActive(false);
        }
        active.setActive(true);
        topTitle.setText(title);
        cards.show(content, cardName);
    }

    static class SidebarItem extends JButton {
        private boolean active = false;

        SidebarItem(String text) {
            super(text);
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(true);
            setOpaque(true);

            setBackground(new Color(18, 18, 22));
            setForeground(new Color(220, 220, 220));
            setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!active) setBackground(new Color(28, 28, 34));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!active) setBackground(new Color(18, 18, 22));
                }
            });
        }

        void setActive(boolean value) {
            this.active = value;
            if (active) {
                setBackground(new Color(33, 150, 243));
                setForeground(Color.WHITE);
            } else {
                setBackground(new Color(18, 18, 22));
                setForeground(new Color(220, 220, 220));
            }
        }
    }
}
