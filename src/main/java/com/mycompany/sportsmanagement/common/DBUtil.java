package com.mycompany.sportsmanagement.common;

import java.sql.*;

public class DBUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sports_center";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dinesh2504"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ✅ Add balance to a user's account
    public static boolean addBalance(String userId, double amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Add a new sports activity
    public static boolean addActivity(String name, double charge) {
        String sql = "INSERT INTO activity_areas (name, charge) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, charge);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Get user balance (for display)
    public static Double getUserBalance(String userId) {
        String sql = "SELECT balance FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Deduct charge for activity and log the transaction
    public static boolean deductBalance(String userId, String activityName) {
        String getActivity = "SELECT activity_id, charge FROM activity_areas WHERE name = ?";
        String getBalance = "SELECT balance FROM users WHERE user_id = ?";
        String deduct = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
        String log = "INSERT INTO activity_logs (user_id, activity_id, amount_deducted) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            int activityId = -1;
            double charge = 0.0;

            // Step 1: Get activity charge
            try (PreparedStatement stmt = conn.prepareStatement(getActivity)) {
                stmt.setString(1, activityName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    activityId = rs.getInt("activity_id");
                    charge = rs.getDouble("charge");
                } else {
                    System.out.println("❌ Activity not found.");
                    return false;
                }
            }

            // Step 2: Check user balance
            double balance = 0.0;
            try (PreparedStatement stmt = conn.prepareStatement(getBalance)) {
                stmt.setString(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                } else {
                    System.out.println("❌ User not found.");
                    return false;
                }
            }

            if (balance < charge) {
                System.out.println("❌ Insufficient balance.");
                return false;
            }

            // Step 3: Deduct and log
            try (
                PreparedStatement stmt1 = conn.prepareStatement(deduct);
                PreparedStatement stmt2 = conn.prepareStatement(log)
            ) {
                stmt1.setDouble(1, charge);
                stmt1.setString(2, userId);
                stmt1.executeUpdate();

                stmt2.setString(1, userId);
                stmt2.setInt(2, activityId);
                stmt2.setDouble(3, charge);
                stmt2.executeUpdate();

                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean addUser(String userId, String name, double balance) {
    String sql = "INSERT INTO users (user_id, name, balance) VALUES (?, ?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, userId);
        stmt.setString(2, name);
        stmt.setDouble(3, balance);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
