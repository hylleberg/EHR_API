package exceptionhandler;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import model.ErrorMessage;




@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {
    @Override
    public Response toResponse(DataNotFoundException e) {
        ErrorMessage errorMsg = new ErrorMessage(e.getMessage(), 404);
        return Response.status(Response.Status.NOT_FOUND).entity(errorMsg).build();
    }
}
