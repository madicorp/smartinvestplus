package net.madicorp.smartinvestplus.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.madicorp.smartinvestplus.domain.Fractionnement;
import net.madicorp.smartinvestplus.service.FractionnementService;
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
 * REST controller for managing Fractionnement.
 */
@RestController
@RequestMapping("/api")
public class FractionnementResource {

    private final Logger log = LoggerFactory.getLogger(FractionnementResource.class);
        
    @Inject
    private FractionnementService fractionnementService;
    
    /**
     * POST  /fractionnements : Create a new fractionnement.
     *
     * @param fractionnement the fractionnement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fractionnement, or with status 400 (Bad Request) if the fractionnement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fractionnements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fractionnement> createFractionnement(@Valid @RequestBody Fractionnement fractionnement) throws URISyntaxException {
        log.debug("REST request to save Fractionnement : {}", fractionnement);
        if (fractionnement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fractionnement", "idexists", "A new fractionnement cannot already have an ID")).body(null);
        }
        Fractionnement result = fractionnementService.save(fractionnement);
        return ResponseEntity.created(new URI("/api/fractionnements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fractionnement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fractionnements : Updates an existing fractionnement.
     *
     * @param fractionnement the fractionnement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fractionnement,
     * or with status 400 (Bad Request) if the fractionnement is not valid,
     * or with status 500 (Internal Server Error) if the fractionnement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fractionnements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fractionnement> updateFractionnement(@Valid @RequestBody Fractionnement fractionnement) throws URISyntaxException {
        log.debug("REST request to update Fractionnement : {}", fractionnement);
        if (fractionnement.getId() == null) {
            return createFractionnement(fractionnement);
        }
        Fractionnement result = fractionnementService.save(fractionnement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fractionnement", fractionnement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fractionnements : get all the fractionnements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fractionnements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/fractionnements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fractionnement>> getAllFractionnements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Fractionnements");
        Page<Fractionnement> page = fractionnementService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fractionnements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fractionnements/:id : get the "id" fractionnement.
     *
     * @param id the id of the fractionnement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fractionnement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fractionnements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fractionnement> getFractionnement(@PathVariable Long id) {
        log.debug("REST request to get Fractionnement : {}", id);
        Fractionnement fractionnement = fractionnementService.findOne(id);
        return Optional.ofNullable(fractionnement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fractionnements/:id : delete the "id" fractionnement.
     *
     * @param id the id of the fractionnement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fractionnements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFractionnement(@PathVariable Long id) {
        log.debug("REST request to delete Fractionnement : {}", id);
        fractionnementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fractionnement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fractionnements?query=:query : search for the fractionnement corresponding
     * to the query.
     *
     * @param query the query of the fractionnement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/fractionnements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fractionnement>> searchFractionnements(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Fractionnements for query {}", query);
        Page<Fractionnement> page = fractionnementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fractionnements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
