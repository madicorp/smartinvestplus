package net.madicorp.smartinvestplus.service;

import net.madicorp.smartinvestplus.domain.Fractionnement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Fractionnement.
 */
public interface FractionnementService {

    /**
     * Save a fractionnement.
     * 
     * @param fractionnement the entity to save
     * @return the persisted entity
     */
    Fractionnement save(Fractionnement fractionnement);

    /**
     *  Get all the fractionnements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Fractionnement> findAll(Pageable pageable);

    /**
     *  Get the "id" fractionnement.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Fractionnement findOne(Long id);

    /**
     *  Delete the "id" fractionnement.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the fractionnement corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Fractionnement> search(String query, Pageable pageable);
}
