/**
 * Created by dominic on 10/4/17.
 */
import java.io.*;
import java.sql.*;
public class p3 {

    public static void main(String [] args){
        String username = "username";
        String password = "password";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (ClassNotFoundException e){
            System.out.println("Oracle JDBC driver is missing");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver Registered!");
        Connection connection = null;


        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int option = 0;
        String input = "";

        if(args.length == 2) {
            System.out.println("1 - Report Location Information");
            System.out.println("2 - Report Edge Information");
            System.out.println("3 - Report CS Staff Information");
            System.out.println("4 - Insert New Phone Extension");
            System.out.println("5 - Exit Program");
        } else if(args.length == 3) {
            option = Integer.parseInt(args[2]);
            if (option < 1 || option > 5) {
                System.out.println("Not a valid option.");
                System.out.println("1 - Report Location Information");
                System.out.println("2 - Report Edge Information");
                System.out.println("3 - Report CS Staff Information");
                System.out.println("4 - Insert New Phone Extension");
                System.out.println("5 - Exit Program");
                return;
            }
        }else{
            System.out.println("Expected input: p3 <username> <password> <option>");
            return;
        }

        username = args[0];
        password = args[1];
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", username, password);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Connected");


        try {
            Statement stmt = connection.createStatement();
            String query = "";
            switch (option) {
                case 1:
                    System.out.println("Enter Location ID:");
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    query = "SELECT * " +
                            "FROM LOCATIONS " +
                            "WHERE locationID = '" + input + "'";
                    System.out.println(query);
                    ResultSet rset = stmt.executeQuery(query);

                    System.out.println("Query executed");
                    String locationID = "";
                    String locationName = "";
                    String locationType = "";
                    int xCoord = 0;
                    int yCoord = 0;
                    int M = 0;
                    while (rset.next()) {
                        System.out.println("entered Loop");
                        locationID = rset.getString("locationID");
                        locationName = rset.getString("locationName");
                        locationType = rset.getString("locationType");
                        xCoord = rset.getInt("xcoord");
                        yCoord = rset.getInt("ycoord");
                        System.out.println("Location Information");
                        System.out.println("Location ID: " + locationID);
                        System.out.println("Location Name: " + locationName);
                        System.out.println("Location Type: " + locationType);
                        System.out.println("X-Coordinate: " + xCoord);
                        System.out.println("Y-Coordinate: " + yCoord);
                        System.out.println("Floor: " + M);
                    }
            }

        }catch (SQLException e){

        }
    }
}
