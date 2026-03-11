package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public static List<Object[]> findAll() throws Exception {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT course_id, course_name, grade_level, credits FROM Course ORDER BY course_id";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("grade_level"),
                        rs.getInt("credits")
                });
            }
        }
        return rows;
    }

    public static void insert(String name, String gradeLevel, int credits) throws Exception {
        String sql = "INSERT INTO Course(course_name,grade_level,credits) VALUES(?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, gradeLevel);
            ps.setInt(3, credits);
            ps.executeUpdate();
        }
    }

    public static void update(int id, String name, String gradeLevel, int credits) throws Exception {
        String sql = "UPDATE Course SET course_name=?, grade_level=?, credits=? WHERE course_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, gradeLevel);
            ps.setInt(3, credits);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws Exception {
        String sql = "DELETE FROM Course WHERE course_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
