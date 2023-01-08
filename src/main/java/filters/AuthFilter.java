package filters;

import datalayer.DAOcontroller;

import exceptionhandler.ForbiddenException;
import exceptionhandler.NotAuthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;

import jakarta.ws.rs.ext.Provider;
import model.Role;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

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


        // Validate the token
        String role = verifyToken(token);
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


    }

    public String verifyToken(String token) {
        try {
            //Split token, payload = index 1
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String[] chunks = token.split("\\.");
            String payload = new String(decoder.decode(chunks[1]));
            System.out.println(payload);

            //Workaround, padded payload with "|" in order to extract user/role
            //Principal only produced payload token...
            String[] klonks = payload.split("\\|");
            String claimedUser = new String(klonks[1]);

            String[] klonks2 = payload.split("\\?");
            String claimedRole = new String(klonks2[1]);

            //Fetch secret key with reference to user
            DAOcontroller dc = new DAOcontroller();
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(dc.getKeyDB(claimedUser)));
            System.out.println("Decoded key: " + key);

            //Signature/token verification
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            System.out.println("Body: " + jwt.getBody());
            // Return role for role-based auth
            return claimedRole;

        } catch (Exception e) {
            System.out.println("Verification failed. Not authorized.");
            // requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            throw new NotAuthorizedException("Adgang nægtet.");
        }
    }


    //Extract enum roles from namebinding interface Secured
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

    private void checkPermissions(List<Role> allowedRoles, String role) {
        //Check if there's an index of provided Role in the defined allowed roles
        if (allowedRoles.toString().indexOf(role) > 0) {
            System.out.println("Allowed role!");
        } else {
            System.out.println("Forbidden!");
            throw new ForbiddenException("Du har ikke rettigheder til at tilgå denne funktionalitet.");
        }

    }
}