package hh.forest_of_habits.service;

import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;

public interface AuthFacade {
    String getUsername();

    void checkOwn(Forest forest);

    void checkOwn(Tree tree);
}
