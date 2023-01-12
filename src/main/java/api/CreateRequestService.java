package api;

import datalayer.DAOcontroller;
import filters.Secured;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.RequestData;
import model.Role;


@Path("/requestcons")
public class CreateRequestService {

    private static DAOcontroller dc = new DAOcontroller();

    @Secured({Role.patient})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doLogin(RequestData requestdata) {
        System.out.println("Request service activated");
        dc.saveConsultationReqToDB(requestdata);

        return Response.status(Response.Status.CREATED).build();
    }
}
