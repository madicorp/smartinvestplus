package net.madicorp.smartinvestplus.web.rest;

import net.madicorp.smartinvestplus.SmartinvestplusApp;
import net.madicorp.smartinvestplus.domain.Cloture;
import net.madicorp.smartinvestplus.repository.ClotureRepository;
import net.madicorp.smartinvestplus.service.ClotureService;
import net.madicorp.smartinvestplus.repository.search.ClotureSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ClotureResource REST controller.
 *
 * @see ClotureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartinvestplusApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClotureResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;

    private static final Integer DEFAULT_VALEUR = 1;
    private static final Integer UPDATED_VALEUR = 2;

    private static final Integer DEFAULT_TRANSACTIONS = 1;
    private static final Integer UPDATED_TRANSACTIONS = 2;

    private static final Double DEFAULT_COURS = 1D;
    private static final Double UPDATED_COURS = 2D;

    @Inject
    private ClotureRepository clotureRepository;

    @Inject
    private ClotureService clotureService;

    @Inject
    private ClotureSearchRepository clotureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClotureMockMvc;

    private Cloture cloture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClotureResource clotureResource = new ClotureResource();
        ReflectionTestUtils.setField(clotureResource, "clotureService", clotureService);
        this.restClotureMockMvc = MockMvcBuilders.standaloneSetup(clotureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clotureSearchRepository.deleteAll();
        cloture = new Cloture();
        cloture.setDate(DEFAULT_DATE);
        cloture.setVolume(DEFAULT_VOLUME);
        cloture.setValeur(DEFAULT_VALEUR);
        cloture.setTransactions(DEFAULT_TRANSACTIONS);
        cloture.setCours(DEFAULT_COURS);
    }

    @Test
    @Transactional
    public void createCloture() throws Exception {
        int databaseSizeBeforeCreate = clotureRepository.findAll().size();

        // Create the Cloture

        restClotureMockMvc.perform(post("/api/clotures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cloture)))
                .andExpect(status().isCreated());

        // Validate the Cloture in the database
        List<Cloture> clotures = clotureRepository.findAll();
        assertThat(clotures).hasSize(databaseSizeBeforeCreate + 1);
        Cloture testCloture = clotures.get(clotures.size() - 1);
        assertThat(testCloture.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCloture.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testCloture.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testCloture.getTransactions()).isEqualTo(DEFAULT_TRANSACTIONS);
        assertThat(testCloture.getCours()).isEqualTo(DEFAULT_COURS);

        // Validate the Cloture in ElasticSearch
        Cloture clotureEs = clotureSearchRepository.findOne(testCloture.getId());
        assertThat(clotureEs).isEqualToComparingFieldByField(testCloture);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = clotureRepository.findAll().size();
        // set the field null
        cloture.setDate(null);

        // Create the Cloture, which fails.

        restClotureMockMvc.perform(post("/api/clotures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cloture)))
                .andExpect(status().isBadRequest());

        List<Cloture> clotures = clotureRepository.findAll();
        assertThat(clotures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = clotureRepository.findAll().size();
        // set the field null
        cloture.setCours(null);

        // Create the Cloture, which fails.

        restClotureMockMvc.perform(post("/api/clotures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cloture)))
                .andExpect(status().isBadRequest());

        List<Cloture> clotures = clotureRepository.findAll();
        assertThat(clotures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClotures() throws Exception {
        // Initialize the database
        clotureRepository.saveAndFlush(cloture);

        // Get all the clotures
        restClotureMockMvc.perform(get("/api/clotures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cloture.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)))
                .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
                .andExpect(jsonPath("$.[*].transactions").value(hasItem(DEFAULT_TRANSACTIONS)))
                .andExpect(jsonPath("$.[*].cours").value(hasItem(DEFAULT_COURS.doubleValue())));
    }

    @Test
    @Transactional
    public void getCloture() throws Exception {
        // Initialize the database
        clotureRepository.saveAndFlush(cloture);

        // Get the cloture
        restClotureMockMvc.perform(get("/api/clotures/{id}", cloture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cloture.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.transactions").value(DEFAULT_TRANSACTIONS))
            .andExpect(jsonPath("$.cours").value(DEFAULT_COURS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCloture() throws Exception {
        // Get the cloture
        restClotureMockMvc.perform(get("/api/clotures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCloture() throws Exception {
        // Initialize the database
        clotureService.save(cloture);

        int databaseSizeBeforeUpdate = clotureRepository.findAll().size();

        // Update the cloture
        Cloture updatedCloture = new Cloture();
        updatedCloture.setId(cloture.getId());
        updatedCloture.setDate(UPDATED_DATE);
        updatedCloture.setVolume(UPDATED_VOLUME);
        updatedCloture.setValeur(UPDATED_VALEUR);
        updatedCloture.setTransactions(UPDATED_TRANSACTIONS);
        updatedCloture.setCours(UPDATED_COURS);

        restClotureMockMvc.perform(put("/api/clotures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCloture)))
                .andExpect(status().isOk());

        // Validate the Cloture in the database
        List<Cloture> clotures = clotureRepository.findAll();
        assertThat(clotures).hasSize(databaseSizeBeforeUpdate);
        Cloture testCloture = clotures.get(clotures.size() - 1);
        assertThat(testCloture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCloture.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testCloture.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testCloture.getTransactions()).isEqualTo(UPDATED_TRANSACTIONS);
        assertThat(testCloture.getCours()).isEqualTo(UPDATED_COURS);

        // Validate the Cloture in ElasticSearch
        Cloture clotureEs = clotureSearchRepository.findOne(testCloture.getId());
        assertThat(clotureEs).isEqualToComparingFieldByField(testCloture);
    }

    @Test
    @Transactional
    public void deleteCloture() throws Exception {
        // Initialize the database
        clotureService.save(cloture);

        int databaseSizeBeforeDelete = clotureRepository.findAll().size();

        // Get the cloture
        restClotureMockMvc.perform(delete("/api/clotures/{id}", cloture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clotureExistsInEs = clotureSearchRepository.exists(cloture.getId());
        assertThat(clotureExistsInEs).isFalse();

        // Validate the database is empty
        List<Cloture> clotures = clotureRepository.findAll();
        assertThat(clotures).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCloture() throws Exception {
        // Initialize the database
        clotureService.save(cloture);

        // Search the cloture
        restClotureMockMvc.perform(get("/api/_search/clotures?query=id:" + cloture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cloture.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].transactions").value(hasItem(DEFAULT_TRANSACTIONS)))
            .andExpect(jsonPath("$.[*].cours").value(hasItem(DEFAULT_COURS.doubleValue())));
    }
}
