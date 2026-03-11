package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherFrame extends JFrame {

    private JTable table;
    private JTextField txtFirstName, txtLastName;

    public TeacherFrame() {
        setTitle("Teacher Management");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel top = new JPanel();
        txtFirstName = new JTextField(10);
        txtLastName = new JTextField(10);

        JButton btnAdd = new JButton("Add Teacher");
        JButton btnList = new JButton("List Teachers");

        top.add(new JLabel("First Name"));
        top.add(txtFirstName);
        top.add(new JLabel("Last Name"));
        top.add(txtLastName);
        top.add(btnAdd);
        top.add(btnList);

        table = new JTable();
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addTeacher());
        btnList.addActionListener(e -> loadTeachers());
    }

    private void addTeacher() {
        String fn = txtFirstName.getText().trim();
        String ln = txtLastName.getText().trim();

        if (fn.isEmpty() || ln.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            return;
        }

        String sql = "INSERT INTO Teacher(first_name, last_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fn);
            ps.setString(2, ln);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Teacher added");
            loadTeachers();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void loadTeachers() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "First Name", "Last Name"}, 0
        );

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT teacher_id, first_name, last_name FROM Teacher");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("teacher_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                });
            }
            table.setModel(model);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
