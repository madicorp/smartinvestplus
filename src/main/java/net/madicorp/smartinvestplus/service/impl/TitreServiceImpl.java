package net.madicorp.smartinvestplus.service.impl;

import net.madicorp.smartinvestplus.service.TitreService;
import net.madicorp.smartinvestplus.domain.Titre;
import net.madicorp.smartinvestplus.repository.TitreRepository;
import net.madicorp.smartinvestplus.repository.search.TitreSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Titre.
 */
@Service
@Transactional
public class TitreServiceImpl implements TitreService{

    private final Logger log = LoggerFactory.getLogger(TitreServiceImpl.class);
    
    @Inject
    private TitreRepository titreRepository;
    
    @Inject
    private TitreSearchRepository titreSearchRepository;
    
    /**
     * Save a titre.
     * 
     * @param titre the entity to save
     * @return the persisted entity
     */
    public Titre save(Titre titre) {
        log.debug("Request to save Titre : {}", titre);
        Titre result = titreRepository.save(titre);
        titreSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the titres.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Titre> findAll(Pageable pageable) {
        log.debug("Request to get all Titres");
        Page<Titre> result = titreRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one titre by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Titre findOne(Long id) {
        log.debug("Request to get Titre : {}", id);
        Titre titre = titreRepository.findOne(id);
        return titre;
    }

    /**
     *  Delete the  titre by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Titre : {}", id);
        titreRepository.delete(id);
        titreSearchRepository.delete(id);
    }

    /**
     * Search for the titre corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Titre> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Titres for query {}", query);
        return titreSearchRepository.search(queryStringQuery(query), pageable);
    }
}
