package datalayer;

import java.sql.*;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.LoginData;
import model.PatientData;
import model.PayloadPatient;

public class DAOcontroller {


    private LoginData logindata;
    private PatientData patientdata;
    public Response fetchLoginDataDB(LoginData logindata){

        this.logindata = logindata;

        try{
            //Forbidenlse til database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-test", "root", "T3stT3st123!");
            //Forbered query statement
            PreparedStatement preparedStatement = con.prepareStatement("select * from logindata where brugernavn = ? and password = ?");
            preparedStatement.setString(1,logindata.getUsername());
            preparedStatement.setString(2, logindata.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                System.out.println("Kombination korrekt");
                return Response.status(Response.Status.CREATED).entity("Kombinatioon korrekt!").build();

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Brugernavn/password kombination forkert.");
        return Response.status(Response.Status.BAD_REQUEST).entity("Forkert kombination").build();

    }

    public Response fetchPatientDataDB(PatientData patientdata){

        this.patientdata = patientdata;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-test", "root", "T3stT3st123!");
            PreparedStatement preparedStatement = con.prepareStatement("select * from patient where cpr = ?");
            preparedStatement.setString(1,patientdata.getCpr());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                patientdata.setFornavn(resultSet.getString("fornavn"));
                patientdata.setEfternavn(resultSet.getString("efternavn"));
                patientdata.setAdresse(resultSet.getString("adresse"));

                System.out.println("Patient data hentet");
                return Response.status(Response.Status.CREATED).entity(patientdata).build();
            }else{


            }

        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Intet CPR nr fundet.");

        return Response.status(Response.Status.BAD_REQUEST).entity("Forkert CPR").build();
    }

    public void fetchAftaleDataDB(){

        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-test", "root", "T3stT3st123!");
            PreparedStatement preparedStatement = con.prepareStatement("select * from patient where cpr = ?");
            preparedStatement.setString(1,patientdata.getCpr());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                patientdata.setFornavn(resultSet.getString("fornavn"));
                patientdata.setEfternavn(resultSet.getString("efternavn"));
                patientdata.setAdresse(resultSet.getString("adresse"));

                //JSON til frontend
            }else{

                System.out.println("Intet CPR nr fundet.");

                //Error til API
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}


