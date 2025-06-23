package com.mycompany.sportsmanagement.admin;

import com.mycompany.sportsmanagement.common.DBUtil;
import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ViewUsersPage extends Application {

    private ObservableList<User> users = FXCollections.observableArrayList();
    private TableView<User> table = new TableView<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("User Directory");

        TableColumn<User, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, Double> balanceCol = new TableColumn<>("Balance (₹)");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        table.getColumns().addAll(idCol, nameCol, balanceCol);
        loadUsers();

        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID or Name");

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            table.setItems(
                users.filtered(u ->
                    u.getUserId().toLowerCase().contains(newText.toLowerCase()) ||
                    u.getName().toLowerCase().contains(newText.toLowerCase()))
            );
        });

        VBox layout = new VBox(10, new Label("📋 All Users"), searchField, table);
        layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, 500, 400));
        stage.show();
    }

    private void loadUsers() {
        users.clear();
        String sql = "SELECT user_id, name, balance FROM users";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setItems(users);
    }

    public static class User {
        private final String userId;
        private final String name;
        private final Double balance;

        public User(String userId, String name, Double balance) {
            this.userId = userId;
            this.name = name;
            this.balance = balance;
        }

        public String getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public Double getBalance() {
            return balance;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
