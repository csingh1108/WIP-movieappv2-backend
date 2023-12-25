package moviewebsiteapp.backend.errors;

public class SeatAlreadyTakenException extends RuntimeException{
    public SeatAlreadyTakenException(String message){
        super(message);
    }
}
