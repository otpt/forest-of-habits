package hh.forest_of_habits.exception;

public class EmailAlreadyExistsException extends BadRequestException {
    private static final String MSG = "Email %s уже существует";

    public EmailAlreadyExistsException(String name) {
        super(MSG.formatted(name));
    }
}
