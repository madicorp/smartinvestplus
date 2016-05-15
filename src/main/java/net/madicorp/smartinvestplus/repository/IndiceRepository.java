package net.madicorp.smartinvestplus.repository;

import net.madicorp.smartinvestplus.domain.Indice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Indice entity.
 */
public interface IndiceRepository extends JpaRepository<Indice,Long> {

}
