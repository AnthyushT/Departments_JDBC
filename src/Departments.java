import java.sql.*;
import java.util.Scanner;

public class Departments {
    public static final String CREATE = "DROP TABLE Department IF EXISTS; CREATE TABLE Department (deptId INTEGER PRIMARY KEY AUTO_INCREMENT," +
            "deptName VARCHAR(30),deptStrength INTEGER,deptLocation VARCHAR(30))";

    public static final String INSERT = "INSERT INTO Department (deptName, deptStrength, deptLocation) VALUES (?, ?, ?)";
    public static final String UPDATE_NAME = "UPDATE Department SET deptName = ? WHERE deptId = ?";
    public static final String UPDATE_STRENGTH = "UPDATE Department SET deptStrength = ? WHERE deptId = ?";
    public static final String UPDATE_LOCATION = "UPDATE Department SET deptLocation = ? WHERE deptId = ?";
    public static final String SELECTQUERY = "SELECT * FROM Department";
    public static final String DELETEQUERY = "DELETE FROM Department WHERE deptId = ?";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./db", "root", "password");
            if (conn != null) {
                System.out.println("Connection is Successful.");
            }
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE);
            preparedStatement.execute();
            System.out.println("Table created");
            Scanner sc = new Scanner(System.in);
            int choice;
            while (true) {
                System.out.println("Department Table");
                System.out.println("1. Insert data into Department");
                System.out.println("2. Update data in the Department");
                System.out.println("3. Delete data in the Department");
                System.out.println("4. Display the Department Table");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine();
                int rowsModified = 0;
                switch (choice) {
                    case 1:
                        System.out.println("Enter Department Name: ");
                        String deptName = sc.nextLine();
                        System.out.println("Enter Department Strength: ");
                        int deptStrength = sc.nextInt();
                        sc.nextLine(); // Consume the newline character
                        System.out.println("Enter Department Location: ");
                        String deptLocation = sc.nextLine();

                        PreparedStatement insertStatement = conn.prepareStatement(INSERT);
                        insertStatement.setString(1, deptName);
                        insertStatement.setInt(2, deptStrength);
                        insertStatement.setString(3, deptLocation);
                        int rowsInserted = insertStatement.executeUpdate();
                        System.out.println(rowsInserted + " rows added.");
                        rowsModified = rowsInserted;
                        break;

                    case 2:
                        System.out.println("Enter Department ID for update: ");
                        int deptId = sc.nextInt();
                        sc.nextLine();
                        System.out.println("1. Update Department Name");
                        System.out.println("2. Update Department Strength");
                        System.out.println("3. Update Department Location");
                        int updateChoice = sc.nextInt();
                        sc.nextLine();
                        PreparedStatement updateStatement = null;
                        String updateField = "";
                        switch (updateChoice) {
                            case 1:
                                updateStatement = conn.prepareStatement(UPDATE_NAME);
                                System.out.println("Enter the new Department Name: ");
                                updateField = sc.nextLine();
                                break;
                            case 2:
                                updateStatement = conn.prepareStatement(UPDATE_STRENGTH);
                                System.out.println("Enter the new Department Strength: ");
                                updateField = sc.nextLine();
                                break;
                            case 3:
                                updateStatement = conn.prepareStatement(UPDATE_LOCATION);
                                System.out.println("Enter the new Department Location: ");
                                updateField = sc.nextLine();
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                        if (updateStatement != null) {
                            updateStatement.setString(1, updateField);
                            updateStatement.setInt(2, deptId);
                            int rowsUpdated = updateStatement.executeUpdate();
                            System.out.println(rowsUpdated + " rows updated.");
                            rowsModified = rowsUpdated;
                        }
                        break;
                    case 3:
                        System.out.println("Enter Department ID to delete: ");
                        int deleteId = sc.nextInt();
                        sc.nextLine();
                        PreparedStatement deleteStatement = conn.prepareStatement(DELETEQUERY);
                        deleteStatement.setInt(1, deleteId);
                        int rowsDeleted = deleteStatement.executeUpdate();
                        System.out.println(rowsDeleted + " rows deleted.");
                        rowsModified = rowsDeleted;
                        break;
                    case 4:
                        PreparedStatement selectStatement = conn.prepareStatement(SELECTQUERY);
                        ResultSet resultSet = selectStatement.executeQuery();
                        System.out.println("Department Table:");
                        if (!resultSet.isBeforeFirst()) {
                            System.out.println("No data in the table.");
                        } else {
                            System.out.format("%-15s%-15s%-15s%-15s\n", "ID", "Name", "Strength", "Location");
                            int rowCount = 0;
                            while (resultSet.next()) {
                                int id = resultSet.getInt("deptId");
                                String name = resultSet.getString("deptName");
                                int strength = resultSet.getInt("deptStrength");
                                String location = resultSet.getString("deptLocation");
                                System.out.format("%-15s%-15s%-15s%-15s\n", id, name, strength, location);
                                rowCount++;
                            }
                            System.out.println("Total rows: " + rowCount);
                        }
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        conn.close();
                        sc.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
                if (rowsModified > 0) {
                    System.out.println("Total rows modified: " + rowsModified);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
