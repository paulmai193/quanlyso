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
import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.repository.TransactionDetailsRepository;
import logia.quanlyso.service.TransactionDetailsService;
import logia.quanlyso.service.dto.TransactionDetailsDTO;
import logia.quanlyso.service.mapper.TransactionDetailsMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the TransactionDetailsResource REST controller.
 *
 * @see TransactionDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class TransactionDetailsResourceIntTest {

	/** The Constant DEFAULT_AMOUNT. */
	static final Float								DEFAULT_AMOUNT	= 1F;

	/** The Constant UPDATED_AMOUNT. */
	static final Float								UPDATED_AMOUNT	= 2F;

	// /** The Constant DEFAULT_PROFIT. */
	// static final Float DEFAULT_PROFIT = 1F;
	//
	// /** The Constant UPDATED_PROFIT. */
	// static final Float UPDATED_PROFIT = 2F;
	//
	// /** The Constant DEFAULT_COSTS. */
	// static final Float DEFAULT_COSTS = 1F;
	//
	// /** The Constant UPDATED_COSTS. */
	// static final Float UPDATED_COSTS = 2F;

	/** The transaction details repository. */
	@Autowired
	private TransactionDetailsRepository			transactionDetailsRepository;

	/** The transaction details mapper. */
	@Autowired
	private TransactionDetailsMapper				transactionDetailsMapper;

	/** The transaction details service. */
	@Autowired
	private TransactionDetailsService				transactionDetailsService;

	/** The jackson message converter. */
	@Autowired
	private MappingJackson2HttpMessageConverter		jacksonMessageConverter;

	/** The pageable argument resolver. */
	@Autowired
	private PageableHandlerMethodArgumentResolver	pageableArgumentResolver;

	/** The exception translator. */
	@Autowired
	private ExceptionTranslator						exceptionTranslator;

	/** The em. */
	@Autowired
	private EntityManager							em;

	/** The rest transaction details mock mvc. */
	private MockMvc									restTransactionDetailsMockMvc;

	/** The transaction details. */
	private TransactionDetails						transactionDetails;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TransactionDetailsResource transactionDetailsResource = new TransactionDetailsResource(
				this.transactionDetailsService);
		this.restTransactionDetailsMockMvc = MockMvcBuilders
				.standaloneSetup(transactionDetailsResource)
				.setCustomArgumentResolvers(this.pageableArgumentResolver)
				.setControllerAdvice(this.exceptionTranslator)
				.setMessageConverters(this.jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 * 
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 *
	 * @param em the em
	 * @return the transaction details
	 */
	public static TransactionDetails createEntity(EntityManager em) {
		TransactionDetails transactionDetails = new TransactionDetails().amount(TransactionDetailsResourceIntTest.DEFAULT_AMOUNT);
		return transactionDetails;
	}

	// /**
	// * Creates the and save entity.
	// *
	// * @param em the em
	// * @return the transaction details
	// */
	// public static TransactionDetails createAndSaveEntity(EntityManager em) {
	// TransactionDetails transactionDetails = createEntity(em);
	// em.persist(transactionDetails);
	// em.flush();
	// return transactionDetails;
	// }

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.transactionDetails = TransactionDetailsResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the transaction details.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createTransactionDetails() throws Exception {
		int databaseSizeBeforeCreate = this.transactionDetailsRepository.findAll().size();

		// Create the TransactionDetails
		TransactionDetailsDTO transactionDetailsDTO = this.transactionDetailsMapper
				.toDto(this.transactionDetails);
		this.restTransactionDetailsMockMvc
		.perform(
				MockMvcRequestBuilders.post("/api/transaction-details").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the TransactionDetails in the database
		List<TransactionDetails> transactionDetailsList = this.transactionDetailsRepository.findAll();
		Assertions.assertThat(transactionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
		TransactionDetails testTransactionDetails = transactionDetailsList
				.get(transactionDetailsList.size() - 1);
		Assertions.assertThat(testTransactionDetails.getAmount()).isEqualTo(TransactionDetailsResourceIntTest.DEFAULT_AMOUNT);
	}

	/**
	 * Creates the transaction details with existing id.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createTransactionDetailsWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.transactionDetailsRepository.findAll().size();

		// Create the TransactionDetails with an existing ID
		this.transactionDetails.setId(1L);
		TransactionDetailsDTO transactionDetailsDTO = this.transactionDetailsMapper
				.toDto(this.transactionDetails);

		// An entity with an existing ID cannot be created, so this API call must fail
		this.restTransactionDetailsMockMvc
		.perform(
				MockMvcRequestBuilders.post("/api/transaction-details").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<TransactionDetails> transactionDetailsList = this.transactionDetailsRepository.findAll();
		Assertions.assertThat(transactionDetailsList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all transaction details.
	 *
	 * @return the all transaction details
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getAllTransactionDetails() throws Exception {
		// Initialize the database
		this.transactionDetailsRepository.saveAndFlush(this.transactionDetails);

		// Get all the transactionDetailsList
		this.restTransactionDetailsMockMvc.perform(MockMvcRequestBuilders.get("/api/transaction-details?sort=id,desc"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(
				MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.transactionDetails.getId().intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].amount").value(Matchers.hasItem(TransactionDetailsResourceIntTest.DEFAULT_AMOUNT.doubleValue())));
	}

	/**
	 * Gets the transaction details.
	 *
	 * @return the transaction details
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getTransactionDetails() throws Exception {
		// Initialize the database
		this.transactionDetailsRepository.saveAndFlush(this.transactionDetails);

		// Get the transactionDetails
		this.restTransactionDetailsMockMvc
		.perform(MockMvcRequestBuilders.get("/api/transaction-details/{id}", this.transactionDetails.getId()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.transactionDetails.getId().intValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(TransactionDetailsResourceIntTest.DEFAULT_AMOUNT.doubleValue()));
	}

	/**
	 * Gets the non existing transaction details.
	 *
	 * @return the non existing transaction details
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getNonExistingTransactionDetails() throws Exception {
		// Get the transactionDetails
		this.restTransactionDetailsMockMvc.perform(MockMvcRequestBuilders.get("/api/transaction-details/{id}", Long.MAX_VALUE))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update transaction details.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateTransactionDetails() throws Exception {
		// Initialize the database
		this.transactionDetailsRepository.saveAndFlush(this.transactionDetails);
		int databaseSizeBeforeUpdate = this.transactionDetailsRepository.findAll().size();

		// Update the transactionDetails
		TransactionDetails updatedTransactionDetails = this.transactionDetailsRepository
				.findOne(this.transactionDetails.getId());
		updatedTransactionDetails.amount(TransactionDetailsResourceIntTest.UPDATED_AMOUNT);
		TransactionDetailsDTO transactionDetailsDTO = this.transactionDetailsMapper
				.toDto(updatedTransactionDetails);

		this.restTransactionDetailsMockMvc
		.perform(MockMvcRequestBuilders.put("/api/transaction-details").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the TransactionDetails in the database
		List<TransactionDetails> transactionDetailsList = this.transactionDetailsRepository.findAll();
		Assertions.assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);
		TransactionDetails testTransactionDetails = transactionDetailsList
				.get(transactionDetailsList.size() - 1);
		Assertions.assertThat(testTransactionDetails.getAmount()).isEqualTo(TransactionDetailsResourceIntTest.UPDATED_AMOUNT);
	}

	/**
	 * Update non existing transaction details.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingTransactionDetails() throws Exception {
		int databaseSizeBeforeUpdate = this.transactionDetailsRepository.findAll().size();

		// Create the TransactionDetails
		TransactionDetailsDTO transactionDetailsDTO = this.transactionDetailsMapper
				.toDto(this.transactionDetails);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		this.restTransactionDetailsMockMvc
		.perform(MockMvcRequestBuilders.put("/api/transaction-details").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the TransactionDetails in the database
		List<TransactionDetails> transactionDetailsList = this.transactionDetailsRepository.findAll();
		Assertions.assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete transaction details.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void deleteTransactionDetails() throws Exception {
		// Initialize the database
		this.transactionDetailsRepository.saveAndFlush(this.transactionDetails);
		int databaseSizeBeforeDelete = this.transactionDetailsRepository.findAll().size();

		// Get the transactionDetails
		this.restTransactionDetailsMockMvc
		.perform(MockMvcRequestBuilders.delete("/api/transaction-details/{id}", this.transactionDetails.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<TransactionDetails> transactionDetailsList = this.transactionDetailsRepository.findAll();
		Assertions.assertThat(transactionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
	}

	/**
	 * Equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(TransactionDetails.class);
		TransactionDetails transactionDetails1 = new TransactionDetails();
		transactionDetails1.setId(1L);
		TransactionDetails transactionDetails2 = new TransactionDetails();
		transactionDetails2.setId(transactionDetails1.getId());
		Assertions.assertThat(transactionDetails1).isEqualTo(transactionDetails2);
		transactionDetails2.setId(2L);
		Assertions.assertThat(transactionDetails1).isNotEqualTo(transactionDetails2);
		transactionDetails1.setId(null);
		Assertions.assertThat(transactionDetails1).isNotEqualTo(transactionDetails2);
	}

	/**
	 * Dto equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(TransactionDetailsDTO.class);
		TransactionDetailsDTO transactionDetailsDTO1 = new TransactionDetailsDTO();
		transactionDetailsDTO1.setId(1L);
		TransactionDetailsDTO transactionDetailsDTO2 = new TransactionDetailsDTO();
		Assertions.assertThat(transactionDetailsDTO1).isNotEqualTo(transactionDetailsDTO2);
		transactionDetailsDTO2.setId(transactionDetailsDTO1.getId());
		Assertions.assertThat(transactionDetailsDTO1).isEqualTo(transactionDetailsDTO2);
		transactionDetailsDTO2.setId(2L);
		Assertions.assertThat(transactionDetailsDTO1).isNotEqualTo(transactionDetailsDTO2);
		transactionDetailsDTO1.setId(null);
		Assertions.assertThat(transactionDetailsDTO1).isNotEqualTo(transactionDetailsDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.transactionDetailsMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.transactionDetailsMapper.fromId(null)).isNull();
	}
}
