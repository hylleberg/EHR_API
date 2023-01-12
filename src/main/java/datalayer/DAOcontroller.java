package datalayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exceptionhandler.DataNotFoundException;

import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.LoginData;
import model.PatientData;
import model.RequestData;

public class DAOcontroller {

    private SqlConnector sqlcon = new SqlConnector();
    private LoginData logindata;
    private PatientData patientdata;

    private AftaleData aftaledata;

    private String cpr;

    private RequestData requestdata;

    public String getKeyDB(String username) {
        try {
            //Forbidenlse til database
            Connection con = sqlcon.getConnection();
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("select * from user where username = ?");
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

    public void setKeyDB(String encodedKey, String username) {


        try {
            //Forbidenlse til database
            Connection con = sqlcon.getConnection();
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE `s112786`.`user` SET `key` = ? WHERE (`username` = ?)");
            preparedStatement.setString(1, encodedKey);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String fetchLoginDataDB(LoginData logindata) {

        this.logindata = logindata;

        try {
            //Forbidenlse til database
            Connection con = sqlcon.getConnection();
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("select * from user where username = ? and password = ?");
            preparedStatement.setString(1, logindata.getUsername());
            preparedStatement.setString(2, logindata.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                return resultSet.getString("role");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    public Response fetchPatientDataDB(PatientData patientdata) {

        this.patientdata = patientdata;

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from patient where cpr = ?");
            preparedStatement.setString(1, patientdata.getCpr());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                patientdata.setFirstname(resultSet.getString("firstname"));
                patientdata.setLastname(resultSet.getString("lastname"));
                patientdata.setAddress(resultSet.getString("address"));
                patientdata.setCity(resultSet.getString("city"));

                System.out.println("Patient data hentet");
                return Response.status(Response.Status.CREATED).entity(patientdata).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Intet CPR nr fundet.");

        // return Response.status(Response.Status.BAD_REQUEST).entity("Forkert CPR").build();
        throw new DataNotFoundException("Kunne ikke finde CPR nr.");
    }

    public Response fetchAftaleDataPatientDB(String cpr) {

        List<AftaleData> aftaler = new ArrayList<AftaleData>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from consultation where cpr = ?");
            preparedStatement.setString(1, cpr);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AftaleData aftaledata = new AftaleData();
                aftaledata.setWorkerusername(resultSet.getString("workerusername"));
                aftaledata.setDatetime(resultSet.getString("starttime"));
                aftaledata.setDuration(resultSet.getInt("duration"));
                aftaledata.setNote(resultSet.getString("note"));
                aftaledata.setId(resultSet.getInt("id"));

                aftaler.add(aftaledata);

                System.out.println("Aftale tilføjet til objekt");

            }

            return Response.status(Response.Status.CREATED).entity(aftaler).build();


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Intet CPR nr fundet.");

        throw new DataNotFoundException("Intet CPR nr fundet.");
    }

    public Response fetchAftaleDB(int aftaleid) {

        AftaleData aftaledata = new AftaleData();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from consultation where id = ?");
            preparedStatement.setInt(1, aftaleid);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                aftaledata.setWorkerusername(resultSet.getString("workerusername"));
                aftaledata.setCpr(resultSet.getString("cpr"));
                aftaledata.setDatetime(resultSet.getString("starttime"));
                aftaledata.setDuration(resultSet.getInt("duration"));
                aftaledata.setNote(resultSet.getString("note"));
                aftaledata.setId(resultSet.getInt("id"));

                System.out.println("Aftale tilføjet til objekt (fetchAftaleDB)");

            }

            return Response.status(Response.Status.CREATED).entity(aftaledata).build();


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Intet aftale med dette ID fundet.");

        throw new DataNotFoundException("Intet aftale med dette ID fundet.");
    }

    public Response fetchAftaleDataWorkerDB(String workerusername) {

        List<AftaleData> aftaler = new ArrayList<AftaleData>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from consultation where workerusername = ?");
            preparedStatement.setString(1, workerusername);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AftaleData aftaledata = new AftaleData();
                aftaledata.setCpr(resultSet.getString("cpr"));
                aftaledata.setDatetime(resultSet.getString("starttime"));
                aftaledata.setDuration(resultSet.getInt("duration"));
                aftaledata.setNote(resultSet.getString("note"));

                aftaler.add(aftaledata);

                System.out.println("Aftale tilføjet til objekt");

            }

            return Response.status(Response.Status.CREATED).entity(aftaler).build();


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Intet CPR nr fundet.");

        throw new DataNotFoundException("Intet CPR nr fundet.");
    }
    public void saveConsultationReqToDB(RequestData requestdata){
        this.requestdata = requestdata;

        try {

            //Insert request consultation
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO `s112786`.`reqConsultation` (startTime, timeOfDay,comment,workerusername,cpr) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, requestdata.getDatetime());
            preparedStatement.setString(2, requestdata.getTimeOfDay());
            preparedStatement.setString(3, requestdata.getComment());
            preparedStatement.setString(4, requestdata.getWorkerusername());
            preparedStatement.setString(5, requestdata.getCpr());

            preparedStatement.executeUpdate();

            // Set flag "true"
            preparedStatement = con.prepareStatement("INSERT INTO `s112786`.`requestflags` (cpr,flag,workerusername) VALUES (?, ?, ?)");
            preparedStatement.setString(1, requestdata.getCpr());
            preparedStatement.setBoolean(2, true);
            preparedStatement.setString(3, requestdata.getWorkerusername());

            preparedStatement.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();

        }

    }

    public Response fetchFlagDataToWorker(String workerusername){

        ArrayList<String> pendingPatient = new ArrayList<String>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select cpr from `s112786`.`reqConsultation` where workerusername = ?");
            preparedStatement.setString(1, workerusername);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                pendingPatient.add(resultSet.getString("cpr"));

                System.out.println("CPR flag tilføjet til objekt");

            }

            return Response.status(Response.Status.CREATED).entity(pendingPatient).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Response fetchRequestDataToWorker(String workerusername, String cpr){

        List<RequestData> requestlist = new ArrayList<RequestData>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from `s112786`.`reqConsultation` where workerusername = ? AND cpr = ?");
            preparedStatement.setString(1, workerusername);
            preparedStatement.setString(2, cpr);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RequestData requestdata = new RequestData();

                requestdata.setDatetime(resultSet.getString("startTime"));
                requestdata.setTimeOfDay(resultSet.getString("timeOfDay"));
                requestdata.setComment(resultSet.getString("comment"));
                requestdata.setCpr(resultSet.getString("cpr"));

                requestlist.add(requestdata);

                System.out.println("Request tilføjet til objekt");

            }

            return Response.status(Response.Status.CREATED).entity(requestlist).build();


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Ingen anmodninger til worker.");
        throw new DataNotFoundException("Ingen anmodninger til worker.");
    }

}


