package hh.forest_of_habits.repository;

import hh.forest_of_habits.entity.Forest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForestRepository extends CrudRepository<Forest, Long> {
    List<Forest> findByUser_name(String username);

    Optional<Forest> findBySharedId(UUID uuid);

    @Query(value = "SELECT f.* FROM forests f JOIN accesses a on f.id = a.forest_id where a.user_id = ?1", nativeQuery = true)
    List<Forest> findFriendsForest(Long id);

    @Query(value = "INSERT INTO accesses VALUES(?1, ?2)", nativeQuery = true)
    @Modifying
    @Transactional
    void insertAccess(Long userId, Long forestId);

    @Query(value = "DELETE FROM accesses WHERE user_id = ?1 and forest_id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAccess(Long userId, Long forestId);

    @Query(value = "SELECT COUNT(*) FROM accesses WHERE user_id = ?1 and forest_id = ?2", nativeQuery = true)
    int checkPermission(Long userId, Long forestId);
}
