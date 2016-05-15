package net.madicorp.smartinvestplus.service.impl;

import net.madicorp.smartinvestplus.service.IndiceService;
import net.madicorp.smartinvestplus.domain.Indice;
import net.madicorp.smartinvestplus.repository.IndiceRepository;
import net.madicorp.smartinvestplus.repository.search.IndiceSearchRepository;
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
 * Service Implementation for managing Indice.
 */
@Service
@Transactional
public class IndiceServiceImpl implements IndiceService{

    private final Logger log = LoggerFactory.getLogger(IndiceServiceImpl.class);
    
    @Inject
    private IndiceRepository indiceRepository;
    
    @Inject
    private IndiceSearchRepository indiceSearchRepository;
    
    /**
     * Save a indice.
     * 
     * @param indice the entity to save
     * @return the persisted entity
     */
    public Indice save(Indice indice) {
        log.debug("Request to save Indice : {}", indice);
        Indice result = indiceRepository.save(indice);
        indiceSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the indices.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Indice> findAll(Pageable pageable) {
        log.debug("Request to get all Indices");
        Page<Indice> result = indiceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one indice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Indice findOne(Long id) {
        log.debug("Request to get Indice : {}", id);
        Indice indice = indiceRepository.findOne(id);
        return indice;
    }

    /**
     *  Delete the  indice by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Indice : {}", id);
        indiceRepository.delete(id);
        indiceSearchRepository.delete(id);
    }

    /**
     * Search for the indice corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Indice> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Indices for query {}", query);
        return indiceSearchRepository.search(queryStringQuery(query), pageable);
    }
}
