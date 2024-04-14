package hh.forest_of_habits.exception;

public class BadCredentialsException extends UnauthorizedException {
    private static final String MSG = "Невервый логин или пароль";

    public BadCredentialsException() {
        super(MSG);
    }
}
