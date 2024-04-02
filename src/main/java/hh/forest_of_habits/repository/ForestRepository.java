package hh.forest_of_habits.repository;

import hh.forest_of_habits.entity.Forest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForestRepository extends CrudRepository<Forest, Long> {
    List<Forest> findByUser_name(String username);
}
