package net.madicorp.smartinvestplus.web.rest;

import net.madicorp.smartinvestplus.SmartinvestplusApp;
import net.madicorp.smartinvestplus.domain.Titre;
import net.madicorp.smartinvestplus.repository.TitreRepository;
import net.madicorp.smartinvestplus.service.TitreService;
import net.madicorp.smartinvestplus.repository.search.TitreSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TitreResource REST controller.
 *
 * @see TitreResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartinvestplusApp.class)
@WebAppConfiguration
@IntegrationTest
public class TitreResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_SYMBOLE = "AAAAA";
    private static final String UPDATED_SYMBOLE = "BBBBB";

    @Inject
    private TitreRepository titreRepository;

    @Inject
    private TitreService titreService;

    @Inject
    private TitreSearchRepository titreSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTitreMockMvc;

    private Titre titre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TitreResource titreResource = new TitreResource();
        ReflectionTestUtils.setField(titreResource, "titreService", titreService);
        this.restTitreMockMvc = MockMvcBuilders.standaloneSetup(titreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        titreSearchRepository.deleteAll();
        titre = new Titre();
        titre.setNom(DEFAULT_NOM);
        titre.setSymbole(DEFAULT_SYMBOLE);
    }

    @Test
    @Transactional
    public void createTitre() throws Exception {
        int databaseSizeBeforeCreate = titreRepository.findAll().size();

        // Create the Titre

        restTitreMockMvc.perform(post("/api/titres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(titre)))
                .andExpect(status().isCreated());

        // Validate the Titre in the database
        List<Titre> titres = titreRepository.findAll();
        assertThat(titres).hasSize(databaseSizeBeforeCreate + 1);
        Titre testTitre = titres.get(titres.size() - 1);
        assertThat(testTitre.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testTitre.getSymbole()).isEqualTo(DEFAULT_SYMBOLE);

        // Validate the Titre in ElasticSearch
        Titre titreEs = titreSearchRepository.findOne(testTitre.getId());
        assertThat(titreEs).isEqualToComparingFieldByField(testTitre);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = titreRepository.findAll().size();
        // set the field null
        titre.setNom(null);

        // Create the Titre, which fails.

        restTitreMockMvc.perform(post("/api/titres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(titre)))
                .andExpect(status().isBadRequest());

        List<Titre> titres = titreRepository.findAll();
        assertThat(titres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTitres() throws Exception {
        // Initialize the database
        titreRepository.saveAndFlush(titre);

        // Get all the titres
        restTitreMockMvc.perform(get("/api/titres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(titre.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].symbole").value(hasItem(DEFAULT_SYMBOLE.toString())));
    }

    @Test
    @Transactional
    public void getTitre() throws Exception {
        // Initialize the database
        titreRepository.saveAndFlush(titre);

        // Get the titre
        restTitreMockMvc.perform(get("/api/titres/{id}", titre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(titre.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.symbole").value(DEFAULT_SYMBOLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTitre() throws Exception {
        // Get the titre
        restTitreMockMvc.perform(get("/api/titres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTitre() throws Exception {
        // Initialize the database
        titreService.save(titre);

        int databaseSizeBeforeUpdate = titreRepository.findAll().size();

        // Update the titre
        Titre updatedTitre = new Titre();
        updatedTitre.setId(titre.getId());
        updatedTitre.setNom(UPDATED_NOM);
        updatedTitre.setSymbole(UPDATED_SYMBOLE);

        restTitreMockMvc.perform(put("/api/titres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTitre)))
                .andExpect(status().isOk());

        // Validate the Titre in the database
        List<Titre> titres = titreRepository.findAll();
        assertThat(titres).hasSize(databaseSizeBeforeUpdate);
        Titre testTitre = titres.get(titres.size() - 1);
        assertThat(testTitre.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testTitre.getSymbole()).isEqualTo(UPDATED_SYMBOLE);

        // Validate the Titre in ElasticSearch
        Titre titreEs = titreSearchRepository.findOne(testTitre.getId());
        assertThat(titreEs).isEqualToComparingFieldByField(testTitre);
    }

    @Test
    @Transactional
    public void deleteTitre() throws Exception {
        // Initialize the database
        titreService.save(titre);

        int databaseSizeBeforeDelete = titreRepository.findAll().size();

        // Get the titre
        restTitreMockMvc.perform(delete("/api/titres/{id}", titre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean titreExistsInEs = titreSearchRepository.exists(titre.getId());
        assertThat(titreExistsInEs).isFalse();

        // Validate the database is empty
        List<Titre> titres = titreRepository.findAll();
        assertThat(titres).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTitre() throws Exception {
        // Initialize the database
        titreService.save(titre);

        // Search the titre
        restTitreMockMvc.perform(get("/api/_search/titres?query=id:" + titre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(titre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].symbole").value(hasItem(DEFAULT_SYMBOLE.toString())));
    }
}
