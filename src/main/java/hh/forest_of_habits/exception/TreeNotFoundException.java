package hh.forest_of_habits.exception;

public class TreeNotFoundException extends NotFoundException {
    private static final String MSG = "Дерево с id = %d не найден";

    public TreeNotFoundException(Long id) {
        super(MSG.formatted(id));
    }
}
