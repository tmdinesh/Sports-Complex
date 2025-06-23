package com.mycompany.sportsmanagement.admin;

import com.mycompany.sportsmanagement.common.BarcodeScanner;
import com.mycompany.sportsmanagement.common.DBUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddBalancePage extends Application {

    private Label balanceLabel = new Label("");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Balance - Sports Center");

        Label idLabel = new Label("Scan/Enter User ID:");
        TextField idField = new TextField();

        // 📷 Scan Barcode Button
        Button scanBtn = new Button("📷 Scan Barcode");
        scanBtn.setOnAction(e -> {
            String scanned = BarcodeScanner.scanBarcode();
            if (scanned != null) {
                idField.setText(scanned);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Scan Failed");
                alert.setHeaderText(null);
                alert.setContentText("No barcode detected. Try again.");
                alert.showAndWait();
            }
        });

        HBox scanBox = new HBox(10, idField, scanBtn);

        Button fetchBalanceBtn = new Button("Check Current Balance");
        fetchBalanceBtn.setOnAction(e -> {
            String userId = idField.getText().trim();
            Double balance = DBUtil.getUserBalance(userId);
            if (balance != null) {
                balanceLabel.setText("Current Balance: ₹" + balance);
            } else {
                balanceLabel.setText("❌ User not found.");
            }
        });

        Label amountLabel = new Label("Enter Amount to Add:");
        TextField amountField = new TextField();

        Button addButton = new Button("Add Balance");
        Label statusLabel = new Label();

        addButton.setOnAction(e -> {
            String userId = idField.getText().trim();
            String amountText = amountField.getText().trim();

            if (userId.isEmpty() || amountText.isEmpty()) {
                statusLabel.setText("❌ Please fill all fields.");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                boolean success = DBUtil.addBalance(userId, amount);

                if (success) {
                    statusLabel.setText("✅ Balance added successfully!");
                    Double updated = DBUtil.getUserBalance(userId);
                    balanceLabel.setText("Updated Balance: ₹" + updated);
                } else {
                    statusLabel.setText("❌ Failed to add balance. Check User ID.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Invalid amount entered.");
            }
        });

        VBox vbox = new VBox(10,
                idLabel, scanBox,
                fetchBalanceBtn, balanceLabel,
                amountLabel, amountField,
                addButton, statusLabel);

        vbox.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(vbox, 450, 380));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
