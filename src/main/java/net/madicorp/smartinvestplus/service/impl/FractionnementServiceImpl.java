package net.madicorp.smartinvestplus.service.impl;

import net.madicorp.smartinvestplus.domain.Cloture;
import net.madicorp.smartinvestplus.domain.Titre;
import net.madicorp.smartinvestplus.repository.ClotureRepository;
import net.madicorp.smartinvestplus.service.FractionnementService;
import net.madicorp.smartinvestplus.domain.Fractionnement;
import net.madicorp.smartinvestplus.repository.FractionnementRepository;
import net.madicorp.smartinvestplus.repository.search.FractionnementSearchRepository;
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
 * Service Implementation for managing Fractionnement.
 */
@Service
@Transactional
public class FractionnementServiceImpl implements FractionnementService{

    private final Logger log = LoggerFactory.getLogger(FractionnementServiceImpl.class);

    @Inject
    private FractionnementRepository fractionnementRepository;

    @Inject
    private ClotureRepository clotureRepository;

    @Inject
    private FractionnementSearchRepository fractionnementSearchRepository;

    /**
     * Save a fractionnement.
     *
     * @param fractionnement the entity to save
     * @return the persisted entity
     */
    public Fractionnement save(Fractionnement fractionnement) {
        log.debug("Request to save Fractionnement : {}", fractionnement);
        Fractionnement result =  result = fractionnementRepository.save(fractionnement);
        return result;
    }

    /**
     *  Get all the fractionnements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Fractionnement> findAll(Pageable pageable) {
        log.debug("Request to get all Fractionnements");
        Page<Fractionnement> result = fractionnementRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one fractionnement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Fractionnement findOne(Long id) {
        log.debug("Request to get Fractionnement : {}", id);
        Fractionnement fractionnement = fractionnementRepository.findOne(id);
        return fractionnement;
    }

    /**
     *  Delete the  fractionnement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Fractionnement : {}", id);
        fractionnementRepository.delete(id);
        fractionnementSearchRepository.delete(id);
    }

    /**
     * Search for the fractionnement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Fractionnement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Fractionnements for query {}", query);
        return fractionnementSearchRepository.search(queryStringQuery(query), pageable);
    }
}
