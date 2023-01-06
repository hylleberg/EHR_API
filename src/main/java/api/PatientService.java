package api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import datalayer.DAOcontroller;
import model.PatientData;


// GET patientdata from search query "CPR"

@Path("{cpr}")
public class PatientService {
    private static DAOcontroller dc = new DAOcontroller();

    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public Response findPatientData(@PathParam("cpr") String cpr){

        System.out.println("Patientservice aktiveret");

        PatientData patientdata = new PatientData();
        patientdata.setCpr(cpr);
        return dc.fetchPatientDataDB(patientdata);
    }

}
