package filters;

import datalayer.DAOcontroller;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Priority;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import model.Role;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {


        System.out.println("Caught in authFilter!");
        Secured annotation = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
        if (annotation == null) { //No annotation on method level
            annotation = resourceInfo.getResourceClass().getAnnotation(Secured.class);
        }
        if (annotation == null) { //No annotation on class level
//            setUserOnContext(requestContext);
            return; //No need to verify - EndPoint is not secured!
        }
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Not authorized!");
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();


        try {
            // Validate the token
            String role = verifyToken(token);
            try{
                //Role stuff
                Class<?> resourceClass = resourceInfo.getResourceClass();
                List<Role> classRoles = extractRoles(resourceClass);

                // Get the resource method which matches with the requested URL
                // Extract the roles declared by it
                Method resourceMethod = resourceInfo.getResourceMethod();
                List<Role> methodRoles = extractRoles(resourceMethod);

                if (methodRoles.isEmpty()) {
                    checkPermissions(classRoles, role);
                } else {
                    checkPermissions(methodRoles, role);
                }
            }catch(Exception e){
                        requestContext.abortWith(
                        Response.status(Response.Status.FORBIDDEN).build());
            }



        } catch (Exception e) {
            System.out.println("Verification failed. Not authorized.");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }


    }

    public String verifyToken(String token) {

        //Split token, payload = index 1
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println(payload);

        //Workaround, padded payload med "|" til at extracte user+role fra token
        String[] klonks = payload.split("\\|");
        String claimedUser = new String(klonks[1]);

        String[] klonks2 = payload.split("\\?");
        String claimedRole= new String(klonks2[1]);

        //Hent secret key fra DB med reference til user
        DAOcontroller dc = new DAOcontroller();
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(dc.getKeyDB(claimedUser)));
        System.out.println("Decoded key: " + key);

        //Endelig verifikation af signatur
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        System.out.println("Body: " + jwt.getBody());

        return claimedRole;
    }


    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<Role>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<Role>();
            } else {
                Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles, String role) throws Exception {

        if(allowedRoles.toString().indexOf(role)>0){
            System.out.println("Allowed role!");
        }else{
            System.out.println("Forbidden!");
            throw new Exception("Forbidden!");
        }

    }
}