package net.madicorp.smartinvestplus.service.impl;

import net.madicorp.smartinvestplus.service.ClotureService;
import net.madicorp.smartinvestplus.domain.Cloture;
import net.madicorp.smartinvestplus.repository.ClotureRepository;
import net.madicorp.smartinvestplus.repository.search.ClotureSearchRepository;
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
 * Service Implementation for managing Cloture.
 */
@Service
@Transactional
public class ClotureServiceImpl implements ClotureService{

    private final Logger log = LoggerFactory.getLogger(ClotureServiceImpl.class);
    
    @Inject
    private ClotureRepository clotureRepository;
    
    @Inject
    private ClotureSearchRepository clotureSearchRepository;
    
    /**
     * Save a cloture.
     * 
     * @param cloture the entity to save
     * @return the persisted entity
     */
    public Cloture save(Cloture cloture) {
        log.debug("Request to save Cloture : {}", cloture);
        Cloture result = clotureRepository.save(cloture);
        clotureSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the clotures.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Cloture> findAll(Pageable pageable) {
        log.debug("Request to get all Clotures");
        Page<Cloture> result = clotureRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one cloture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Cloture findOne(Long id) {
        log.debug("Request to get Cloture : {}", id);
        Cloture cloture = clotureRepository.findOne(id);
        return cloture;
    }

    /**
     *  Delete the  cloture by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cloture : {}", id);
        clotureRepository.delete(id);
        clotureSearchRepository.delete(id);
    }

    /**
     * Search for the cloture corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cloture> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clotures for query {}", query);
        return clotureSearchRepository.search(queryStringQuery(query), pageable);
    }
}
