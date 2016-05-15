package net.madicorp.smartinvestplus.repository;

import net.madicorp.smartinvestplus.domain.Titre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Titre entity.
 */
public interface TitreRepository extends JpaRepository<Titre,Long> {

}
