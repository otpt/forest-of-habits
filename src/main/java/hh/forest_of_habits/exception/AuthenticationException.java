package hh.forest_of_habits.exception;

public class AuthenticationException extends RuntimeException {
    private static final String MSG = "Неверный логин или пароль";

    public AuthenticationException() {
        super(MSG);
    }
}
