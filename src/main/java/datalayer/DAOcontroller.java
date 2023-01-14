package datalayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exceptionhandler.DataNotFoundException;

import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.LoginData;
import model.PatientData;
import model.RequestData;

public class DAOcontroller {

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
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
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
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
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
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
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
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
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
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
                aftaledata.setAftaleid(resultSet.getInt("id"));

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
                aftaledata.setAftaleid(resultSet.getInt("id"));

                System.out.println("Aftale tilføjet til objekt (fetchAftaleDB)");

            }

            return Response.status(Response.Status.CREATED).entity(aftaledata).build();


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
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
                aftaledata.setAftaleid((resultSet.getInt("id")));
                aftaledata.setWorkerusername((resultSet.getString("workerusername")));

                aftaler.add(aftaledata);

                System.out.println("Aftale tilføjet til objekt");
            }

            return Response.status(Response.Status.CREATED).entity(aftaler).build();


        } catch (Exception e) {
            System.out.println("Intet CPR nr fundet.");

            throw new DataNotFoundException("Intet CPR nr fundet.");
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }

    }
    public void saveConsultationReqToDB(RequestData requestdata){
        this.requestdata = requestdata;

        try {

            //Insert request consultation
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO `s112786`.`reqconsultation` (startTime, timeOfDay,note,workerusername,cpr) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, requestdata.getDatetime());
            preparedStatement.setString(2, requestdata.getTimeOfDay());
            preparedStatement.setString(3, requestdata.getNote());
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
            System.out.println("");

            throw new DataNotFoundException("Kunne ikke gemme anmodninger.");

        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }

    }
    public void saveAftaleToDB(AftaleData aftaledata){
        this.aftaledata = aftaledata;

        try {

            //Insert request consultation
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO `s112786`.`consultation` (startTime,note, duration, workerusername,cpr) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, aftaledata.getDatetime());
            preparedStatement.setString(2, aftaledata.getNote());
            preparedStatement.setInt(3, aftaledata.getDuration());
            preparedStatement.setString(4, aftaledata.getWorkerusername());
            preparedStatement.setString(5, aftaledata.getCpr());

            preparedStatement.executeUpdate();

            // Delete request
            if(aftaledata.getAftaleid() > 0) {
                preparedStatement = con.prepareStatement("DELETE FROM `s112786`.`reqconsultation` where id = ?");
                preparedStatement.setInt(1, aftaledata.getAftaleid());


                preparedStatement.executeUpdate();
            }
        }catch(Exception e){
            System.out.println("Der skete en fejl ved forsøg på at gemme aftale.");

            throw new DataNotFoundException("Der skete en fejl ved forsøg på at gemme aftale.");

        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }

    }
    public Response fetchFlagDataToWorker(String workerusername){

        ArrayList<String> pendingPatient = new ArrayList<String>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select cpr from `s112786`.`reqconsultation` where workerusername = ?");
            preparedStatement.setString(1, workerusername);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                pendingPatient.add(resultSet.getString("cpr"));

                System.out.println("CPR flag tilføjet til objekt");

            }

            return Response.status(Response.Status.CREATED).entity(pendingPatient).build();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }
        return null;
    }
    public Response fetchRequestListToWorker(String workerusername){

        List<RequestData> requestlist = new ArrayList<RequestData>();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from `s112786`.`reqconsultation` where workerusername = ?");
            preparedStatement.setString(1, workerusername);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RequestData requestdata = new RequestData();

                requestdata.setDatetime(resultSet.getString("startTime"));
                requestdata.setTimeOfDay(resultSet.getString("timeOfDay"));
                requestdata.setNote(resultSet.getString("note"));
                requestdata.setCpr(resultSet.getString("cpr"));
                requestdata.setAftaleid(resultSet.getInt("id"));

                requestlist.add(requestdata);

                System.out.println("Request tilføjet til objekt");

            }

            return Response.status(Response.Status.CREATED).entity(requestlist).build();


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }
        System.out.println("Ingen anmodninger til worker.");
        throw new DataNotFoundException("Ingen anmodninger til worker.");
    }
    public Response fetchRequestDB(int aftaleid) {

        RequestData requestdata = new RequestData();

        try {
            Connection con = sqlcon.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("select * from `s112786`.`reqconsultation` where id = ?");
            preparedStatement.setInt(1, aftaleid);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                requestdata.setDatetime(resultSet.getString("startTime"));
                requestdata.setTimeOfDay(resultSet.getString("timeOfDay"));
                requestdata.setNote(resultSet.getString("note"));
                requestdata.setWorkerusername(resultSet.getString("workerusername"));
                requestdata.setAftaleid(resultSet.getInt("id"));
                requestdata.setCpr(resultSet.getString("cpr"));

                System.out.println("request tilføjet til model (fetchRequestDB)");

            }

            return Response.status(Response.Status.CREATED).entity(requestdata).build();


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }
        System.out.println("Ingen aftale med dette ID fundet.");

        throw new DataNotFoundException("Intet aftale med dette ID fundet.");
    }

    public Response deleteAftaleDB(int aftaleid){

        try {

            Connection con = sqlcon.getConnection();

            // Delete aftale
            preparedStatement = con.prepareStatement("DELETE FROM `s112786`.`consultation` where id = ?");
            preparedStatement.setInt(1, aftaleid);

            preparedStatement.executeUpdate();

            return Response.status(Response.Status.CREATED).build();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Der skete en fejl ved forsøg på at slette aftale.");

            throw new DataNotFoundException("Der skete en fejl ved forsøg på at slette aftale.");
        }finally {
            try { resultSet.close(); } catch (Exception e) { /* Ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* Ignored */ }
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }
    }


}


