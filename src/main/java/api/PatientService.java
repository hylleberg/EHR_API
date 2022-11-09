package api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import datalayer.DAOcontroller;
import model.PatientData;



@Path("cpr")
public class PatientService {
    private static DAOcontroller dc = new DAOcontroller();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response findPatientData(PatientData patientdata){
        return dc.fetchPatientDataDB(patientdata);
    }

}
