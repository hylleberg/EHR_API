package api;

import datalayer.DAOcontroller;
import filters.Secured;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Role;

@Path("/flags")
public class FlagService {
    private static DAOcontroller dc = new DAOcontroller();
    @Path("/{workerusername}")
    @Secured({Role.doctor})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPatientData(@PathParam("workerusername") String workerusername) {

        System.out.println("Patientservice aktiveret");

        return dc.fetchFlagDataToWorker(workerusername);
    }
}
