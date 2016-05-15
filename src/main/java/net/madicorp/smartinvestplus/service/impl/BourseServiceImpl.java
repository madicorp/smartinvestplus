package net.madicorp.smartinvestplus.service.impl;

import net.madicorp.smartinvestplus.service.BourseService;
import net.madicorp.smartinvestplus.domain.Bourse;
import net.madicorp.smartinvestplus.repository.BourseRepository;
import net.madicorp.smartinvestplus.repository.search.BourseSearchRepository;
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
 * Service Implementation for managing Bourse.
 */
@Service
@Transactional
public class BourseServiceImpl implements BourseService{

    private final Logger log = LoggerFactory.getLogger(BourseServiceImpl.class);
    
    @Inject
    private BourseRepository bourseRepository;
    
    @Inject
    private BourseSearchRepository bourseSearchRepository;
    
    /**
     * Save a bourse.
     * 
     * @param bourse the entity to save
     * @return the persisted entity
     */
    public Bourse save(Bourse bourse) {
        log.debug("Request to save Bourse : {}", bourse);
        Bourse result = bourseRepository.save(bourse);
        bourseSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the bourses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Bourse> findAll(Pageable pageable) {
        log.debug("Request to get all Bourses");
        Page<Bourse> result = bourseRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one bourse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Bourse findOne(Long id) {
        log.debug("Request to get Bourse : {}", id);
        Bourse bourse = bourseRepository.findOneWithEagerRelationships(id);
        return bourse;
    }

    /**
     *  Delete the  bourse by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bourse : {}", id);
        bourseRepository.delete(id);
        bourseSearchRepository.delete(id);
    }

    /**
     * Search for the bourse corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Bourse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bourses for query {}", query);
        return bourseSearchRepository.search(queryStringQuery(query), pageable);
    }
}
