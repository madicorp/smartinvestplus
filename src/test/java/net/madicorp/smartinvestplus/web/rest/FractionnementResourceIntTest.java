package net.madicorp.smartinvestplus.web.rest;

import net.madicorp.smartinvestplus.SmartinvestplusApp;
import net.madicorp.smartinvestplus.domain.Fractionnement;
import net.madicorp.smartinvestplus.repository.FractionnementRepository;
import net.madicorp.smartinvestplus.service.FractionnementService;
import net.madicorp.smartinvestplus.repository.search.FractionnementSearchRepository;

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
 * Test class for the FractionnementResource REST controller.
 *
 * @see FractionnementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartinvestplusApp.class)
@WebAppConfiguration
@IntegrationTest
public class FractionnementResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VALEUR = 1;
    private static final Integer UPDATED_VALEUR = 2;

    @Inject
    private FractionnementRepository fractionnementRepository;

    @Inject
    private FractionnementService fractionnementService;

    @Inject
    private FractionnementSearchRepository fractionnementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFractionnementMockMvc;

    private Fractionnement fractionnement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FractionnementResource fractionnementResource = new FractionnementResource();
        ReflectionTestUtils.setField(fractionnementResource, "fractionnementService", fractionnementService);
        this.restFractionnementMockMvc = MockMvcBuilders.standaloneSetup(fractionnementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fractionnementSearchRepository.deleteAll();
        fractionnement = new Fractionnement();
        fractionnement.setDate(DEFAULT_DATE);
        fractionnement.setValeur(DEFAULT_VALEUR);
    }

    @Test
    @Transactional
    public void createFractionnement() throws Exception {
        int databaseSizeBeforeCreate = fractionnementRepository.findAll().size();

        // Create the Fractionnement

        restFractionnementMockMvc.perform(post("/api/fractionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fractionnement)))
                .andExpect(status().isCreated());

        // Validate the Fractionnement in the database
        List<Fractionnement> fractionnements = fractionnementRepository.findAll();
        assertThat(fractionnements).hasSize(databaseSizeBeforeCreate + 1);
        Fractionnement testFractionnement = fractionnements.get(fractionnements.size() - 1);
        assertThat(testFractionnement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFractionnement.getValeur()).isEqualTo(DEFAULT_VALEUR);

        // Validate the Fractionnement in ElasticSearch
        Fractionnement fractionnementEs = fractionnementSearchRepository.findOne(testFractionnement.getId());
        assertThat(fractionnementEs).isEqualToComparingFieldByField(testFractionnement);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fractionnementRepository.findAll().size();
        // set the field null
        fractionnement.setDate(null);

        // Create the Fractionnement, which fails.

        restFractionnementMockMvc.perform(post("/api/fractionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fractionnement)))
                .andExpect(status().isBadRequest());

        List<Fractionnement> fractionnements = fractionnementRepository.findAll();
        assertThat(fractionnements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = fractionnementRepository.findAll().size();
        // set the field null
        fractionnement.setValeur(null);

        // Create the Fractionnement, which fails.

        restFractionnementMockMvc.perform(post("/api/fractionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fractionnement)))
                .andExpect(status().isBadRequest());

        List<Fractionnement> fractionnements = fractionnementRepository.findAll();
        assertThat(fractionnements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFractionnements() throws Exception {
        // Initialize the database
        fractionnementRepository.saveAndFlush(fractionnement);

        // Get all the fractionnements
        restFractionnementMockMvc.perform(get("/api/fractionnements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fractionnement.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)));
    }

    @Test
    @Transactional
    public void getFractionnement() throws Exception {
        // Initialize the database
        fractionnementRepository.saveAndFlush(fractionnement);

        // Get the fractionnement
        restFractionnementMockMvc.perform(get("/api/fractionnements/{id}", fractionnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fractionnement.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR));
    }

    @Test
    @Transactional
    public void getNonExistingFractionnement() throws Exception {
        // Get the fractionnement
        restFractionnementMockMvc.perform(get("/api/fractionnements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFractionnement() throws Exception {
        // Initialize the database
        fractionnementService.save(fractionnement);

        int databaseSizeBeforeUpdate = fractionnementRepository.findAll().size();

        // Update the fractionnement
        Fractionnement updatedFractionnement = new Fractionnement();
        updatedFractionnement.setId(fractionnement.getId());
        updatedFractionnement.setDate(UPDATED_DATE);
        updatedFractionnement.setValeur(UPDATED_VALEUR);

        restFractionnementMockMvc.perform(put("/api/fractionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFractionnement)))
                .andExpect(status().isOk());

        // Validate the Fractionnement in the database
        List<Fractionnement> fractionnements = fractionnementRepository.findAll();
        assertThat(fractionnements).hasSize(databaseSizeBeforeUpdate);
        Fractionnement testFractionnement = fractionnements.get(fractionnements.size() - 1);
        assertThat(testFractionnement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFractionnement.getValeur()).isEqualTo(UPDATED_VALEUR);

        // Validate the Fractionnement in ElasticSearch
        Fractionnement fractionnementEs = fractionnementSearchRepository.findOne(testFractionnement.getId());
        assertThat(fractionnementEs).isEqualToComparingFieldByField(testFractionnement);
    }

    @Test
    @Transactional
    public void deleteFractionnement() throws Exception {
        // Initialize the database
        fractionnementService.save(fractionnement);

        int databaseSizeBeforeDelete = fractionnementRepository.findAll().size();

        // Get the fractionnement
        restFractionnementMockMvc.perform(delete("/api/fractionnements/{id}", fractionnement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fractionnementExistsInEs = fractionnementSearchRepository.exists(fractionnement.getId());
        assertThat(fractionnementExistsInEs).isFalse();

        // Validate the database is empty
        List<Fractionnement> fractionnements = fractionnementRepository.findAll();
        assertThat(fractionnements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFractionnement() throws Exception {
        // Initialize the database
        fractionnementService.save(fractionnement);

        // Search the fractionnement
        restFractionnementMockMvc.perform(get("/api/_search/fractionnements?query=id:" + fractionnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fractionnement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)));
    }
}
