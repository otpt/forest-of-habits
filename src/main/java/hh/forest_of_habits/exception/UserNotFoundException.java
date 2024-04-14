package hh.forest_of_habits.exception;

public class UserNotFoundException extends NotFoundException {
    private static final String MSG = "Пользователь с username %s не найден";

    public UserNotFoundException(String username) {
        super(MSG.formatted(username));
    }
}
