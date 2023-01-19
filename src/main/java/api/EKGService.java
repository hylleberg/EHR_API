package api;

import business.LoginController;
import datalayer.DAOcontroller;
import exceptionhandler.ForbiddenException;
import filters.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import model.EKGData;
import model.LoginData;
import model.Role;

@Path("/ekg")
public class EKGService {

    @Path("/post")
    @Secured({Role.doctor})
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postEKG(EKGData ekgdata) {
        DAOcontroller dc = new DAOcontroller();

        System.out.println(ekgdata.getEkgDataList());
        dc.createEkgValues(ekgdata);

        return Response.status(Response.Status.CREATED).build();
    }

    @Path("/fetch/recording/{cpr}/{ekgID}")
    @Secured({Role.doctor, Role.patient})
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEKGValues(@PathParam("cpr") String cpr, @PathParam("ekgID") String ekgID, @Context SecurityContext securityContext) {
        DAOcontroller dc = new DAOcontroller();
        if(securityContext.isUserInRole("")){
            // If requested CPR doesn't equal userpatient CPR issue 403 exception
            if(!cpr.equals(securityContext.getUserPrincipal().getName())){
                throw new ForbiddenException("Forsøg på at slå en anden patients målinger op - fy fy");
            }
        }
        return dc.fetchEKGValuesDB(ekgID);
    }

    @Path("/fetch/list/{cpr}")
    @Secured({Role.doctor, Role.patient})
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEKGList(@PathParam("cpr") String cpr, @Context SecurityContext securityContext) {
        DAOcontroller dc = new DAOcontroller();
        if(securityContext.isUserInRole("")){
            // If requested CPR doesn't equal userpatient CPR issue 403 exception
            if(!cpr.equals(securityContext.getUserPrincipal().getName())){
                throw new ForbiddenException("Forsøg på at slå en anden patients målinger op - fy fy");
            }
        }
        return dc.fetchEKGListDB(cpr);
    }
}
