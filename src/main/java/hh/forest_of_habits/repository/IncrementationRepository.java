package hh.forest_of_habits.repository;

import hh.forest_of_habits.entity.Incrementation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncrementationRepository extends CrudRepository<Incrementation, Long> {
    List<Incrementation> findAllByTreeId(Long treeId);
}