package net.madicorp.smartinvestplus.service;

import net.madicorp.smartinvestplus.domain.Titre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Titre.
 */
public interface TitreService {

    /**
     * Save a titre.
     * 
     * @param titre the entity to save
     * @return the persisted entity
     */
    Titre save(Titre titre);

    /**
     *  Get all the titres.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Titre> findAll(Pageable pageable);

    /**
     *  Get the "id" titre.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Titre findOne(Long id);

    /**
     *  Delete the "id" titre.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the titre corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Titre> search(String query, Pageable pageable);
}
