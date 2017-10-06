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
            ResultSet rset;
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

                    rset = stmt.executeQuery(query);

                    if(rset.next()) {
                        String locationID = rset.getString("locationID");
                        String locationName = rset.getString("locationName");
                        String locationType = rset.getString("locationType");
                        int xCoord = rset.getInt("xcoord");
                        int yCoord = rset.getInt("ycoord");
                        String M = rset.getString("mapFloor");
                        System.out.println("Location Information");
                        System.out.println("Location ID: " + locationID);
                        System.out.println("Location Name: " + locationName);
                        System.out.println("Location Type: " + locationType);
                        System.out.println("X-Coordinate: " + xCoord);
                        System.out.println("Y-Coordinate: " + yCoord);
                        System.out.println("Floor: " + M);
                    }
                    System.out.println("done");
                    break;
                case 2:
                    System.out.println("Enter Edge ID:");
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    query =
                            "SELECT s.edgeID, s.locationName s_locationName, s.mapFloor s_M, f.locationName f_locationName, f.mapFloor f_M " +
                            "FROM (SELECT edgeID, locationName, mapFloor " +
                                "FROM edges INNER JOIN locations " +
                                "ON startingLocationID = locationID " +
                                "WHERE edgeID = '" + input + "') s " +
                            "INNER JOIN " +
                                "(SELECT edgeID, locationName, mapFloor " +
                                "FROM edges INNER JOIN locations " +
                                "ON endingLocationID = locationID " +
                                "WHERE edgeID = '" + input + "') f " +
                                "ON s.edgeID = f.edgeID";

                    rset = stmt.executeQuery(query);

                    String edgeID = "";
                    String sLocationName = "";
                    String sFloor = "";
                    String fLocationName = "";
                    String fFloor = "";

                    while (rset.next()){
                        edgeID  = rset.getString("edgeID");
                        sLocationName = rset.getString("s_locationName");
                        sFloor = rset.getString("s_M");
                        fLocationName = rset.getString("f_locationName");
                        fFloor = rset.getString("f_M");
                        System.out.println("Edges Information");
                        System.out.println("Edge ID: " + edgeID);
                        System.out.println("Starting Location Name: " + sLocationName);
                        System.out.println("Starting Location Floor: " + sFloor);
                        System.out.println("Ending Location Name: " + fLocationName);
                        System.out.println("Ending Location Floor: " + fFloor);
                    }
                    break;
            }

        }catch (SQLException e){

        }
    }
}
