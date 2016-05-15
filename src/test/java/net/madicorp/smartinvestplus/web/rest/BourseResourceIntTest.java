package net.madicorp.smartinvestplus.web.rest;

import net.madicorp.smartinvestplus.SmartinvestplusApp;
import net.madicorp.smartinvestplus.domain.Bourse;
import net.madicorp.smartinvestplus.repository.BourseRepository;
import net.madicorp.smartinvestplus.service.BourseService;
import net.madicorp.smartinvestplus.repository.search.BourseSearchRepository;

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
 * Test class for the BourseResource REST controller.
 *
 * @see BourseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartinvestplusApp.class)
@WebAppConfiguration
@IntegrationTest
public class BourseResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_SYMBOLE = "AAAAA";
    private static final String UPDATED_SYMBOLE = "BBBBB";

    @Inject
    private BourseRepository bourseRepository;

    @Inject
    private BourseService bourseService;

    @Inject
    private BourseSearchRepository bourseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBourseMockMvc;

    private Bourse bourse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BourseResource bourseResource = new BourseResource();
        ReflectionTestUtils.setField(bourseResource, "bourseService", bourseService);
        this.restBourseMockMvc = MockMvcBuilders.standaloneSetup(bourseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bourseSearchRepository.deleteAll();
        bourse = new Bourse();
        bourse.setNom(DEFAULT_NOM);
        bourse.setSymbole(DEFAULT_SYMBOLE);
    }

    @Test
    @Transactional
    public void createBourse() throws Exception {
        int databaseSizeBeforeCreate = bourseRepository.findAll().size();

        // Create the Bourse

        restBourseMockMvc.perform(post("/api/bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bourse)))
                .andExpect(status().isCreated());

        // Validate the Bourse in the database
        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeCreate + 1);
        Bourse testBourse = bourses.get(bourses.size() - 1);
        assertThat(testBourse.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBourse.getSymbole()).isEqualTo(DEFAULT_SYMBOLE);

        // Validate the Bourse in ElasticSearch
        Bourse bourseEs = bourseSearchRepository.findOne(testBourse.getId());
        assertThat(bourseEs).isEqualToComparingFieldByField(testBourse);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = bourseRepository.findAll().size();
        // set the field null
        bourse.setNom(null);

        // Create the Bourse, which fails.

        restBourseMockMvc.perform(post("/api/bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bourse)))
                .andExpect(status().isBadRequest());

        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBourses() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);

        // Get all the bourses
        restBourseMockMvc.perform(get("/api/bourses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bourse.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].symbole").value(hasItem(DEFAULT_SYMBOLE.toString())));
    }

    @Test
    @Transactional
    public void getBourse() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);

        // Get the bourse
        restBourseMockMvc.perform(get("/api/bourses/{id}", bourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bourse.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.symbole").value(DEFAULT_SYMBOLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBourse() throws Exception {
        // Get the bourse
        restBourseMockMvc.perform(get("/api/bourses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBourse() throws Exception {
        // Initialize the database
        bourseService.save(bourse);

        int databaseSizeBeforeUpdate = bourseRepository.findAll().size();

        // Update the bourse
        Bourse updatedBourse = new Bourse();
        updatedBourse.setId(bourse.getId());
        updatedBourse.setNom(UPDATED_NOM);
        updatedBourse.setSymbole(UPDATED_SYMBOLE);

        restBourseMockMvc.perform(put("/api/bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBourse)))
                .andExpect(status().isOk());

        // Validate the Bourse in the database
        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeUpdate);
        Bourse testBourse = bourses.get(bourses.size() - 1);
        assertThat(testBourse.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBourse.getSymbole()).isEqualTo(UPDATED_SYMBOLE);

        // Validate the Bourse in ElasticSearch
        Bourse bourseEs = bourseSearchRepository.findOne(testBourse.getId());
        assertThat(bourseEs).isEqualToComparingFieldByField(testBourse);
    }

    @Test
    @Transactional
    public void deleteBourse() throws Exception {
        // Initialize the database
        bourseService.save(bourse);

        int databaseSizeBeforeDelete = bourseRepository.findAll().size();

        // Get the bourse
        restBourseMockMvc.perform(delete("/api/bourses/{id}", bourse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bourseExistsInEs = bourseSearchRepository.exists(bourse.getId());
        assertThat(bourseExistsInEs).isFalse();

        // Validate the database is empty
        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBourse() throws Exception {
        // Initialize the database
        bourseService.save(bourse);

        // Search the bourse
        restBourseMockMvc.perform(get("/api/_search/bourses?query=id:" + bourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].symbole").value(hasItem(DEFAULT_SYMBOLE.toString())));
    }
}
