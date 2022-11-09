package datalayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnector {

    public static Connection getConnection(){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql-db.caprover.diplomportal.dk/" + "s215848?user=s215848&password=wi4eMewJpOEOTxullpogV");
       return con;
        }catch(Exception e){
            e.printStackTrace();

        }
    return null;
    }
}
