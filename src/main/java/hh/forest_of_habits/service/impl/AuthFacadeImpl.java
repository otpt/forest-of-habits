package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.exception.ForbiddenException;
import hh.forest_of_habits.service.AuthFacade;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacadeImpl implements AuthFacade {
    @Override
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public void checkOwn(Forest forest) {
        if (!forest.getUser().getName().equals(getUsername())) {
            throw new ForbiddenException("Нет доступа");
        }
    }

    @Override
    public void checkOwn(Tree tree) {
        checkOwn(tree.getForest());
    }
}
