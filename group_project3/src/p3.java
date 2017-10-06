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
                    break;
                case 3:
                    System.out.println("Enter CS Staff Account Name:");
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    query = "SELECT X.accountName, X.firstName, X.lastName, X.officeID, X.titleName, P.phoneExt " +
                            "FROM" +
                            " (SELECT F.accountName, F.firstName, F.lastName, F.officeID, S.titleName " + "FROM" +
                            " (SELECT C.accountName, C.firstName, C.lastName, C.officeID, T.acronym " +
                            "FROM (SELECT * " +
                            "FROM CSSTAFF " +
                            "WHERE accountName = '" + input + "') C" + ", CSSTAFFTITLES T" +
                            " WHERE C.accountName = T.accountName) F, TITLES S" +
                            " WHERE F.acronym = S.acronym) X, PhoneExtensions P" +
                            " WHERE X.accountName = P.accountName";
                    System.out.println(query);
                    ResultSet rset3 = stmt.executeQuery(query);

                    System.out.println("CS Staff Information");
                    String aName = "Account Name: ";
                    String fName = "First Name: ";
                    String lName = "Last Name: ";
                    int oID ;
                    String title = "Title: ";
                    int pExt ;// "Phone Ext: ";

                    while(rset3.next()) {
                        System.out.println(aName + rset3.getString("accountName"));
                        System.out.println(fName + rset3.getString("firstName"));
                        System.out.println(lName + rset3.getString("lastName"));
                        System.out.println("Office ID: " + rset3.getInt("officeID"));
                        System.out.println(title + rset3.getString("titleName"));
                        System.out.println("Phone Ext: " + rset3.getInt("phoneExt"));
                    }
                    break;
                case 4:
                    System.out.println("Enter CS Staff Acount Name: ");
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    String acName = input;
                    System.out.println("Enter New Phone Extension: ");
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    String phExt = input;
                    int phExten = Integer.parseInt(phExt);

                    query = "INSERT INTO PhoneExtensions (accountName, phoneExt) " +
                            "VALUES ( '" + acName + "', " + phExten + ")";
                    System.out.println(query);
                    ResultSet rset4 = stmt.executeQuery(query);
                    break;
            }


        }catch (SQLException e){

        }
    }
}
