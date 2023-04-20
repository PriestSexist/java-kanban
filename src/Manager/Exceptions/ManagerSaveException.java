package Manager.Exceptions;

public class ManagerSaveException extends Exception{
    public ManagerSaveException(String message) {
        super(message);
    }

    public String getDetailMessage() {
        return getMessage();
    }
}
