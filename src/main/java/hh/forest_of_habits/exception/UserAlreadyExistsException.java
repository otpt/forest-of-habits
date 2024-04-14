package hh.forest_of_habits.exception;

public class UserAlreadyExistsException extends BadRequestException {
    private static final String MSG = "Пользователь с именем %s уже существует";

    public UserAlreadyExistsException(String name) {
        super(MSG.formatted(name));
    }
}
