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
import logia.quanlyso.domain.Factor;
import logia.quanlyso.repository.FactorRepository;
import logia.quanlyso.service.FactorService;
import logia.quanlyso.service.dto.FactorDTO;
import logia.quanlyso.service.mapper.FactorMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the FactorResource REST controller.
 *
 * @see FactorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class FactorResourceIntTest {

    /** The Constant DEFAULT_NAME. */
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    
    /** The Constant UPDATED_NAME. */
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    /** The factor repository. */
    @Autowired
    private FactorRepository factorRepository;

    /** The factor mapper. */
    @Autowired
    private FactorMapper factorMapper;

    /** The factor service. */
    @Autowired
    private FactorService factorService;

    /** The jackson message converter. */
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    /** The pageable argument resolver. */
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    /** The exception translator. */
    @Autowired
    private ExceptionTranslator exceptionTranslator;

    /** The em. */
    @Autowired
    private EntityManager em;

    /** The rest factor mock mvc. */
    private MockMvc restFactorMockMvc;

    /** The factor. */
    private Factor factor;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FactorResource factorResource = new FactorResource(factorService);
        this.restFactorMockMvc = MockMvcBuilders.standaloneSetup(factorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * 
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *
     * @param em the em
     * @return the factor
     */
    public static Factor createEntity(EntityManager em) {
        Factor factor = new Factor()
            .name(DEFAULT_NAME);
        return factor;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        factor = createEntity(em);
    }

    /**
     * Creates the factor.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createFactor() throws Exception {
        int databaseSizeBeforeCreate = factorRepository.findAll().size();

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);
        restFactorMockMvc.perform(post("/api/factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isCreated());

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeCreate + 1);
        Factor testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getName()).isEqualTo(DEFAULT_NAME);
    }

    /**
     * Creates the factor with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createFactorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factorRepository.findAll().size();

        // Create the Factor with an existing ID
        factor.setId(1L);
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactorMockMvc.perform(post("/api/factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Factor> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all factors.
     *
     * @return the all factors
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAllFactors() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factor);

        // Get all the factorList
        restFactorMockMvc.perform(get("/api/factors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Gets the factor.
     *
     * @return the factor
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getFactor() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factor);

        // Get the factor
        restFactorMockMvc.perform(get("/api/factors/{id}", factor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(factor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    /**
     * Gets the non existing factor.
     *
     * @return the non existing factor
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getNonExistingFactor() throws Exception {
        // Get the factor
        restFactorMockMvc.perform(get("/api/factors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    /**
     * Update factor.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateFactor() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factor);
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();

        // Update the factor
        Factor updatedFactor = factorRepository.findOne(factor.getId());
        updatedFactor
            .name(UPDATED_NAME);
        FactorDTO factorDTO = factorMapper.toDto(updatedFactor);

        restFactorMockMvc.perform(put("/api/factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isOk());

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        Factor testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getName()).isEqualTo(UPDATED_NAME);
    }

    /**
     * Update non existing factor.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFactorMockMvc.perform(put("/api/factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isCreated());

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete factor.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void deleteFactor() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factor);
        int databaseSizeBeforeDelete = factorRepository.findAll().size();

        // Get the factor
        restFactorMockMvc.perform(delete("/api/factors/{id}", factor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Factor> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factor.class);
        Factor factor1 = new Factor();
        factor1.setId(1L);
        Factor factor2 = new Factor();
        factor2.setId(factor1.getId());
        assertThat(factor1).isEqualTo(factor2);
        factor2.setId(2L);
        assertThat(factor1).isNotEqualTo(factor2);
        factor1.setId(null);
        assertThat(factor1).isNotEqualTo(factor2);
    }

    /**
     * Dto equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorDTO.class);
        FactorDTO factorDTO1 = new FactorDTO();
        factorDTO1.setId(1L);
        FactorDTO factorDTO2 = new FactorDTO();
        assertThat(factorDTO1).isNotEqualTo(factorDTO2);
        factorDTO2.setId(factorDTO1.getId());
        assertThat(factorDTO1).isEqualTo(factorDTO2);
        factorDTO2.setId(2L);
        assertThat(factorDTO1).isNotEqualTo(factorDTO2);
        factorDTO1.setId(null);
        assertThat(factorDTO1).isNotEqualTo(factorDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(factorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(factorMapper.fromId(null)).isNull();
    }
}
