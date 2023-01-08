package exceptionhandler;

public class NotAuthorizedException extends RuntimeException{

    public NotAuthorizedException(String msg){
        super(msg);
    }
}
