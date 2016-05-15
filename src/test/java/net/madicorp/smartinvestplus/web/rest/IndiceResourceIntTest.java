package net.madicorp.smartinvestplus.web.rest;

import net.madicorp.smartinvestplus.SmartinvestplusApp;
import net.madicorp.smartinvestplus.domain.Indice;
import net.madicorp.smartinvestplus.repository.IndiceRepository;
import net.madicorp.smartinvestplus.service.IndiceService;
import net.madicorp.smartinvestplus.repository.search.IndiceSearchRepository;

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
 * Test class for the IndiceResource REST controller.
 *
 * @see IndiceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartinvestplusApp.class)
@WebAppConfiguration
@IntegrationTest
public class IndiceResourceIntTest {


    private static final Double DEFAULT_TOP = 1D;
    private static final Double UPDATED_TOP = 2D;

    private static final Double DEFAULT_COMPOSITE = 1D;
    private static final Double UPDATED_COMPOSITE = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private IndiceRepository indiceRepository;

    @Inject
    private IndiceService indiceService;

    @Inject
    private IndiceSearchRepository indiceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIndiceMockMvc;

    private Indice indice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IndiceResource indiceResource = new IndiceResource();
        ReflectionTestUtils.setField(indiceResource, "indiceService", indiceService);
        this.restIndiceMockMvc = MockMvcBuilders.standaloneSetup(indiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        indiceSearchRepository.deleteAll();
        indice = new Indice();
        indice.setTop(DEFAULT_TOP);
        indice.setComposite(DEFAULT_COMPOSITE);
        indice.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createIndice() throws Exception {
        int databaseSizeBeforeCreate = indiceRepository.findAll().size();

        // Create the Indice

        restIndiceMockMvc.perform(post("/api/indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(indice)))
                .andExpect(status().isCreated());

        // Validate the Indice in the database
        List<Indice> indices = indiceRepository.findAll();
        assertThat(indices).hasSize(databaseSizeBeforeCreate + 1);
        Indice testIndice = indices.get(indices.size() - 1);
        assertThat(testIndice.getTop()).isEqualTo(DEFAULT_TOP);
        assertThat(testIndice.getComposite()).isEqualTo(DEFAULT_COMPOSITE);
        assertThat(testIndice.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Indice in ElasticSearch
        Indice indiceEs = indiceSearchRepository.findOne(testIndice.getId());
        assertThat(indiceEs).isEqualToComparingFieldByField(testIndice);
    }

    @Test
    @Transactional
    public void checkTopIsRequired() throws Exception {
        int databaseSizeBeforeTest = indiceRepository.findAll().size();
        // set the field null
        indice.setTop(null);

        // Create the Indice, which fails.

        restIndiceMockMvc.perform(post("/api/indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(indice)))
                .andExpect(status().isBadRequest());

        List<Indice> indices = indiceRepository.findAll();
        assertThat(indices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompositeIsRequired() throws Exception {
        int databaseSizeBeforeTest = indiceRepository.findAll().size();
        // set the field null
        indice.setComposite(null);

        // Create the Indice, which fails.

        restIndiceMockMvc.perform(post("/api/indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(indice)))
                .andExpect(status().isBadRequest());

        List<Indice> indices = indiceRepository.findAll();
        assertThat(indices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = indiceRepository.findAll().size();
        // set the field null
        indice.setDate(null);

        // Create the Indice, which fails.

        restIndiceMockMvc.perform(post("/api/indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(indice)))
                .andExpect(status().isBadRequest());

        List<Indice> indices = indiceRepository.findAll();
        assertThat(indices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIndices() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indices
        restIndiceMockMvc.perform(get("/api/indices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(indice.getId().intValue())))
                .andExpect(jsonPath("$.[*].top").value(hasItem(DEFAULT_TOP.doubleValue())))
                .andExpect(jsonPath("$.[*].composite").value(hasItem(DEFAULT_COMPOSITE.doubleValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getIndice() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get the indice
        restIndiceMockMvc.perform(get("/api/indices/{id}", indice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(indice.getId().intValue()))
            .andExpect(jsonPath("$.top").value(DEFAULT_TOP.doubleValue()))
            .andExpect(jsonPath("$.composite").value(DEFAULT_COMPOSITE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIndice() throws Exception {
        // Get the indice
        restIndiceMockMvc.perform(get("/api/indices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndice() throws Exception {
        // Initialize the database
        indiceService.save(indice);

        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();

        // Update the indice
        Indice updatedIndice = new Indice();
        updatedIndice.setId(indice.getId());
        updatedIndice.setTop(UPDATED_TOP);
        updatedIndice.setComposite(UPDATED_COMPOSITE);
        updatedIndice.setDate(UPDATED_DATE);

        restIndiceMockMvc.perform(put("/api/indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedIndice)))
                .andExpect(status().isOk());

        // Validate the Indice in the database
        List<Indice> indices = indiceRepository.findAll();
        assertThat(indices).hasSize(databaseSizeBeforeUpdate);
        Indice testIndice = indices.get(indices.size() - 1);
        assertThat(testIndice.getTop()).isEqualTo(UPDATED_TOP);
        assertThat(testIndice.getComposite()).isEqualTo(UPDATED_COMPOSITE);
        assertThat(testIndice.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Indice in ElasticSearch
        Indice indiceEs = indiceSearchRepository.findOne(testIndice.getId());
        assertThat(indiceEs).isEqualToComparingFieldByField(testIndice);
    }

    @Test
    @Transactional
    public void deleteIndice() throws Exception {
        // Initialize the database
        indiceService.save(indice);

        int databaseSizeBeforeDelete = indiceRepository.findAll().size();

        // Get the indice
        restIndiceMockMvc.perform(delete("/api/indices/{id}", indice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean indiceExistsInEs = indiceSearchRepository.exists(indice.getId());
        assertThat(indiceExistsInEs).isFalse();

        // Validate the database is empty
        List<Indice> indices = indiceRepository.findAll();
        assertThat(indices).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndice() throws Exception {
        // Initialize the database
        indiceService.save(indice);

        // Search the indice
        restIndiceMockMvc.perform(get("/api/_search/indices?query=id:" + indice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indice.getId().intValue())))
            .andExpect(jsonPath("$.[*].top").value(hasItem(DEFAULT_TOP.doubleValue())))
            .andExpect(jsonPath("$.[*].composite").value(hasItem(DEFAULT_COMPOSITE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}
