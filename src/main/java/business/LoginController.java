package business;

import datalayer.DAOcontroller;
import jakarta.ws.rs.core.Response;
import model.LoginData;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;

import java.util.Calendar;

public class LoginController {

    private static Key key = MacProvider.generateKey(SignatureAlgorithm.HS512);

    public Response validateUser(LoginData logindata) {

        DAOcontroller dc = new DAOcontroller();

        boolean res = dc.fetchLoginDataDB(logindata);

        if (res == true) {
            System.out.println("Kombination korrekt");


            String token = issueToken(logindata.getUsername());
            System.out.println("Token created");
            System.out.println(token);
            return Response.status(Response.Status.CREATED).entity(token).build();
        } else {
            System.out.println("Brugernavn/password kombination forkert.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Forkert kombination").build();
        }
    }

    public static String issueToken(String username){

        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.MINUTE, 240);

        return Jwts.builder()
                .claim("username",username)
                .signWith(SignatureAlgorithm.HS512,key)
                .setExpiration(expiry.getTime())
                .compact();
    }
}
