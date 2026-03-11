package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentFrame extends JFrame {

    private JTable table;
    private JTextField txtId, txtFirstName, txtLastName;

    public StudentFrame() {
        setTitle("Student Management");
        setSize(750, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtId = new JTextField(5);
        txtId.setEditable(false);

        txtFirstName = new JTextField(12);
        txtLastName = new JTextField(12);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        JButton btnList = new JButton("Refresh");

        // Tooltips (opsiyonel ama güzel durur)
        btnAdd.setToolTipText("Insert new student");
        btnUpdate.setToolTipText("Update selected student");
        btnDelete.setToolTipText("Delete selected student");
        btnClear.setToolTipText("Clear fields");
        btnList.setToolTipText("Reload table");

        top.add(new JLabel("ID:"));
        top.add(txtId);
        top.add(new JLabel("First Name:"));
        top.add(txtFirstName);
        top.add(new JLabel("Last Name:"));
        top.add(txtLastName);
        top.add(btnAdd);
        top.add(btnUpdate);
        top.add(btnDelete);
        top.add(btnClear);
        top.add(btnList);

        // Table
        table = new JTable();
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearFields());
        btnList.addActionListener(e -> loadStudents());

        // Table row click -> fill fields
        table.getSelectionModel().addListSelectionListener(this::onRowSelected);

        // initial load
        loadStudents();
    }

    private void onRowSelected(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtId.setText(table.getValueAt(row, 0).toString());
        txtFirstName.setText(table.getValueAt(row, 1).toString());
        txtLastName.setText(table.getValueAt(row, 2).toString());
    }

    private void addStudent() {
        String fn = txtFirstName.getText().trim();
        String ln = txtLastName.getText().trim();

        if (fn.isEmpty() || ln.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill First Name and Last Name");
            return;
        }

        String sql = "INSERT INTO Student(first_name, last_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fn);
            ps.setString(2, ln);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student added");
            loadStudents();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateStudent() {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student from table first");
            return;
        }

        String fn = txtFirstName.getText().trim();
        String ln = txtLastName.getText().trim();

        if (fn.isEmpty() || ln.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill First Name and Last Name");
            return;
        }

        String sql = "UPDATE Student SET first_name=?, last_name=? WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fn);
            ps.setString(2, ln);
            ps.setInt(3, Integer.parseInt(idText));

            int affected = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, affected > 0 ? "Student updated" : "No row updated");

            loadStudents();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteStudent() {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student from table first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete student_id=" + idText + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Student WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idText));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student deleted");
            loadStudents();
            clearFields();

        } catch (Exception ex) {
            // Eğer FK yüzünden silinmiyorsa (Enrollment bağlıysa) bu mesaj çıkar
            JOptionPane.showMessageDialog(this,
                    "Delete failed. This student may be referenced in Enrollment.\n" + ex.getMessage());
        }
    }

    private void loadStudents() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "First Name", "Last Name"}, 0
        );

        String sql = "SELECT student_id, first_name, last_name FROM Student ORDER BY student_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                });
            }
            table.setModel(model);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        table.clearSelection();
    }
}
