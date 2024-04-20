package hh.forest_of_habits.exception;

public class JwtTokenException extends RuntimeException {
    private static final String MSG = "Срок действия токена истек";

    public JwtTokenException() {
        super(MSG);
    }
}
