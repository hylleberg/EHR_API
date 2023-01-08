package exceptionhandler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import model.ErrorMessage;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        ErrorMessage errorMsg = new ErrorMessage(e.getMessage(), 403);
        return Response.status(Response.Status.FORBIDDEN).entity(errorMsg).build();
    }
}
