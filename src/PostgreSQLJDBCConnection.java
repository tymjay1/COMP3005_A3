import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Scanner;

public class PostgreSQLJDBCConnection {

    // Connecting with the PostgreSQL database
    public static Connection connect() {
        // JDBC & Database credentials
        String url = "jdbc:postgresql://<HOST>:<PORT>/<DATABASE_NAME>";
        String user = "<USERNAME>";
        String password = "<PASSWORD>";

        Connection conn = null;

        try { 
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Failed to establish connection.");
            }
        }
            catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    // Get all the students in the Students table
    public static void getAllStudents(Connection conn) {
        try {
            System.out.println("Getting all students...");

            String query = "SELECT * FROM Students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                int id = rs.getInt("student_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String date = rs.getDate("enrollment_date").toString();
                System.out.println(id + " | " + first_name + " " + last_name + "\t" + email + "\t" + date);
            }
            System.out.println();
            rs.close();
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new student with the given information
    public static void addStudent(String first_name, String last_name, String new_email, LocalDate enrollment_date, Connection conn) {
        try {
            System.out.println("Adding new student...");

            String query = "INSERT INTO Students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, new_email);
            pstmt.setDate(4, java.sql.Date.valueOf(enrollment_date));
            pstmt.executeUpdate();

            System.out.println("Student " + first_name + " " + last_name + " added successfully.");

            System.out.println();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Update the student email address using the student_id
    public static void updateStudentEmail(int student_id, String new_email, Connection conn) {
        try {
            System.out.println("Updating student information...");

            String query = "UPDATE Students SET email = ? WHERE student_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            int rows = pstmt.executeUpdate();

            if(rows > 0) {
                System.out.println("Updated information for student " + student_id + ".");
            }else {
                System.out.println("Failed to update student " + student_id + " information.");
            }

            System.out.println();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a student using the student_id
    public static void deleteStudent(int student_id, Connection conn) {
        try {
            System.out.println("Deleting student...");

            String query = "DELETE FROM Students WHERE student_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, student_id);
            int rows = pstmt.executeUpdate();

            if(rows > 0) {
                System.out.println("Student " + student_id + " deleted successfully.");
            }else {
                System.out.println("Failed to delete student " + student_id + ".");
            }

            System.out.println();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Attempting to connect with PostgreSQL...");

        Connection conn = connect();
        if(conn == null) {
            return;
        }
        System.out.println();

        // getAllStudents(conn);

        // addStudent("Quang", "Lac", "quang.lac@gmail.com", LocalDate.of(2023, 9, 5), conn);
        // getAllStudents(conn);

        // updateStudentEmail(12, "quang.lac1@gmail.com", conn);
        // getAllStudents(conn);

        // deleteStudent(12, conn);
        // getAllStudents(conn);

        try {
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}