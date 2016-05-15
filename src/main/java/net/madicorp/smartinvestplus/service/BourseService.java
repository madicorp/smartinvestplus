package net.madicorp.smartinvestplus.service;

import net.madicorp.smartinvestplus.domain.Bourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Bourse.
 */
public interface BourseService {

    /**
     * Save a bourse.
     * 
     * @param bourse the entity to save
     * @return the persisted entity
     */
    Bourse save(Bourse bourse);

    /**
     *  Get all the bourses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Bourse> findAll(Pageable pageable);

    /**
     *  Get the "id" bourse.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Bourse findOne(Long id);

    /**
     *  Delete the "id" bourse.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bourse corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Bourse> search(String query, Pageable pageable);
}
