package com.mycompany.sportsmanagement.scanner;

import com.mycompany.sportsmanagement.common.BarcodeScanner;
import com.mycompany.sportsmanagement.common.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ActivityScannerPage extends Application {

    private ComboBox<String> activityDropdown;
    private TextField userIdField;
    private Label statusLabel;
    private Label balanceLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Activity Scanner");

        Label activityLabel = new Label("Select Activity:");
        activityDropdown = new ComboBox<>();
        loadActivities();

        Label userIdLabel = new Label("Scan/Enter User ID:");
        userIdField = new TextField();

        // 📷 Scan Barcode Button
        Button scanBtn = new Button("📷 Scan Barcode");
        scanBtn.setOnAction(e -> {
            String scanned = BarcodeScanner.scanBarcode();
            if (scanned != null) {
                userIdField.setText(scanned);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Scan Failed");
                alert.setHeaderText(null);
                alert.setContentText("No barcode detected. Try again.");
                alert.showAndWait();
            }
        });

        HBox scanBox = new HBox(10, userIdField, scanBtn);

        Button checkBalanceBtn = new Button("Check Balance");
        balanceLabel = new Label("");

        checkBalanceBtn.setOnAction(e -> {
            String userId = userIdField.getText().trim();
            Double balance = DBUtil.getUserBalance(userId);
            if (balance != null) {
                balanceLabel.setText("Current Balance: ₹" + balance);
            } else {
                balanceLabel.setText("❌ User not found.");
            }
        });

        Button scanButton = new Button("Scan & Deduct");
        statusLabel = new Label();

        scanButton.setOnAction(e -> handleScan());

        VBox vbox = new VBox(10,
                activityLabel, activityDropdown,
                userIdLabel, scanBox,
                checkBalanceBtn, balanceLabel,
                scanButton, statusLabel);

        vbox.setPadding(new Insets(20));
        stage.setScene(new Scene(vbox, 450, 380));
        stage.show();
    }

    private void loadActivities() {
        ObservableList<String> activities = FXCollections.observableArrayList();
        String sql = "SELECT name FROM activity_areas";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                activities.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        activityDropdown.setItems(activities);
    }

    private void handleScan() {
        String userId = userIdField.getText().trim();
        String activity = activityDropdown.getValue();

        if (userId.isEmpty() || activity == null) {
            statusLabel.setText("❌ Please enter User ID and select an activity.");
            return;
        }

        boolean success = DBUtil.deductBalance(userId, activity);
        if (success) {
            Double updated = DBUtil.getUserBalance(userId);
            statusLabel.setText("✅ Access granted. Balance deducted.");
            balanceLabel.setText("Updated Balance: ₹" + updated);
        } else {
            statusLabel.setText("❌ Access denied. Check balance or user ID.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
