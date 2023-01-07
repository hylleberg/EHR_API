package api;

import datalayer.DAOcontroller;
import filters.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.PatientData;


@Path("aftale")
public class AftaleService {

    private static DAOcontroller dc = new DAOcontroller();

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{cpr}")
    public Response findAftaleData(@PathParam("cpr") String cpr){

        System.out.println("Aftale service aktiviteret");

        PatientData patientdata = new PatientData();
        patientdata.setCpr(cpr);

        return dc.fetchAftaleDataDB(patientdata);
    }
}
