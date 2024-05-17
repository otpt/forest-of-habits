package hh.forest_of_habits.repository;

import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Tree;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRepository extends CrudRepository<Tree, Long> {
    @Query("SELECT new hh.forest_of_habits.dto.response.TreeResponse(t.id, t.name, t.description, t.type, t.period, t.createdAt, t.limit, t.forest.id, coalesce(SUM(il.value), 0)) " +
            "FROM Tree t LEFT JOIN t.increments il " +
            "WHERE t.forest.id = :forestId AND (t.type <> 'PERIODIC_TREE' OR (t.type = 'PERIODIC_TREE' AND FUNCTION('AGE', coalesce(il.date, current_date), current_date) <= CASE t.period " +
            "WHEN hh.forest_of_habits.enums.TreePeriod.DAY THEN FUNCTION('MAKE_INTERVAL', '0', '0', '0' ,'1') " +
            "WHEN hh.forest_of_habits.enums.TreePeriod.WEEK THEN FUNCTION('MAKE_INTERVAL', '0', '0', '1') " +
            "WHEN hh.forest_of_habits.enums.TreePeriod.MONTH THEN FUNCTION('MAKE_INTERVAL', '0', '1') " +
            "WHEN hh.forest_of_habits.enums.TreePeriod.QUARTER THEN FUNCTION('MAKE_INTERVAL', '0', '3') " +
            "WHEN hh.forest_of_habits.enums.TreePeriod.YEAR THEN FUNCTION('MAKE_INTERVAL', '1') " +
            "END)) " +
            "GROUP BY t.id")
    List<TreeResponse> findTreesWithIncrementsCounter(Long forestId);
}