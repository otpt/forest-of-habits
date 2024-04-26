package hh.forest_of_habits.utils;

import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.exception.ForbiddenException;

public class OwnUtils {
    public static void checkOwn(Forest forest) {
        if (!forest.getUser().getName().equals(AuthFacade.getUsername())) {
            throw new ForbiddenException("Нет доступа");
        }
    }

    public static void checkOwn(Tree tree) {
        checkOwn(tree.getForest());
    }
}
