package hh.forest_of_habits.repository;

import hh.forest_of_habits.entity.Tree;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRepository extends CrudRepository<Tree, Long> {
    List<Tree> findAllByForest_id(Long forestId);
}