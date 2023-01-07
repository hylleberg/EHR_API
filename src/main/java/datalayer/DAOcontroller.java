package datalayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.LoginData;
import model.PatientData;

public class DAOcontroller {

    private SqlConnector sqlcon = new SqlConnector();
    private LoginData logindata;
    private PatientData patientdata;

    private AftaleData aftaledata;

    public String getKeyDB(String username){
        try {
            //Forbidenlse til database
            Connection con = sqlcon.getConnection();
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("select * from logindata where brugernavn = ?");
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("key");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
    public void setKeyDB(String encodedKey, String username){


        try {
            //Forbidenlse til database
            Connection con = sqlcon.getConnection();
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE `jdbc-test`.`logindata` SET `key` = ? WHERE (`brugernavn` = ?)");
            preparedStatement.setString(1, encodedKey);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public boolean fetchLoginDataDB(LoginData logindata) {

        this.logindata = logindata;

        try {
            //Forbidenlse til database
            Connection con = sqlcon.getConnection();
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("select * from logindata where brugernavn = ? and password = ?");
            preparedStatement.setString(1, logindata.getUsername());
            preparedStatement.setString(2, logindata.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                return true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public Response fetchPatientDataDB(PatientData patientdata) {

        this.patientdata = patientdata;

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from patient where cpr = ?");
            preparedStatement.setString(1, patientdata.getCpr());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                patientdata.setFornavn(resultSet.getString("fornavn"));
                patientdata.setEfternavn(resultSet.getString("efternavn"));
                patientdata.setAdresse(resultSet.getString("adresse"));

                System.out.println("Patient data hentet");
                return Response.status(Response.Status.CREATED).entity(patientdata).build();
            } else {


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Intet CPR nr fundet.");

        return Response.status(Response.Status.BAD_REQUEST).entity("Forkert CPR").build();
    }

    public Response fetchAftaleDataDB(PatientData patientdata) {
        this.patientdata = patientdata;


        List<AftaleData> aftaler = new ArrayList<AftaleData>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from aftaler where cpr = ?");
            preparedStatement.setString(1, patientdata.getCpr());

            ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    AftaleData aftaledata = new AftaleData();

                    aftaledata.setDatetime(resultSet.getTimestamp("startdato"));
                    aftaledata.setDuration(resultSet.getInt("varighed"));
                    aftaledata.setNote(resultSet.getString("notat"));

                    aftaler.add(aftaledata);

                    System.out.println("Aftale tilføjet til objekt");

                }

                return Response.status(Response.Status.CREATED).entity(aftaler).build();


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Intet CPR nr fundet.");

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}


