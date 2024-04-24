package hh.forest_of_habits.repository;

import hh.forest_of_habits.dto.response.TreeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeViewRepository extends CrudRepository<TreeResponse, Long> {
    @Query("SELECT t FROM TreeResponse t WHERE t.forestId = :forestId")
    List<TreeResponse> findAllTreeByForest_id(Long forestId);
}