package hh.forest_of_habits.utils;

import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.exception.ForbiddenException;
import hh.forest_of_habits.service.AuthFacade;
import hh.forest_of_habits.service.impl.AuthFacadeImpl;
import org.springframework.stereotype.Component;

@Component
public class CheckOwnUtils {

    private static final AuthFacade auth = new AuthFacadeImpl();

    public static void checkOwn(Forest forest) {
        String username = auth.getUsername();
        if (!forest.getUser().getName().equals(username))
            throw new ForbiddenException("Нет доступа");
    }
}
