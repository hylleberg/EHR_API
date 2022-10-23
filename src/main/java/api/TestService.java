package api;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("test")
public class TestService {

@GET
    public String getTest(){
        return "TEST";
    }


}
