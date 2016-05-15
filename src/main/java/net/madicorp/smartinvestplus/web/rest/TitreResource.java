package net.madicorp.smartinvestplus.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.madicorp.smartinvestplus.domain.Titre;
import net.madicorp.smartinvestplus.service.TitreService;
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
 * REST controller for managing Titre.
 */
@RestController
@RequestMapping("/api")
public class TitreResource {

    private final Logger log = LoggerFactory.getLogger(TitreResource.class);
        
    @Inject
    private TitreService titreService;
    
    /**
     * POST  /titres : Create a new titre.
     *
     * @param titre the titre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new titre, or with status 400 (Bad Request) if the titre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/titres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Titre> createTitre(@Valid @RequestBody Titre titre) throws URISyntaxException {
        log.debug("REST request to save Titre : {}", titre);
        if (titre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("titre", "idexists", "A new titre cannot already have an ID")).body(null);
        }
        Titre result = titreService.save(titre);
        return ResponseEntity.created(new URI("/api/titres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("titre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /titres : Updates an existing titre.
     *
     * @param titre the titre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated titre,
     * or with status 400 (Bad Request) if the titre is not valid,
     * or with status 500 (Internal Server Error) if the titre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/titres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Titre> updateTitre(@Valid @RequestBody Titre titre) throws URISyntaxException {
        log.debug("REST request to update Titre : {}", titre);
        if (titre.getId() == null) {
            return createTitre(titre);
        }
        Titre result = titreService.save(titre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("titre", titre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /titres : get all the titres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of titres in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/titres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Titre>> getAllTitres(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Titres");
        Page<Titre> page = titreService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/titres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /titres/:id : get the "id" titre.
     *
     * @param id the id of the titre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the titre, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/titres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Titre> getTitre(@PathVariable Long id) {
        log.debug("REST request to get Titre : {}", id);
        Titre titre = titreService.findOne(id);
        return Optional.ofNullable(titre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /titres/:id : delete the "id" titre.
     *
     * @param id the id of the titre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/titres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTitre(@PathVariable Long id) {
        log.debug("REST request to delete Titre : {}", id);
        titreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("titre", id.toString())).build();
    }

    /**
     * SEARCH  /_search/titres?query=:query : search for the titre corresponding
     * to the query.
     *
     * @param query the query of the titre search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/titres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Titre>> searchTitres(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Titres for query {}", query);
        Page<Titre> page = titreService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/titres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
