package com.mycompany.sportsmanagement.admin;

import com.mycompany.sportsmanagement.common.BarcodeScanner;
import com.mycompany.sportsmanagement.common.DBUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddUserPage extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Add New User");

        Label idLabel = new Label("User ID:");
        TextField idField = new TextField();

        // 📷 Barcode Scan Button
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

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label balanceLabel = new Label("Initial Balance:");
        TextField balanceField = new TextField();

        Button addButton = new Button("Add User");
        Label statusLabel = new Label();

        addButton.setOnAction(e -> {
            String userId = idField.getText().trim();
            String name = nameField.getText().trim();
            String balanceText = balanceField.getText().trim();

            if (userId.isEmpty() || name.isEmpty() || balanceText.isEmpty()) {
                statusLabel.setText("❌ Please fill all fields.");
                return;
            }

            try {
                double balance = Double.parseDouble(balanceText);
                boolean success = DBUtil.addUser(userId, name, balance);
                if (success) {
                    statusLabel.setText("✅ User added successfully!");
                    idField.clear();
                    nameField.clear();
                    balanceField.clear();
                } else {
                    statusLabel.setText("❌ Failed to add user. User ID might already exist.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Invalid balance amount.");
            }
        });

        VBox vbox = new VBox(10,
                idLabel, scanBox,
                nameLabel, nameField,
                balanceLabel, balanceField,
                addButton, statusLabel);

        vbox.setPadding(new Insets(20));
        stage.setScene(new Scene(vbox, 450, 350));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
