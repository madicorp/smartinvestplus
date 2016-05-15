package net.madicorp.smartinvestplus.service;

import net.madicorp.smartinvestplus.domain.Cloture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Cloture.
 */
public interface ClotureService {

    /**
     * Save a cloture.
     * 
     * @param cloture the entity to save
     * @return the persisted entity
     */
    Cloture save(Cloture cloture);

    /**
     *  Get all the clotures.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Cloture> findAll(Pageable pageable);

    /**
     *  Get the "id" cloture.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Cloture findOne(Long id);

    /**
     *  Delete the "id" cloture.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cloture corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Cloture> search(String query, Pageable pageable);
}
