package api;

import datalayer.DAOcontroller;
import exceptionhandler.DataNotFoundException;
import filters.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.RequestData;
import model.Role;


@Path("/request")
public class RequestService {

    private static DAOcontroller dc = new DAOcontroller();

    @Secured({Role.patient})
    @Path("/save/request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveRequest(RequestData requestdata) {
        System.out.println("Request service activated");
        dc.saveConsultationReqToDB(requestdata);

        return Response.status(Response.Status.CREATED).build();
    }

    @Secured({Role.doctor})
    @Path("/save/aftale")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAftale(AftaleData aftaledata) {
        System.out.println("Request service activated");
        dc.saveAftaleToDB(aftaledata);

        return Response.status(Response.Status.CREATED).build();
    }

    @Secured({Role.doctor})
    @Path("/flags/{workerusername}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchFlags(@PathParam("workerusername") String workerusername) {

        System.out.println("fetch flags service to worker");

        return dc.fetchFlagDataToWorker(workerusername);
    }

    @Secured({Role.doctor})
    @Path("/list/{workerusername}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchList(@PathParam("workerusername") String workerusername) {
        System.out.println("Request service LIST activated");


            return dc.fetchRequestListToWorker(workerusername);

    }

    @Secured({Role.doctor})
    @Path("/{aftaleid}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchRequest(@PathParam("aftaleid") int aftaleid) {
        System.out.println("fetch request service activated");
        return dc.fetchRequestDB(aftaleid);
    }


}
