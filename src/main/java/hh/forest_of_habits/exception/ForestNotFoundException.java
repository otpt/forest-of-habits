package hh.forest_of_habits.exception;

public class ForestNotFoundException extends NotFoundException {
    private static final String MSG = "Лес с id = %d не найден";

    public ForestNotFoundException(Long id) {
        super(MSG.formatted(id));
    }
}
