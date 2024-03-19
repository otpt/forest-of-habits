package hh.forest_of_habits.repository;

import hh.forest_of_habits.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUsersByName(String name);
}
