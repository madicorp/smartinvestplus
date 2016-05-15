package net.madicorp.smartinvestplus.repository;

import net.madicorp.smartinvestplus.domain.Fractionnement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fractionnement entity.
 */
public interface FractionnementRepository extends JpaRepository<Fractionnement,Long> {

}
