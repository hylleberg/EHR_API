package business;


import datalayer.DAOcontroller;


import io.jsonwebtoken.io.Encoders;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import model.LoginData;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Calendar;

public class LoginController {


    public Response validateUser(LoginData logindata) {

        DAOcontroller dc = new DAOcontroller();

        // Check DB for valid logindata, return user role
        String res = dc.fetchLoginDataDB(logindata);

        if (res.length() > 0) {
            System.out.println("Kombination korrekt");
            String token = issueToken(logindata.getUsername(), res);

            System.out.println("Token created");
            System.out.println("issued token: " + token);

            return Response.status(Response.Status.CREATED).entity(token).build();

        } else {
            System.out.println("Brugernavn/password kombination forkert.");
            // return Response.status(Response.Status.UNAUTHORIZED).entity("Forkert kombination").build();
            throw new NotAuthorizedException("Adgang nægtet.");
        }
    }

    public String issueToken(String username, String role) {
        DAOcontroller dc = new DAOcontroller();

        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.MINUTE, 240);

        //generate secret key
        SecretKey key = new GenerateKey().getKey();

        //Encode key to String for DB
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Encdoed key: " + key);

        //Save encodedKey to DB, referenced to claim "username"
        dc.setKeyDB(secretString, username);
        String prepUsername = "|" + username + "|";

        //Padding for role
        String prepRole = "?" + role + "?";

        //Build token, signwith key
        return Jwts.builder()
                .setSubject(prepUsername)
                .claim("username", prepUsername)
                .claim("Role", prepRole)
                .signWith(key)
                .setExpiration(expiry.getTime())
                .compact();
    }
}
