/**
 * Created by dominic on 10/4/17.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

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
                    break;
                case 2:
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("Enter Edge ID:");
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
                    rset = stmt.executeQuery(query);

                    Set<String> titles = new TreeSet<String>();
                    Set<String> pExts = new TreeSet<String>();

                    System.out.println("CS Staff Information");
                    String aName = "Account Name: ";
                    String fName = "First Name: ";
                    String lName = "Last Name: ";
                    String title = "";
                    String pExt = "";
                    String tempTitle;
                    String tempExt;

                    if(rset.next()) {
                        System.out.println(aName + rset.getString("accountName"));
                        System.out.println(fName + rset.getString("firstName"));
                        System.out.println(lName + rset.getString("lastName"));
                        System.out.println("Office ID: " + rset.getInt("officeID"));
                        tempTitle = rset.getString("titleName");
                        tempExt = Integer.toString(rset.getInt("phoneExt"));
                        titles.add(tempTitle);
                        pExts.add(tempExt);
                        title = tempTitle;
                        pExt = tempExt;
                    }

                    while(rset.next()) {
                        tempTitle = rset.getString("titleName");
                        tempExt = Integer.toString(rset.getInt("phoneExt"));
                        titles.add(tempTitle);
                        pExts.add(tempExt);
                    }
                    tempTitle = title;
                    tempExt = pExt;

                    for (String t : titles) {
                        if(!t.equals(tempTitle))
                            title += ", " + t;
                    }

                    for (String e : pExts) {
                        if(!e.equals(tempExt))
                            pExt += ", " + e;
                    }
                    if(!title.equals(""))
                        System.out.println("Title: " + title);
                    if(!pExt.equals(""))
                        System.out.println("Phone Ext: " + pExt);
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
                    rset = stmt.executeQuery(query);
                    break;
                case 5:
                    System.out.println("Exiting program");
                    return;
            }


        }catch (SQLException e){
            e.printStackTrace();
            return;
        }
    }
}
