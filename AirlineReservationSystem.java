import org.sqlite.JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AirlineReservationSystem {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:airline.db");
            initializeDatabase(connection);

            while (true) {
                System.out.println("Airline Reservation System");
                System.out.println("1. Reserve a seat");
                System.out.println("2. View reservations");
                System.out.println("3. Exit");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        reserveSeat(connection);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        connection.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS reservations (" +
                "id INTEGER PRIMARY KEY," +
                "passenger_name TEXT," +
                "flight_number TEXT," +
                "departure_date TEXT," +
                "seat_number TEXT" +
                ");";

        PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL);
        createTableStatement.execute();
    }

    private static void reserveSeat(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter passenger name: ");
        String passengerName = scanner.nextLine();

        System.out.println("Enter flight number: ");
        String flightNumber = scanner.nextLine();

        System.out.println("Enter departure date: ");
        String departureDate = scanner.nextLine();

        System.out.println("Enter seat number: ");
        String seatNumber = scanner.nextLine();

        String insertSQL = "INSERT INTO reservations (passenger_name, flight_number, departure_date, seat_number) VALUES (?, ?, ?, ?);";
        PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
        insertStatement.setString(1, passengerName);
        insertStatement.setString(2, flightNumber);
        insertStatement.setString(3, departureDate);
        insertStatement.setString(4, seatNumber);
        insertStatement.executeUpdate();

        System.out.println("Seat reserved successfully!");
    }

    private static void viewReservations(Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM reservations;";
        PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
        ResultSet resultSet = selectStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Reservation ID: " + resultSet.getInt("id"));
            System.out.println("Passenger Name: " + resultSet.getString("passenger_name"));
            System.out.println("Flight Number: " + resultSet.getString("flight_number"));
            System.out.println("Departure Date: " + resultSet.getString("departure_date"));
            System.out.println("Seat Number: " + resultSet.getString("seat_number"));
            System.out.println();
        }
    }
}
