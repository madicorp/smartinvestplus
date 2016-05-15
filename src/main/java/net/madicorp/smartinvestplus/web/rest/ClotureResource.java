package net.madicorp.smartinvestplus.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.madicorp.smartinvestplus.domain.Cloture;
import net.madicorp.smartinvestplus.service.ClotureService;
import net.madicorp.smartinvestplus.web.rest.util.HeaderUtil;
import net.madicorp.smartinvestplus.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cloture.
 */
@RestController
@RequestMapping("/api")
public class ClotureResource {

    private final Logger log = LoggerFactory.getLogger(ClotureResource.class);
        
    @Inject
    private ClotureService clotureService;
    
    /**
     * POST  /clotures : Create a new cloture.
     *
     * @param cloture the cloture to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cloture, or with status 400 (Bad Request) if the cloture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clotures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cloture> createCloture(@Valid @RequestBody Cloture cloture) throws URISyntaxException {
        log.debug("REST request to save Cloture : {}", cloture);
        if (cloture.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cloture", "idexists", "A new cloture cannot already have an ID")).body(null);
        }
        Cloture result = clotureService.save(cloture);
        return ResponseEntity.created(new URI("/api/clotures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cloture", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clotures : Updates an existing cloture.
     *
     * @param cloture the cloture to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cloture,
     * or with status 400 (Bad Request) if the cloture is not valid,
     * or with status 500 (Internal Server Error) if the cloture couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clotures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cloture> updateCloture(@Valid @RequestBody Cloture cloture) throws URISyntaxException {
        log.debug("REST request to update Cloture : {}", cloture);
        if (cloture.getId() == null) {
            return createCloture(cloture);
        }
        Cloture result = clotureService.save(cloture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cloture", cloture.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clotures : get all the clotures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clotures in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/clotures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cloture>> getAllClotures(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Clotures");
        Page<Cloture> page = clotureService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clotures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clotures/:id : get the "id" cloture.
     *
     * @param id the id of the cloture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cloture, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/clotures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cloture> getCloture(@PathVariable Long id) {
        log.debug("REST request to get Cloture : {}", id);
        Cloture cloture = clotureService.findOne(id);
        return Optional.ofNullable(cloture)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clotures/:id : delete the "id" cloture.
     *
     * @param id the id of the cloture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/clotures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCloture(@PathVariable Long id) {
        log.debug("REST request to delete Cloture : {}", id);
        clotureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cloture", id.toString())).build();
    }

    /**
     * SEARCH  /_search/clotures?query=:query : search for the cloture corresponding
     * to the query.
     *
     * @param query the query of the cloture search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/clotures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cloture>> searchClotures(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Clotures for query {}", query);
        Page<Cloture> page = clotureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/clotures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
