package net.madicorp.smartinvestplus.repository;

import net.madicorp.smartinvestplus.domain.Bourse;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Bourse entity.
 */
public interface BourseRepository extends JpaRepository<Bourse,Long> {

    @Query("select distinct bourse from Bourse bourse left join fetch bourse.users")
    List<Bourse> findAllWithEagerRelationships();

    @Query("select bourse from Bourse bourse left join fetch bourse.users where bourse.id =:id")
    Bourse findOneWithEagerRelationships(@Param("id") Long id);

}
