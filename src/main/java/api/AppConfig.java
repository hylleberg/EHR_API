package api;


import exceptionhandler.*;
import filters.AuthFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")

public class AppConfig extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        //Endpoints
        classes.add(AuthService.class);
        classes.add(PatientService.class);
        classes.add(AftaleService.class);
        classes.add(AuthFilter.class);
        classes.add(CreateRequestService.class);
        classes.add(FlagService.class);

        //Exception mappers
        classes.add(DataNotFoundExceptionMapper.class);
        classes.add(NotAuthorizedExceptionMapper.class);
        classes.add(ForbiddenExceptionMapper.class);
        classes.add(GenericExceptionMapper.class);
        return classes;
    }
}