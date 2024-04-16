package hh.forest_of_habits.exception;

public class InternalServerErrorException extends RuntimeException {
    static private final String MSG = "Ошибка сервера";

    public InternalServerErrorException() {
        super(MSG);
    }
}
