package net.madicorp.smartinvestplus.service;

import net.madicorp.smartinvestplus.domain.Indice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Indice.
 */
public interface IndiceService {

    /**
     * Save a indice.
     * 
     * @param indice the entity to save
     * @return the persisted entity
     */
    Indice save(Indice indice);

    /**
     *  Get all the indices.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Indice> findAll(Pageable pageable);

    /**
     *  Get the "id" indice.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Indice findOne(Long id);

    /**
     *  Delete the "id" indice.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the indice corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Indice> search(String query, Pageable pageable);
}
