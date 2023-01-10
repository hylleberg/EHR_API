package api;

import exceptionhandler.ForbiddenException;
import filters.Secured;
import jakarta.ws.rs.*;


import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import datalayer.DAOcontroller;
import jakarta.ws.rs.core.SecurityContext;
import model.PatientData;
import model.Role;

import java.security.Principal;


@Path("{cpr}")

public class PatientService {
    private static DAOcontroller dc = new DAOcontroller();

    @Secured({Role.doctor, Role.patient})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPatientData(@PathParam("cpr") String cpr,@Context SecurityContext securityContext) {

        System.out.println("Patientservice aktiveret");

        // Check if user request is role patient
        if(securityContext.isUserInRole("")){
            // If requested CPR doesn't equal userpatient CPR issue 403 exception
            if(!cpr.equals(securityContext.getUserPrincipal().getName())){
                throw new ForbiddenException("Forsøg på at slå en anden patient op - fy fy");
            }
        }

        PatientData patientdata = new PatientData();
        patientdata.setCpr(cpr);
        return dc.fetchPatientDataDB(patientdata);
    }

}
