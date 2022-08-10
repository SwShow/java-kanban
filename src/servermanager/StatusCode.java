package servermanager;


public enum StatusCode {
    BAD_REQUEST(400),
    SUCCESS(200),
    NOT_IMPLEMENTED(501),
    NOT_FOUND(404);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
