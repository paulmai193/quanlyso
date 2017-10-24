/*
 * 
 */
package logia.quanlyso.web.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

	/** The Constant DEFAULT_RATE. */
	private static final Float DEFAULT_RATE = 1F;

	/** The Constant UPDATED_RATE. */
	private static final Float UPDATED_RATE = 2F;

	/** The cost factor repository. */
	@Autowired
	private CostFactorRepository costFactorRepository;

	/** The cost factor mapper. */
	@Autowired
	private CostFactorMapper costFactorMapper;

	/** The cost factor service. */
	@Autowired
	private CostFactorService costFactorService;

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

	/** The rest cost factor mock mvc. */
	private MockMvc restCostFactorMockMvc;

	/** The cost factor. */
	private CostFactor costFactor;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		CostFactorResource costFactorResource = new CostFactorResource(this.costFactorService);
		this.restCostFactorMockMvc = MockMvcBuilders.standaloneSetup(costFactorResource)
				.setCustomArgumentResolvers(this.pageableArgumentResolver).setControllerAdvice(this.exceptionTranslator)
				.setMessageConverters(this.jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 *
	 * @param em
	 *            the em
	 * @return the cost factor
	 */
	public static CostFactor createEntity(EntityManager em) {
		CostFactor costFactor = new CostFactor().minRate(CostFactorResourceIntTest.DEFAULT_RATE);
		return costFactor;
	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.costFactor = CostFactorResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the cost factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createCostFactor() throws Exception {
		int databaseSizeBeforeCreate = this.costFactorRepository.findAll().size();

		// Create the CostFactor
		CostFactorDTO costFactorDTO = this.costFactorMapper.toDto(this.costFactor);
		this.restCostFactorMockMvc
				.perform(MockMvcRequestBuilders.post("/api/cost-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the CostFactor in the database
		List<CostFactor> costFactorList = this.costFactorRepository.findAll();
		Assertions.assertThat(costFactorList).hasSize(databaseSizeBeforeCreate + 1);
		CostFactor testCostFactor = costFactorList.get(costFactorList.size() - 1);
		Assertions.assertThat(testCostFactor.getMinRate()).isEqualTo(CostFactorResourceIntTest.DEFAULT_RATE);
	}

	/**
	 * Creates the cost factor with existing id.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createCostFactorWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.costFactorRepository.findAll().size();

		// Create the CostFactor with an existing ID
		this.costFactor.setId(1L);
		CostFactorDTO costFactorDTO = this.costFactorMapper.toDto(this.costFactor);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		this.restCostFactorMockMvc
				.perform(MockMvcRequestBuilders.post("/api/cost-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<CostFactor> costFactorList = this.costFactorRepository.findAll();
		Assertions.assertThat(costFactorList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all cost factors.
	 *
	 * @return the all cost factors
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllCostFactors() throws Exception {
		// Initialize the database
		this.costFactorRepository.saveAndFlush(this.costFactor);

		// Get all the costFactorList
		this.restCostFactorMockMvc.perform(MockMvcRequestBuilders.get("/api/cost-factors?sort=id,desc"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id")
						.value(Matchers.hasItem(this.costFactor.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].rate")
						.value(Matchers.hasItem(CostFactorResourceIntTest.DEFAULT_RATE.doubleValue())));
	}

	/**
	 * Gets the cost factor.
	 *
	 * @return the cost factor
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getCostFactor() throws Exception {
		// Initialize the database
		this.costFactorRepository.saveAndFlush(this.costFactor);

		// Get the costFactor
		this.restCostFactorMockMvc
				.perform(MockMvcRequestBuilders.get("/api/cost-factors/{id}", this.costFactor.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.costFactor.getId().intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rate")
						.value(CostFactorResourceIntTest.DEFAULT_RATE.doubleValue()));
	}

	/**
	 * Gets the non existing cost factor.
	 *
	 * @return the non existing cost factor
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getNonExistingCostFactor() throws Exception {
		// Get the costFactor
		this.restCostFactorMockMvc.perform(MockMvcRequestBuilders.get("/api/cost-factors/{id}", Long.MAX_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update cost factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateCostFactor() throws Exception {
		// Initialize the database
		this.costFactorRepository.saveAndFlush(this.costFactor);
		int databaseSizeBeforeUpdate = this.costFactorRepository.findAll().size();

		// Update the costFactor
		CostFactor updatedCostFactor = this.costFactorRepository.findOne(this.costFactor.getId());
		updatedCostFactor.minRate(CostFactorResourceIntTest.UPDATED_RATE);
		CostFactorDTO costFactorDTO = this.costFactorMapper.toDto(updatedCostFactor);

		this.restCostFactorMockMvc
				.perform(MockMvcRequestBuilders.put("/api/cost-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the CostFactor in the database
		List<CostFactor> costFactorList = this.costFactorRepository.findAll();
		Assertions.assertThat(costFactorList).hasSize(databaseSizeBeforeUpdate);
		CostFactor testCostFactor = costFactorList.get(costFactorList.size() - 1);
		Assertions.assertThat(testCostFactor.getMinRate()).isEqualTo(CostFactorResourceIntTest.UPDATED_RATE);
	}

	/**
	 * Update non existing cost factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingCostFactor() throws Exception {
		int databaseSizeBeforeUpdate = this.costFactorRepository.findAll().size();

		// Create the CostFactor
		CostFactorDTO costFactorDTO = this.costFactorMapper.toDto(this.costFactor);

		// If the entity doesn't have an ID, it will be created instead of just
		// being updated
		this.restCostFactorMockMvc
				.perform(MockMvcRequestBuilders.put("/api/cost-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(costFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the CostFactor in the database
		List<CostFactor> costFactorList = this.costFactorRepository.findAll();
		Assertions.assertThat(costFactorList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete cost factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void deleteCostFactor() throws Exception {
		// Initialize the database
		this.costFactorRepository.saveAndFlush(this.costFactor);
		int databaseSizeBeforeDelete = this.costFactorRepository.findAll().size();

		// Get the costFactor
		this.restCostFactorMockMvc.perform(MockMvcRequestBuilders
				.delete("/api/cost-factors/{id}", this.costFactor.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<CostFactor> costFactorList = this.costFactorRepository.findAll();
		Assertions.assertThat(costFactorList).hasSize(databaseSizeBeforeDelete - 1);
	}

	/**
	 * Equals verifier.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CostFactor.class);
		CostFactor costFactor1 = new CostFactor();
		costFactor1.setId(1L);
		CostFactor costFactor2 = new CostFactor();
		costFactor2.setId(costFactor1.getId());
		Assertions.assertThat(costFactor1).isEqualTo(costFactor2);
		costFactor2.setId(2L);
		Assertions.assertThat(costFactor1).isNotEqualTo(costFactor2);
		costFactor1.setId(null);
		Assertions.assertThat(costFactor1).isNotEqualTo(costFactor2);
	}

	/**
	 * Dto equals verifier.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(CostFactorDTO.class);
		CostFactorDTO costFactorDTO1 = new CostFactorDTO();
		costFactorDTO1.setId(1L);
		CostFactorDTO costFactorDTO2 = new CostFactorDTO();
		Assertions.assertThat(costFactorDTO1).isNotEqualTo(costFactorDTO2);
		costFactorDTO2.setId(costFactorDTO1.getId());
		Assertions.assertThat(costFactorDTO1).isEqualTo(costFactorDTO2);
		costFactorDTO2.setId(2L);
		Assertions.assertThat(costFactorDTO1).isNotEqualTo(costFactorDTO2);
		costFactorDTO1.setId(null);
		Assertions.assertThat(costFactorDTO1).isNotEqualTo(costFactorDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.costFactorMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.costFactorMapper.fromId(null)).isNull();
	}
}
