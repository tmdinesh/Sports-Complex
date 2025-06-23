package com.mycompany.sportsmanagement.admin;

import com.mycompany.sportsmanagement.common.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ActivityLogViewer extends Application {

    public static class LogEntry {
        private final String userId;
        private final String activityName;
        private final double amount;
        private final String timestamp;

        public LogEntry(String userId, String activityName, double amount, String timestamp) {
            this.userId = userId;
            this.activityName = activityName;
            this.amount = amount;
            this.timestamp = timestamp;
        }

        public String getUserId() { return userId; }
        public String getActivityName() { return activityName; }
        public double getAmount() { return amount; }
        public String getTimestamp() { return timestamp; }
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Activity Log Viewer");

        TableView<LogEntry> table = new TableView<>();
        ObservableList<LogEntry> data = FXCollections.observableArrayList();

        TableColumn<LogEntry, String> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<LogEntry, String> activityCol = new TableColumn<>("Activity");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<LogEntry, Double> amountCol = new TableColumn<>("Amount Deducted");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<LogEntry, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        table.getColumns().addAll(userCol, activityCol, amountCol, timeCol);
        table.setItems(data);

        loadLogs(data);

        VBox vbox = new VBox(10, table);
        vbox.setPadding(new Insets(20));
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }

    private void loadLogs(ObservableList<LogEntry> list) {
        String sql = """
            SELECT l.user_id, a.name AS activity_name, l.amount_deducted, l.timestamp
            FROM activity_logs l
            JOIN activity_areas a ON l.activity_id = a.activity_id
            ORDER BY l.timestamp DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String uid = rs.getString("user_id");
                String act = rs.getString("activity_name");
                double amt = rs.getDouble("amount_deducted");
                String time = rs.getString("timestamp");

                list.add(new LogEntry(uid, act, amt, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
