# 🏟️ Sports Center Management System

A desktop-based JavaFX application to manage user balances, sports activities, and activity tracking using barcode scanning and webcam input. Ideal for university sports facilities or similar setups.

---

## 🚀 Features

- 👤 Add new users (with card ID)
- 💰 Add balance to existing users (with real-time balance display)
- 🏸 Add new activity areas (with pricing)
- 📲 Scan activities using webcam-based barcode scanner
- 📜 View all activity logs with timestamps and charges
- 🧾 View all registered users and their balances
- 🔐 Built-in database integration with MySQL

---

## 🛠️ Tech Stack

- **JavaFX** for GUI
- **MySQL** for backend database
- **ZXing** for barcode decoding
- **Webcam Capture API** for real-time webcam access
- **Maven** for build automation

---

## 📁 Project Structure

```
src/
└── com.mycompany.sportsmanagement
    ├── admin/                 # Admin UI components (Dashboard, AddUserPage, etc.)
    ├── common/                # Utility classes (DBUtil, BarcodeScanner)
    └── scanner/               # Activity scanner interface
```

---

## 🧑‍💻 Setup Instructions

### 📦 Prerequisites

- Java 21+
- Maven 3+
- MySQL Server
- NetBeans (recommended)

### ⚙️ Database Setup

Run the following SQL script:

```sql
CREATE DATABASE sports_center;

USE sports_center;

CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    balance DECIMAL(10,2) DEFAULT 0.00
);

CREATE TABLE activity_areas (
    activity_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    charge DECIMAL(10,2) NOT NULL
);

CREATE TABLE activity_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    activity_id INT,
    amount_deducted DECIMAL(10,2),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

✅ Sample user:
```sql
INSERT INTO users (user_id, name, balance)
VALUES ('23Z320', 'Test User', 100.00);
```

---

## 🧾 How to Run

1. Clone this repo:
   ```bash
   git clone https://github.com/your-username/sports-center-management.git
   cd sports-center-management
   ```

2. Update your MySQL credentials in `DBUtil.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/sports_center";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your-password";
   ```

3. Build and run with Maven:
   ```bash
   mvn clean install
   mvn exec:java
   ```

---

## 📸 Screenshots

> Add GUI screenshots here (AdminDashboard, Scanner, Logs, etc.)

---

## 🙋‍♂️ Author

Developed by **Dinesh T M**  
Feel free to reach out for improvements or contributions!

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
