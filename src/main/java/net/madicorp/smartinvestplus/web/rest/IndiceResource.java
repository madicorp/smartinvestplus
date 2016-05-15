package net.madicorp.smartinvestplus.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.madicorp.smartinvestplus.domain.Indice;
import net.madicorp.smartinvestplus.service.IndiceService;
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
 * REST controller for managing Indice.
 */
@RestController
@RequestMapping("/api")
public class IndiceResource {

    private final Logger log = LoggerFactory.getLogger(IndiceResource.class);
        
    @Inject
    private IndiceService indiceService;
    
    /**
     * POST  /indices : Create a new indice.
     *
     * @param indice the indice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new indice, or with status 400 (Bad Request) if the indice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/indices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Indice> createIndice(@Valid @RequestBody Indice indice) throws URISyntaxException {
        log.debug("REST request to save Indice : {}", indice);
        if (indice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("indice", "idexists", "A new indice cannot already have an ID")).body(null);
        }
        Indice result = indiceService.save(indice);
        return ResponseEntity.created(new URI("/api/indices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("indice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /indices : Updates an existing indice.
     *
     * @param indice the indice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated indice,
     * or with status 400 (Bad Request) if the indice is not valid,
     * or with status 500 (Internal Server Error) if the indice couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/indices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Indice> updateIndice(@Valid @RequestBody Indice indice) throws URISyntaxException {
        log.debug("REST request to update Indice : {}", indice);
        if (indice.getId() == null) {
            return createIndice(indice);
        }
        Indice result = indiceService.save(indice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("indice", indice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /indices : get all the indices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of indices in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/indices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Indice>> getAllIndices(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Indices");
        Page<Indice> page = indiceService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/indices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /indices/:id : get the "id" indice.
     *
     * @param id the id of the indice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the indice, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/indices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Indice> getIndice(@PathVariable Long id) {
        log.debug("REST request to get Indice : {}", id);
        Indice indice = indiceService.findOne(id);
        return Optional.ofNullable(indice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /indices/:id : delete the "id" indice.
     *
     * @param id the id of the indice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/indices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIndice(@PathVariable Long id) {
        log.debug("REST request to delete Indice : {}", id);
        indiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("indice", id.toString())).build();
    }

    /**
     * SEARCH  /_search/indices?query=:query : search for the indice corresponding
     * to the query.
     *
     * @param query the query of the indice search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/indices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Indice>> searchIndices(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Indices for query {}", query);
        Page<Indice> page = indiceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/indices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
