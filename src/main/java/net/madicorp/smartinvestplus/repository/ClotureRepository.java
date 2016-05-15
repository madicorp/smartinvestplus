package net.madicorp.smartinvestplus.repository;

import net.madicorp.smartinvestplus.domain.Cloture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cloture entity.
 */
public interface ClotureRepository extends JpaRepository<Cloture,Long> {

}
