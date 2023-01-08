package exceptionhandler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import model.ErrorMessage;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
    @Override
    public Response toResponse(NotAuthorizedException e) {
        System.out.println("Not authorized exception issued");
        ErrorMessage errorMsg = new ErrorMessage(e.getMessage(), 401);
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
    }
}
