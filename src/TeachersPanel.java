package ui.panels;

import db.CourseDAO;
import ui.AppTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursesPanel extends JPanel {

    private final JTable table = new JTable();
    private final DefaultTableModel model =
            new DefaultTableModel(new String[]{"ID","Course Name","Grade Level","Credits"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtLevel = new JTextField();
    private final JTextField txtCredits = new JTextField();

    public CoursesPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(15,15,18));
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JPanel card = AppTheme.card();
        card.setLayout(new BorderLayout(12,12));
        card.setBackground(new Color(18,18,22));
        card.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));

        JLabel title = new JLabel("Courses • CRUD");
        title.setForeground(new Color(235,235,235));
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnRefresh = AppTheme.primaryButton("Refresh");
        btnRefresh.addActionListener(e -> load());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(btnRefresh, BorderLayout.EAST);

        table.setModel(model);
        UIUtils.styleTableDark(table);
        table.setFillsViewportHeight(true);

        JScrollPane sp = UIUtils.darkScroll(table);
        JPanel right = buildForm();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, right);
        split.setDividerLocation(900);
        split.setResizeWeight(0.70);
        split.setBorder(null);

        card.add(top, BorderLayout.NORTH);
        card.add(split, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> fillFromSelected());
        load();
    }

    private JPanel buildForm() {
        JPanel formCard = AppTheme.card();
        formCard.setBackground(new Color(18,18,22));
        formCard.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        formCard.setLayout(new GridBagLayout());

        txtId.setEditable(false);

        styleField(txtId);
        styleField(txtName);
        styleField(txtLevel);
        styleField(txtCredits);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.weightx = 1;

        formCard.add(label("Selected ID"), c); c.gridy++;
        formCard.add(txtId, c);

        c.gridy++;
        formCard.add(label("Course Name"), c); c.gridy++;
        formCard.add(txtName, c);

        c.gridy++;
        formCard.add(label("Grade Level"), c); c.gridy++;
        formCard.add(txtLevel, c);

        c.gridy++;
        formCard.add(label("Credits"), c); c.gridy++;
        formCard.add(txtCredits, c);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 10));
        buttons.setOpaque(false);

        JButton btnAdd = AppTheme.primaryButton("Add");
        JButton btnUpdate = AppTheme.ghostButton("Update");
        JButton btnDelete = AppTheme.ghostButton("Delete");

        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());

        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);

        c.gridy++;
        formCard.add(buttons, c);

        JButton btnClear = AppTheme.ghostButton("Clear Form");
        btnClear.addActionListener(e -> clearForm());

        c.gridy++;
        formCard.add(btnClear, c);

        return formCard;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(new Color(200,200,200));
        return l;
    }

    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(240, 34));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void load() {
        model.setRowCount(0);
        try {
            for (Object[] r : CourseDAO.findAll()) model.addRow(r);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void fillFromSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtId.setText(String.valueOf(model.getValueAt(row, 0)));
        txtName.setText(String.valueOf(model.getValueAt(row, 1)));
        txtLevel.setText(String.valueOf(model.getValueAt(row, 2)));
        txtCredits.setText(String.valueOf(model.getValueAt(row, 3)));
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtLevel.setText("");
        txtCredits.setText("");
        table.clearSelection();
    }

    private void addCourse() {
        try {
            int credits = Integer.parseInt(txtCredits.getText().trim());
            CourseDAO.insert(txtName.getText().trim(), txtLevel.getText().trim(), credits);
            load();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateCourse() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a course from table.");
            return;
        }
        try {
            int credits = Integer.parseInt(txtCredits.getText().trim());
            CourseDAO.update(Integer.parseInt(txtId.getText().trim()), txtName.getText().trim(), txtLevel.getText().trim(), credits);
            load();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteCourse() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a course from table.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Delete selected course?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        try {
            CourseDAO.delete(Integer.parseInt(txtId.getText().trim()));
            load();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
