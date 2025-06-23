package com.mycompany.sportsmanagement.admin;

import com.mycompany.sportsmanagement.common.DBUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddActivityPage extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Add Activity Area");

        Label nameLabel = new Label("Activity Name:");
        TextField nameField = new TextField();

        Label chargeLabel = new Label("Charge per Scan:");
        TextField chargeField = new TextField();

        Button addButton = new Button("Add Activity");
        Label statusLabel = new Label();

        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String chargeText = chargeField.getText().trim();

            if (name.isEmpty() || chargeText.isEmpty()) {
                statusLabel.setText("❌ Please fill in all fields.");
                return;
            }

            try {
                double charge = Double.parseDouble(chargeText);
                boolean success = DBUtil.addActivity(name, charge);
                if (success) {
                    statusLabel.setText("✅ Activity added successfully!");
                } else {
                    statusLabel.setText("❌ Failed to add activity (duplicate name?).");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Invalid charge entered.");
            }
        });

        VBox layout = new VBox(10, nameLabel, nameField, chargeLabel, chargeField, addButton, statusLabel);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
