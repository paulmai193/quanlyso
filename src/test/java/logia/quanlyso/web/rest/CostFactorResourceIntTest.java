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
import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.repository.CostFactorRepository;
import logia.quanlyso.service.CostFactorService;
import logia.quanlyso.service.dto.CostFactorDTO;
import logia.quanlyso.service.mapper.CostFactorMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CostFactorResource REST controller.
 *
 * @see CostFactorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class CostFactorResourceIntTest {

    private static final Float DEFAULT_RATE = 1F;
    private static final Float UPDATED_RATE = 2F;

    @Autowired
    private CostFactorRepository costFactorRepository;

    @Autowired
    private CostFactorMapper costFactorMapper;

    @Autowired
    private CostFactorService costFactorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCostFactorMockMvc;

    private CostFactor costFactor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CostFactorResource costFactorResource = new CostFactorResource(costFactorService);
        this.restCostFactorMockMvc = MockMvcBuilders.standaloneSetup(costFactorResource)
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
    public static CostFactor createEntity(EntityManager em) {
        CostFactor costFactor = new CostFactor()
            .rate(DEFAULT_RATE);
        return costFactor;
    }

    @Before
    public void initTest() {
        costFactor = createEntity(em);
    }

    @Test
    @Transactional
    public void createCostFactor() throws Exception {
        int databaseSizeBeforeCreate = costFactorRepository.findAll().size();

        // Create the CostFactor
        CostFactorDTO costFactorDTO = costFactorMapper.toDto(costFactor);
        restCostFactorMockMvc.perform(post("/api/cost-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
            .andExpect(status().isCreated());

        // Validate the CostFactor in the database
        List<CostFactor> costFactorList = costFactorRepository.findAll();
        assertThat(costFactorList).hasSize(databaseSizeBeforeCreate + 1);
        CostFactor testCostFactor = costFactorList.get(costFactorList.size() - 1);
        assertThat(testCostFactor.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    public void createCostFactorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = costFactorRepository.findAll().size();

        // Create the CostFactor with an existing ID
        costFactor.setId(1L);
        CostFactorDTO costFactorDTO = costFactorMapper.toDto(costFactor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostFactorMockMvc.perform(post("/api/cost-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CostFactor> costFactorList = costFactorRepository.findAll();
        assertThat(costFactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCostFactors() throws Exception {
        // Initialize the database
        costFactorRepository.saveAndFlush(costFactor);

        // Get all the costFactorList
        restCostFactorMockMvc.perform(get("/api/cost-factors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costFactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())));
    }

    @Test
    @Transactional
    public void getCostFactor() throws Exception {
        // Initialize the database
        costFactorRepository.saveAndFlush(costFactor);

        // Get the costFactor
        restCostFactorMockMvc.perform(get("/api/cost-factors/{id}", costFactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(costFactor.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCostFactor() throws Exception {
        // Get the costFactor
        restCostFactorMockMvc.perform(get("/api/cost-factors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostFactor() throws Exception {
        // Initialize the database
        costFactorRepository.saveAndFlush(costFactor);
        int databaseSizeBeforeUpdate = costFactorRepository.findAll().size();

        // Update the costFactor
        CostFactor updatedCostFactor = costFactorRepository.findOne(costFactor.getId());
        updatedCostFactor
            .rate(UPDATED_RATE);
        CostFactorDTO costFactorDTO = costFactorMapper.toDto(updatedCostFactor);

        restCostFactorMockMvc.perform(put("/api/cost-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
            .andExpect(status().isOk());

        // Validate the CostFactor in the database
        List<CostFactor> costFactorList = costFactorRepository.findAll();
        assertThat(costFactorList).hasSize(databaseSizeBeforeUpdate);
        CostFactor testCostFactor = costFactorList.get(costFactorList.size() - 1);
        assertThat(testCostFactor.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCostFactor() throws Exception {
        int databaseSizeBeforeUpdate = costFactorRepository.findAll().size();

        // Create the CostFactor
        CostFactorDTO costFactorDTO = costFactorMapper.toDto(costFactor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCostFactorMockMvc.perform(put("/api/cost-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
            .andExpect(status().isCreated());

        // Validate the CostFactor in the database
        List<CostFactor> costFactorList = costFactorRepository.findAll();
        assertThat(costFactorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCostFactor() throws Exception {
        // Initialize the database
        costFactorRepository.saveAndFlush(costFactor);
        int databaseSizeBeforeDelete = costFactorRepository.findAll().size();

        // Get the costFactor
        restCostFactorMockMvc.perform(delete("/api/cost-factors/{id}", costFactor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CostFactor> costFactorList = costFactorRepository.findAll();
        assertThat(costFactorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostFactor.class);
        CostFactor costFactor1 = new CostFactor();
        costFactor1.setId(1L);
        CostFactor costFactor2 = new CostFactor();
        costFactor2.setId(costFactor1.getId());
        assertThat(costFactor1).isEqualTo(costFactor2);
        costFactor2.setId(2L);
        assertThat(costFactor1).isNotEqualTo(costFactor2);
        costFactor1.setId(null);
        assertThat(costFactor1).isNotEqualTo(costFactor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostFactorDTO.class);
        CostFactorDTO costFactorDTO1 = new CostFactorDTO();
        costFactorDTO1.setId(1L);
        CostFactorDTO costFactorDTO2 = new CostFactorDTO();
        assertThat(costFactorDTO1).isNotEqualTo(costFactorDTO2);
        costFactorDTO2.setId(costFactorDTO1.getId());
        assertThat(costFactorDTO1).isEqualTo(costFactorDTO2);
        costFactorDTO2.setId(2L);
        assertThat(costFactorDTO1).isNotEqualTo(costFactorDTO2);
        costFactorDTO1.setId(null);
        assertThat(costFactorDTO1).isNotEqualTo(costFactorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(costFactorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(costFactorMapper.fromId(null)).isNull();
    }
}
