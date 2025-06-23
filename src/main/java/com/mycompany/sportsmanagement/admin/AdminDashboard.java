package com.mycompany.sportsmanagement.admin;

import com.mycompany.sportsmanagement.admin.*;
import com.mycompany.sportsmanagement.scanner.ActivityScannerPage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Admin Dashboard - Sports Center");

        Label title = new Label("🏟️ Admin Panel");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addUserBtn = new Button("👤 Add New User");
        Button addBalanceBtn = new Button("➕ Add Balance");
        Button addActivityBtn = new Button("🏸 Add Activity Area");
        Button scanActivityBtn = new Button("📲 Activity Scanner");
        Button viewUsersBtn = new Button("🧾 View All Users");
        Button viewLogsBtn = new Button("📜 View Activity Logs");

        // Set button style
        for (Button btn : new Button[]{addUserBtn, addBalanceBtn, addActivityBtn, scanActivityBtn, viewUsersBtn, viewLogsBtn}) {
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        // Launch pages
        addUserBtn.setOnAction(e -> launchPage(AddUserPage.class));
        addBalanceBtn.setOnAction(e -> launchPage(AddBalancePage.class));
        addActivityBtn.setOnAction(e -> launchPage(AddActivityPage.class));
        scanActivityBtn.setOnAction(e -> launchPage(ActivityScannerPage.class));
        viewUsersBtn.setOnAction(e -> launchPage(ViewUsersPage.class));

        viewLogsBtn.setOnAction(e -> launchPage(ActivityLogViewer.class));

        VBox layout = new VBox(15, title, addUserBtn, addBalanceBtn, addActivityBtn, scanActivityBtn, viewUsersBtn, viewLogsBtn);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setPrefWidth(400);

        stage.setScene(new Scene(layout));
        stage.show();
    }

    private void launchPage(Class<? extends Application> pageClass) {
        try {
            Application page = pageClass.getDeclaredConstructor().newInstance();
            page.start(new Stage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
