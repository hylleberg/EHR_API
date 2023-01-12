package api;

import datalayer.DAOcontroller;
import exceptionhandler.DataNotFoundException;
import filters.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.AftaleData;
import model.PatientData;
import model.Role;
import model.WorkerData;


@Path("/aftale")
public class AftaleService {

    private static DAOcontroller dc = new DAOcontroller();

    @Secured({Role.doctor, Role.patient})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/patient/{cpr}")
    public Response findAftaleData(@PathParam("cpr") String cpr) {

        System.out.println("GET CPR" + cpr);
        if(cpr.equals("null")){
            System.out.println("404 issued, no cpr");
            throw new DataNotFoundException(" Du har ikke valgt en patient.");
        }else{
            return dc.fetchAftaleDataPatientDB(cpr);
        }

    }

    @Secured({Role.doctor, Role.patient})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{aftaleid}")
    public Response findAftale(@PathParam("aftaleid") int aftaleid) {


            return dc.fetchAftaleDB(aftaleid);


    }

    @Secured({Role.doctor})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/worker/{workerusername}")
    public Response findAftaleWorkerData(@PathParam("workerusername") String workerusername) {

        System.out.println("Aftale worker service aktiviteret");


        return dc.fetchAftaleDataWorkerDB(workerusername);
    }





}
