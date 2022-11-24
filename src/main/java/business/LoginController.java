package business;

import datalayer.DAOcontroller;
import jakarta.ws.rs.core.Response;
import model.LoginData;

public class LoginController {

    public Response validateUser(LoginData logindata) {

        DAOcontroller dc = new DAOcontroller();

        boolean res = dc.fetchLoginDataDB(logindata);
        if (res == true) {
            System.out.println("Kombination korrekt");
            return Response.status(Response.Status.CREATED).entity("Kombinatioon korrekt!").build();
        } else {
            System.out.println("Brugernavn/password kombination forkert.");
            return Response.status(Response.Status.BAD_REQUEST).entity("Forkert kombination").build();
        }
    }
}
