package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    public static List<Object[]> findAll() throws Exception {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT teacher_id, first_name, last_name, email, branch, department, phone FROM Teacher ORDER BY teacher_id";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("teacher_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("branch"),
                        rs.getString("department"),
                        rs.getString("phone")
                });
            }
        }
        return rows;
    }

    public static void insert(String first, String last, String email, String branch, String dept, String phone) throws Exception {
        String sql = "INSERT INTO Teacher(first_name,last_name,email,branch,department,phone) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, email);
            ps.setString(4, branch);
            ps.setString(5, dept);
            ps.setString(6, phone);
            ps.executeUpdate();
        }
    }

    public static void update(int id, String first, String last, String email, String branch, String dept, String phone) throws Exception {
        String sql = "UPDATE Teacher SET first_name=?, last_name=?, email=?, branch=?, department=?, phone=? WHERE teacher_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, email);
            ps.setString(4, branch);
            ps.setString(5, dept);
            ps.setString(6, phone);
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws Exception {
        String sql = "DELETE FROM Teacher WHERE teacher_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
