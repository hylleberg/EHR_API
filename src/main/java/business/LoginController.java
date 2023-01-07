package business;


import datalayer.DAOcontroller;


import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.core.Response;
import model.LoginData;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
;
import java.util.Base64;
import java.util.Calendar;

public class LoginController {



    public Response validateUser(LoginData logindata) {

        DAOcontroller dc = new DAOcontroller();

        boolean res = dc.fetchLoginDataDB(logindata);

        if (res == true) {

                System.out.println("Kombination korrekt");
                String token = issueToken(logindata.getUsername());
                System.out.println("Token created");
                System.out.println("issued token: " +  token);

            return Response.status(Response.Status.CREATED).entity(token).build();

        } else {
            System.out.println("Brugernavn/password kombination forkert.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Forkert kombination").build();
        }
    }

    public String issueToken(String username){
        DAOcontroller dc = new DAOcontroller();

        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.MINUTE, 240);


        SecretKey key = new generateKey().getKey();
        //Encode key to String for DB
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Encdoed key: " + key);
        //Save encodedKey to DB, referenced to claim "username"
        dc.setKeyDB(secretString, username);
        System.out.println("Key hash: " + key.hashCode());
        String prepUsername = "|"+username+"|";

        //Build token, signwith key
        return Jwts.builder()
                .claim("username",prepUsername)
                .signWith(key)
                .setExpiration(expiry.getTime())
                .compact();
    }


    private String decode(String encodedString) {
      return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
