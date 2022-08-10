package servermanager;

public class WrongResponseException extends RuntimeException {

    public WrongResponseException() {

        }

    public WrongResponseException(String message) {
            super(message);
        }

    }
