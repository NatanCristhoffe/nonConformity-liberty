package blessed.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}
