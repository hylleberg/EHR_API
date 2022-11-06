package api;

import datalayer.DAOcontroller;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.PatientData;


@Path("aftale")
public class AftaleService {

    private static DAOcontroller dc = new DAOcontroller();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response findAftaleData(PatientData patientdata){
        System.out.println("aftale service aktiviteret");
        return dc.fetchAftaleDataDB(patientdata);
    }
}
