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

	/** The Constant DEFAULT_RATE. */
	private static final Float DEFAULT_RATE = 1F;

	/** The Constant UPDATED_RATE. */
	private static final Float UPDATED_RATE = 2F;

	/** The profit factor repository. */
	@Autowired
	private ProfitFactorRepository profitFactorRepository;

	/** The profit factor mapper. */
	@Autowired
	private ProfitFactorMapper profitFactorMapper;

	/** The profit factor service. */
	@Autowired
	private ProfitFactorService profitFactorService;

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

	/** The rest profit factor mock mvc. */
	private MockMvc restProfitFactorMockMvc;

	/** The profit factor. */
	private ProfitFactor profitFactor;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ProfitFactorResource profitFactorResource = new ProfitFactorResource(this.profitFactorService);
		this.restProfitFactorMockMvc = MockMvcBuilders.standaloneSetup(profitFactorResource)
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
	 * @return the profit factor
	 */
	public static ProfitFactor createEntity(EntityManager em) {
		ProfitFactor profitFactor = new ProfitFactor().rate(ProfitFactorResourceIntTest.DEFAULT_RATE);
		return profitFactor;
	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.profitFactor = ProfitFactorResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the profit factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createProfitFactor() throws Exception {
		int databaseSizeBeforeCreate = this.profitFactorRepository.findAll().size();

		// Create the ProfitFactor
		ProfitFactorDTO profitFactorDTO = this.profitFactorMapper.toDto(this.profitFactor);
		this.restProfitFactorMockMvc
				.perform(MockMvcRequestBuilders.post("/api/profit-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the ProfitFactor in the database
		List<ProfitFactor> profitFactorList = this.profitFactorRepository.findAll();
		Assertions.assertThat(profitFactorList).hasSize(databaseSizeBeforeCreate + 1);
		ProfitFactor testProfitFactor = profitFactorList.get(profitFactorList.size() - 1);
		Assertions.assertThat(testProfitFactor.getRate()).isEqualTo(ProfitFactorResourceIntTest.DEFAULT_RATE);
	}

	/**
	 * Creates the profit factor with existing id.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createProfitFactorWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.profitFactorRepository.findAll().size();

		// Create the ProfitFactor with an existing ID
		this.profitFactor.setId(1L);
		ProfitFactorDTO profitFactorDTO = this.profitFactorMapper.toDto(this.profitFactor);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		this.restProfitFactorMockMvc
				.perform(MockMvcRequestBuilders.post("/api/profit-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<ProfitFactor> profitFactorList = this.profitFactorRepository.findAll();
		Assertions.assertThat(profitFactorList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all profit factors.
	 *
	 * @return the all profit factors
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllProfitFactors() throws Exception {
		// Initialize the database
		this.profitFactorRepository.saveAndFlush(this.profitFactor);

		// Get all the profitFactorList
		this.restProfitFactorMockMvc.perform(MockMvcRequestBuilders.get("/api/profit-factors?sort=id,desc"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id")
						.value(Matchers.hasItem(this.profitFactor.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].rate")
						.value(Matchers.hasItem(ProfitFactorResourceIntTest.DEFAULT_RATE.doubleValue())));
	}

	/**
	 * Gets the profit factor.
	 *
	 * @return the profit factor
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getProfitFactor() throws Exception {
		// Initialize the database
		this.profitFactorRepository.saveAndFlush(this.profitFactor);

		// Get the profitFactor
		this.restProfitFactorMockMvc
				.perform(MockMvcRequestBuilders.get("/api/profit-factors/{id}", this.profitFactor.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.profitFactor.getId().intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rate")
						.value(ProfitFactorResourceIntTest.DEFAULT_RATE.doubleValue()));
	}

	/**
	 * Gets the non existing profit factor.
	 *
	 * @return the non existing profit factor
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getNonExistingProfitFactor() throws Exception {
		// Get the profitFactor
		this.restProfitFactorMockMvc.perform(MockMvcRequestBuilders.get("/api/profit-factors/{id}", Long.MAX_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update profit factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateProfitFactor() throws Exception {
		// Initialize the database
		this.profitFactorRepository.saveAndFlush(this.profitFactor);
		int databaseSizeBeforeUpdate = this.profitFactorRepository.findAll().size();

		// Update the profitFactor
		ProfitFactor updatedProfitFactor = this.profitFactorRepository.findOne(this.profitFactor.getId());
		updatedProfitFactor.rate(ProfitFactorResourceIntTest.UPDATED_RATE);
		ProfitFactorDTO profitFactorDTO = this.profitFactorMapper.toDto(updatedProfitFactor);

		this.restProfitFactorMockMvc
				.perform(MockMvcRequestBuilders.put("/api/profit-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the ProfitFactor in the database
		List<ProfitFactor> profitFactorList = this.profitFactorRepository.findAll();
		Assertions.assertThat(profitFactorList).hasSize(databaseSizeBeforeUpdate);
		ProfitFactor testProfitFactor = profitFactorList.get(profitFactorList.size() - 1);
		Assertions.assertThat(testProfitFactor.getRate()).isEqualTo(ProfitFactorResourceIntTest.UPDATED_RATE);
	}

	/**
	 * Update non existing profit factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingProfitFactor() throws Exception {
		int databaseSizeBeforeUpdate = this.profitFactorRepository.findAll().size();

		// Create the ProfitFactor
		ProfitFactorDTO profitFactorDTO = this.profitFactorMapper.toDto(this.profitFactor);

		// If the entity doesn't have an ID, it will be created instead of just
		// being updated
		this.restProfitFactorMockMvc
				.perform(MockMvcRequestBuilders.put("/api/profit-factors").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(profitFactorDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the ProfitFactor in the database
		List<ProfitFactor> profitFactorList = this.profitFactorRepository.findAll();
		Assertions.assertThat(profitFactorList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete profit factor.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void deleteProfitFactor() throws Exception {
		// Initialize the database
		this.profitFactorRepository.saveAndFlush(this.profitFactor);
		int databaseSizeBeforeDelete = this.profitFactorRepository.findAll().size();

		// Get the profitFactor
		this.restProfitFactorMockMvc.perform(MockMvcRequestBuilders
				.delete("/api/profit-factors/{id}", this.profitFactor.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<ProfitFactor> profitFactorList = this.profitFactorRepository.findAll();
		Assertions.assertThat(profitFactorList).hasSize(databaseSizeBeforeDelete - 1);
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
		TestUtil.equalsVerifier(ProfitFactor.class);
		ProfitFactor profitFactor1 = new ProfitFactor();
		profitFactor1.setId(1L);
		ProfitFactor profitFactor2 = new ProfitFactor();
		profitFactor2.setId(profitFactor1.getId());
		Assertions.assertThat(profitFactor1).isEqualTo(profitFactor2);
		profitFactor2.setId(2L);
		Assertions.assertThat(profitFactor1).isNotEqualTo(profitFactor2);
		profitFactor1.setId(null);
		Assertions.assertThat(profitFactor1).isNotEqualTo(profitFactor2);
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
		TestUtil.equalsVerifier(ProfitFactorDTO.class);
		ProfitFactorDTO profitFactorDTO1 = new ProfitFactorDTO();
		profitFactorDTO1.setId(1L);
		ProfitFactorDTO profitFactorDTO2 = new ProfitFactorDTO();
		Assertions.assertThat(profitFactorDTO1).isNotEqualTo(profitFactorDTO2);
		profitFactorDTO2.setId(profitFactorDTO1.getId());
		Assertions.assertThat(profitFactorDTO1).isEqualTo(profitFactorDTO2);
		profitFactorDTO2.setId(2L);
		Assertions.assertThat(profitFactorDTO1).isNotEqualTo(profitFactorDTO2);
		profitFactorDTO1.setId(null);
		Assertions.assertThat(profitFactorDTO1).isNotEqualTo(profitFactorDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.profitFactorMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.profitFactorMapper.fromId(null)).isNull();
	}
}
