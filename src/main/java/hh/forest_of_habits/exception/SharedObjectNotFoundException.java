package hh.forest_of_habits.exception;

public class SharedObjectNotFoundException extends NotFoundException {
    private static final String MSG = "%s с id = %s не найден";

    public SharedObjectNotFoundException(String type, String id) {
        super(MSG.formatted(type, id));
    }
}
