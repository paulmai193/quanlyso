package logia.quanlyso.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.repository.ProfitFactorRepository;
import logia.quanlyso.service.ProfitFactorService;
import logia.quanlyso.service.dto.ProfitFactorDTO;
import logia.quanlyso.service.mapper.ProfitFactorMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ProfitFactorResource REST controller.
 *
 * @see ProfitFactorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class ProfitFactorResourceIntTest {

    private static final Float DEFAULT_RATE = 1F;
    private static final Float UPDATED_RATE = 2F;

    @Autowired
    private ProfitFactorRepository profitFactorRepository;

    @Autowired
    private ProfitFactorMapper profitFactorMapper;

    @Autowired
    private ProfitFactorService profitFactorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfitFactorMockMvc;

    private ProfitFactor profitFactor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfitFactorResource profitFactorResource = new ProfitFactorResource(profitFactorService);
        this.restProfitFactorMockMvc = MockMvcBuilders.standaloneSetup(profitFactorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfitFactor createEntity(EntityManager em) {
        ProfitFactor profitFactor = new ProfitFactor()
            .rate(DEFAULT_RATE);
        return profitFactor;
    }

    @Before
    public void initTest() {
        profitFactor = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfitFactor() throws Exception {
        int databaseSizeBeforeCreate = profitFactorRepository.findAll().size();

        // Create the ProfitFactor
        ProfitFactorDTO profitFactorDTO = profitFactorMapper.toDto(profitFactor);
        restProfitFactorMockMvc.perform(post("/api/profit-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfitFactor in the database
        List<ProfitFactor> profitFactorList = profitFactorRepository.findAll();
        assertThat(profitFactorList).hasSize(databaseSizeBeforeCreate + 1);
        ProfitFactor testProfitFactor = profitFactorList.get(profitFactorList.size() - 1);
        assertThat(testProfitFactor.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    public void createProfitFactorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profitFactorRepository.findAll().size();

        // Create the ProfitFactor with an existing ID
        profitFactor.setId(1L);
        ProfitFactorDTO profitFactorDTO = profitFactorMapper.toDto(profitFactor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfitFactorMockMvc.perform(post("/api/profit-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProfitFactor> profitFactorList = profitFactorRepository.findAll();
        assertThat(profitFactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProfitFactors() throws Exception {
        // Initialize the database
        profitFactorRepository.saveAndFlush(profitFactor);

        // Get all the profitFactorList
        restProfitFactorMockMvc.perform(get("/api/profit-factors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profitFactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())));
    }

    @Test
    @Transactional
    public void getProfitFactor() throws Exception {
        // Initialize the database
        profitFactorRepository.saveAndFlush(profitFactor);

        // Get the profitFactor
        restProfitFactorMockMvc.perform(get("/api/profit-factors/{id}", profitFactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profitFactor.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProfitFactor() throws Exception {
        // Get the profitFactor
        restProfitFactorMockMvc.perform(get("/api/profit-factors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfitFactor() throws Exception {
        // Initialize the database
        profitFactorRepository.saveAndFlush(profitFactor);
        int databaseSizeBeforeUpdate = profitFactorRepository.findAll().size();

        // Update the profitFactor
        ProfitFactor updatedProfitFactor = profitFactorRepository.findOne(profitFactor.getId());
        updatedProfitFactor
            .rate(UPDATED_RATE);
        ProfitFactorDTO profitFactorDTO = profitFactorMapper.toDto(updatedProfitFactor);

        restProfitFactorMockMvc.perform(put("/api/profit-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
            .andExpect(status().isOk());

        // Validate the ProfitFactor in the database
        List<ProfitFactor> profitFactorList = profitFactorRepository.findAll();
        assertThat(profitFactorList).hasSize(databaseSizeBeforeUpdate);
        ProfitFactor testProfitFactor = profitFactorList.get(profitFactorList.size() - 1);
        assertThat(testProfitFactor.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    public void updateNonExistingProfitFactor() throws Exception {
        int databaseSizeBeforeUpdate = profitFactorRepository.findAll().size();

        // Create the ProfitFactor
        ProfitFactorDTO profitFactorDTO = profitFactorMapper.toDto(profitFactor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProfitFactorMockMvc.perform(put("/api/profit-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfitFactor in the database
        List<ProfitFactor> profitFactorList = profitFactorRepository.findAll();
        assertThat(profitFactorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProfitFactor() throws Exception {
        // Initialize the database
        profitFactorRepository.saveAndFlush(profitFactor);
        int databaseSizeBeforeDelete = profitFactorRepository.findAll().size();

        // Get the profitFactor
        restProfitFactorMockMvc.perform(delete("/api/profit-factors/{id}", profitFactor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfitFactor> profitFactorList = profitFactorRepository.findAll();
        assertThat(profitFactorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfitFactor.class);
        ProfitFactor profitFactor1 = new ProfitFactor();
        profitFactor1.setId(1L);
        ProfitFactor profitFactor2 = new ProfitFactor();
        profitFactor2.setId(profitFactor1.getId());
        assertThat(profitFactor1).isEqualTo(profitFactor2);
        profitFactor2.setId(2L);
        assertThat(profitFactor1).isNotEqualTo(profitFactor2);
        profitFactor1.setId(null);
        assertThat(profitFactor1).isNotEqualTo(profitFactor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfitFactorDTO.class);
        ProfitFactorDTO profitFactorDTO1 = new ProfitFactorDTO();
        profitFactorDTO1.setId(1L);
        ProfitFactorDTO profitFactorDTO2 = new ProfitFactorDTO();
        assertThat(profitFactorDTO1).isNotEqualTo(profitFactorDTO2);
        profitFactorDTO2.setId(profitFactorDTO1.getId());
        assertThat(profitFactorDTO1).isEqualTo(profitFactorDTO2);
        profitFactorDTO2.setId(2L);
        assertThat(profitFactorDTO1).isNotEqualTo(profitFactorDTO2);
        profitFactorDTO1.setId(null);
        assertThat(profitFactorDTO1).isNotEqualTo(profitFactorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profitFactorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profitFactorMapper.fromId(null)).isNull();
    }
}
