package api;

import business.LoginController;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.LoginData;

@Path("auth")
public class AuthService {
    private static LoginController lc = new LoginController();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response doLogin(LoginData logindata) {
        return lc.validateUser(logindata);
    }
}
