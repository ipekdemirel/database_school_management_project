package ui.panels;

import db.DBConnection;
import db.DashboardDAO;
import ui.AppTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardPanel extends JPanel {

    private final JLabel lblStudents = new JLabel("-");
    private final JLabel lblTeachers = new JLabel("-");
    private final JLabel lblClasses  = new JLabel("-");
    private final JLabel lblCourses  = new JLabel("-");
    private final JLabel lblEnroll   = new JLabel("-");
    private final JLabel lblTopAvg   = new JLabel("-");

    private final JTable table = new JTable();

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 18));

        JPanel topGrid = new JPanel(new GridLayout(2, 3, 14, 14));
        topGrid.setBackground(new Color(15, 15, 18));
        topGrid.setBorder(BorderFactory.createEmptyBorder(16, 16, 10, 16));

        topGrid.add(statCard("Students", lblStudents, new Color(0, 150, 136)));
        topGrid.add(statCard("Teachers", lblTeachers, new Color(33, 150, 243)));
        topGrid.add(statCard("Classes",  lblClasses,  new Color(156, 39, 176)));
        topGrid.add(statCard("Courses",  lblCourses,  new Color(255, 152, 0)));
        topGrid.add(statCard("Enrollments", lblEnroll, new Color(244, 67, 54)));
        topGrid.add(statCard("Top Student Avg", lblTopAvg, new Color(76, 175, 80)));

        JPanel mid = new JPanel(new BorderLayout());
        mid.setBackground(new Color(15, 15, 18));
        mid.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        JPanel tableCard = AppTheme.card();
        tableCard.setLayout(new BorderLayout(10, 10));
        tableCard.setBackground(new Color(18, 18, 22));
        tableCard.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel t = new JLabel("Recent Grades (last 10)");
        t.setForeground(new Color(235, 235, 235));
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        styleTableDark(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(new Color(18, 18, 22));
        sp.setBackground(new Color(18, 18, 22));

        tableCard.add(t, BorderLayout.NORTH);
        tableCard.add(sp, BorderLayout.CENTER);

        mid.add(tableCard, BorderLayout.CENTER);

        add(topGrid, BorderLayout.NORTH);
        add(mid, BorderLayout.CENTER);

        refresh();
    }

    private JPanel statCard(String title, JLabel valueLabel, Color accent) {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(accent);
        c.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel(title);
        t.setForeground(new Color(245, 245, 245));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);

        c.add(t, BorderLayout.NORTH);
        c.add(valueLabel, BorderLayout.CENTER);
        return c;
    }

    private void styleTableDark(JTable table) {
        table.setBackground(new Color(18, 18, 22));
        table.setForeground(new Color(230, 230, 230));
        table.setGridColor(new Color(45, 45, 55));
        table.setSelectionBackground(new Color(60, 90, 160));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader th = table.getTableHeader();
        th.setBackground(new Color(24, 24, 30));
        th.setForeground(new Color(230, 230, 230));
        th.setReorderingAllowed(false);
    }

    public void refresh() {
        int students = DashboardDAO.count("SELECT COUNT(*) FROM Student");
        int teachers = DashboardDAO.count("SELECT COUNT(*) FROM Teacher");
        int classes  = DashboardDAO.count("SELECT COUNT(*) FROM Class");
        int courses  = DashboardDAO.count("SELECT COUNT(*) FROM Course");
        int enroll   = DashboardDAO.count("SELECT COUNT(*) FROM Enrollment");

        double topAvg = DashboardDAO.scalarDouble(
                "SELECT IFNULL(MAX(avg_score),0) FROM (" +
                        "SELECT e.student_id, AVG(g.score) avg_score " +
                        "FROM Enrollment e JOIN Grade g ON g.enrollment_id = e.enrollment_id " +
                        "GROUP BY e.student_id" +
                        ") t"
        );

        lblStudents.setText(String.valueOf(students));
        lblTeachers.setText(String.valueOf(teachers));
        lblClasses.setText(String.valueOf(classes));
        lblCourses.setText(String.valueOf(courses));
        lblEnroll.setText(String.valueOf(enroll));
        lblTopAvg.setText(String.format("%.2f", topAvg));

        loadRecentGrades();
    }

    private void loadRecentGrades() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Student", "Course", "Midterm", "Final", "Average"}, 0
        );

        String sql =
                "SELECT CONCAT(s.first_name,' ',s.last_name) AS student_name, " +
                        "       cr.course_name, " +
                        "       MAX(CASE WHEN g.exam_type='midterm' THEN g.score END) AS midterm, " +
                        "       MAX(CASE WHEN g.exam_type='final' THEN g.score END) AS final_score, " +
                        "       ROUND(AVG(g.score),2) AS average_score " +
                        "FROM Grade g " +
                        "JOIN Enrollment e ON e.enrollment_id = g.enrollment_id " +
                        "JOIN Student s ON s.student_id = e.student_id " +
                        "JOIN Class c ON c.class_id = e.class_id " +
                        "JOIN Course cr ON cr.course_id = c.course_id " +
                        "GROUP BY s.student_id, cr.course_id " +
                        "ORDER BY MAX(g.grade_id) DESC " +
                        "LIMIT 10";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("student_name"),
                        rs.getString("course_name"),
                        rs.getObject("midterm"),
                        rs.getObject("final_score"),
                        rs.getDouble("average_score")
                });
            }

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            table.setModel(model);
        }
    }
}
