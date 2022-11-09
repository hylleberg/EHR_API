package api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.LoginData;
import datalayer.DAOcontroller;



@Path("login")
public class LoginService {
    private static DAOcontroller dc = new DAOcontroller();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response doLogin(LoginData logindata){


        return dc.fetchLoginDataDB(logindata);
        }

}
