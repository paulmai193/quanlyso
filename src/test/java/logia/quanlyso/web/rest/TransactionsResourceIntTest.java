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
import logia.quanlyso.domain.Channel;
import logia.quanlyso.domain.Code;
import logia.quanlyso.domain.Style;
import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.domain.Transactions;
import logia.quanlyso.domain.Types;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.repository.CodeRepository;
import logia.quanlyso.repository.StyleRepository;
import logia.quanlyso.repository.TransactionDetailsRepository;
import logia.quanlyso.repository.TransactionsRepository;
import logia.quanlyso.repository.TypesRepository;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.service.TransactionsService;
import logia.quanlyso.service.dto.TransactionsDTO;
import logia.quanlyso.service.mapper.TransactionsMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the TransactionsResource REST controller.
 *
 * @see TransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class TransactionsResourceIntTest {

	/** The Constant DEFAULT_CHOSEN_NUMBER. */
	static final String DEFAULT_CHOSEN_NUMBER = "01";

	/** The Constant UPDATED_CHOSEN_NUMBER. */
	static final String UPDATED_CHOSEN_NUMBER = "02";

	/** The Constant DEFAULT_NET_VALUE. */
	static final Float DEFAULT_NET_VALUE = 0F;

	// /** The Constant UPDATED_NET_VALUE. */
	// static final Float UPDATED_NET_VALUE = 2F;

	/** The transactions repository. */
	@Autowired
	private TransactionsRepository transactionsRepository;

	/** The transaction details repository. */
	@Autowired
	private TransactionDetailsRepository transactionDetailsRepository;

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/** The channel repository. */
	@Autowired
	private ChannelRepository channelRepository;

	/** The style repository. */
	@Autowired
	private StyleRepository styleRepository;

	/** The types repository. */
	@Autowired
	private TypesRepository typesRepository;

	/** The code repository. */
	@Autowired
	private CodeRepository codeRepository;

	/** The transactions mapper. */
	@Autowired
	private TransactionsMapper transactionsMapper;

	/** The transactions service. */
	@Autowired
	private TransactionsService transactionsService;

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

	/** The rest transactions mock mvc. */
	private MockMvc restTransactionsMockMvc;

	// /** The transactions. */
	// private Transactions transactions;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TransactionsResource transactionsResource = new TransactionsResource(this.transactionsService);
		this.restTransactionsMockMvc = MockMvcBuilders.standaloneSetup(transactionsResource)
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
	 * @return the transactions
	 */
	public static Transactions createEntity(EntityManager em) {
		Transactions transactions = new Transactions().chosenNumber(TransactionsResourceIntTest.DEFAULT_CHOSEN_NUMBER)
				.netValue(TransactionsResourceIntTest.DEFAULT_NET_VALUE);
		return transactions;
	}

	// /**
	// * Creates the and save entity.
	// *
	// * @param em the em
	// * @return the transactions
	// */
	// public static Transactions createAndSaveEntity(EntityManager em) {
	// Transactions transactions = createEntity(em);
	// em.persist(transactions);
	// em.flush();
	// return transactions;
	// }

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {

	}

	/**
	 * Creates the transactions.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		User user = UserResourceIntTest.createEntity(this.em);
		user = this.userRepository.saveAndFlush(user);
		transactions.users(user);
		int databaseSizeBeforeCreate = this.transactionsRepository.findAll().size();

		// Create the Transactions
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(transactions);
		this.restTransactionsMockMvc
				.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Transactions in the database
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(databaseSizeBeforeCreate + 1);
		final Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
		Assertions.assertThat(testTransactions.getChosenNumber())
				.isEqualTo(TransactionsResourceIntTest.DEFAULT_CHOSEN_NUMBER);
	}

	/**
	 * Creates the complete transactions.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createCompleteTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		TransactionDetails details = TransactionDetailsResourceIntTest.createEntity(this.em);
		User user = UserResourceIntTest.createEntity(this.em);
		user = this.userRepository.saveAndFlush(user);
		transactions.addTransactionDetails(details).users(user);

		int databaseSizeBeforeCreate = this.transactionsRepository.findAll().size();

		// Create the Transactions
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(transactions);
		this.restTransactionsMockMvc
				.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Transactions in the database
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(databaseSizeBeforeCreate + 1);
		final Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
		Assertions.assertThat(testTransactions.getChosenNumber())
				.isEqualTo(TransactionsResourceIntTest.DEFAULT_CHOSEN_NUMBER);
		Assertions.assertThat(testTransactions.getTransactionDetails().size()).isEqualTo(1);
		Assertions.assertThat(testTransactions.getUsers().getLogin()).isEqualTo(UserResourceIntTest.DEFAULT_LOGIN);
		Assertions.assertThat(testTransactions.getUsers().getFirstName())
				.isEqualTo(UserResourceIntTest.DEFAULT_FIRSTNAME);
		Assertions.assertThat(testTransactions.getUsers().getLastName())
				.isEqualTo(UserResourceIntTest.DEFAULT_LASTNAME);
		Assertions.assertThat(testTransactions.getUsers().getEmail()).isEqualTo(UserResourceIntTest.DEFAULT_EMAIL);
		Assertions.assertThat(testTransactions.getUsers().getImageUrl())
				.isEqualTo(UserResourceIntTest.DEFAULT_IMAGEURL);
		Assertions.assertThat(testTransactions.getUsers().getLangKey()).isEqualTo(UserResourceIntTest.DEFAULT_LANGKEY);
		Assertions.assertThat(testTransactions.getTransactionDetails().size()).isEqualTo(1);
		Long testDetailId = null;
		for (TransactionDetails testTransactionDetails : testTransactions.getTransactionDetails()) {
			Assertions.assertThat(testTransactionDetails.getTransactions().getId()).isEqualTo(testTransactions.getId());
			Assertions.assertThat(testTransactionDetails.getAmount())
					.isEqualTo(TransactionDetailsResourceIntTest.DEFAULT_AMOUNT);
			testDetailId = testTransactionDetails.getId();
		}
		User testUser = this.userRepository.getOne(user.getId());
		Assertions.assertThat(testUser.getTransactionsses()).contains(testTransactions);
		if (testDetailId != null) {
			TransactionDetails testDetail = this.transactionDetailsRepository.getOne(testDetailId);
			Assertions.assertThat(testDetail.getTransactions()).isEqualTo(testTransactions);
		}
	}

	/**
	 * Creates the transactions with existing id.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createTransactionsWithExistingId() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		int databaseSizeBeforeCreate = this.transactionsRepository.findAll().size();

		// Create the Transactions with an existing ID
		transactions.setId(1L);
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(transactions);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		this.restTransactionsMockMvc
				.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all transactions.
	 *
	 * @return the all transactions
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		this.transactionsRepository.saveAndFlush(transactions);

		// Get all the transactionsList
		this.restTransactionsMockMvc.perform(MockMvcRequestBuilders.get("/api/transactions?sort=id,desc"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id")
						.value(Matchers.hasItem(transactions.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].chosenNumber")
						.value(Matchers.hasItem(TransactionsResourceIntTest.DEFAULT_CHOSEN_NUMBER)));
	}

	/**
	 * Gets the transactions.
	 *
	 * @return the transactions
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		this.transactionsRepository.saveAndFlush(transactions);

		// Get the transactions
		this.restTransactionsMockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", transactions.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(transactions.getId().intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.chosenNumber")
						.value(TransactionsResourceIntTest.DEFAULT_CHOSEN_NUMBER));
	}

	/**
	 * Gets the non existing transactions.
	 *
	 * @return the non existing transactions
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getNonExistingTransactions() throws Exception {
		// Get the transactions
		this.restTransactionsMockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", Long.MAX_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update transactions.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		User user = UserResourceIntTest.createEntity(this.em);
		user = this.userRepository.saveAndFlush(user);
		transactions.users(user);
		this.transactionsRepository.saveAndFlush(transactions);
		int databaseSizeBeforeUpdate = this.transactionsRepository.findAll().size();

		// Update the transactions
		Transactions updatedTransactions = this.transactionsRepository.findOne(transactions.getId());
		updatedTransactions.chosenNumber(TransactionsResourceIntTest.UPDATED_CHOSEN_NUMBER);
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(updatedTransactions);

		this.restTransactionsMockMvc
				.perform(MockMvcRequestBuilders.put("/api/transactions").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the Transactions in the database
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
		Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
		Assertions.assertThat(testTransactions.getChosenNumber())
				.isEqualTo(TransactionsResourceIntTest.UPDATED_CHOSEN_NUMBER);
	}

	/**
	 * Update non existing transactions.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingTransactions() throws Exception {
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		User user = UserResourceIntTest.createEntity(this.em);
		user = this.userRepository.saveAndFlush(user);
		transactions.users(user);
		int databaseSizeBeforeUpdate = this.transactionsRepository.findAll().size();

		// Create the Transactions
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(transactions);

		// If the entity doesn't have an ID, it will be created instead of just
		// being updated
		this.restTransactionsMockMvc
				.perform(MockMvcRequestBuilders.put("/api/transactions").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Transactions in the database
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete transactions.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void deleteTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		this.transactionsRepository.saveAndFlush(transactions);
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		int dbTransactionSizeBeforeDelete = transactionsList.size();

		// Get the transactions
		transactions = transactionsList.get(0);
		this.restTransactionsMockMvc.perform(MockMvcRequestBuilders
				.delete("/api/transactions/{id}", transactions.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(dbTransactionSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void deleteCompleteTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		TransactionDetails details = TransactionDetailsResourceIntTest.createEntity(this.em);
		User user = UserResourceIntTest.createEntity(this.em);
		user = this.userRepository.saveAndFlush(user);
		transactions.addTransactionDetails(details).users(user);
		this.transactionsRepository.saveAndFlush(transactions);
		List<Transactions> transactionsList = this.transactionsRepository.findAll();
		int dbTransactionSizeBeforeDelete = transactionsList.size();
		int dbTransDetailSizeBeforeDelete = this.transactionDetailsRepository.findAll().size();

		// Get the transactions
		transactions = transactionsList.get(0);
		this.restTransactionsMockMvc.perform(MockMvcRequestBuilders
				.delete("/api/transactions/{id}", transactions.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		transactionsList = this.transactionsRepository.findAll();
		Assertions.assertThat(transactionsList).hasSize(dbTransactionSizeBeforeDelete - 1);
		List<TransactionDetails> transactionDetails = this.transactionDetailsRepository.findAll();
		Assertions.assertThat(transactionDetails)
				.hasSize(dbTransDetailSizeBeforeDelete == 0 ? 0 : dbTransDetailSizeBeforeDelete - 1);
		// User testUser = this.userRepository.findOne(user.getId());
		// assertThat(testUser.getTransactionsses()).isEmpty();
	}

	@Test
	@Transactional
	public void calculateTransactions() throws Exception {
		// Initialize the database
		Transactions transactions = TransactionsResourceIntTest.createEntity(this.em);
		TransactionDetails details = TransactionDetailsResourceIntTest.createEntity(this.em);
		Channel channel = this.channelRepository.getOne(1L);
		Code code = CodeResourceIntTest.createEntity(this.em);
		code.openDate(CodeResourceIntTest.UPDATED_OPEN_DATE).channels(channel);
		this.codeRepository.saveAndFlush(code);
		Style style = this.styleRepository.getOne(1L);
		Types types = this.typesRepository.getOne(1L);
		details.channels(channel).styles(style).types(types);
		transactions.addTransactionDetails(details);

		// Expect net value
		float netValue = (float) 74.25;

		// Calculate the Transactions
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(transactions);
		this.restTransactionsMockMvc
				.perform(MockMvcRequestBuilders.post("/api/transactions/calculate")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netValue").value(netValue));
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
		TestUtil.equalsVerifier(Transactions.class);
		Transactions transactions1 = new Transactions();
		transactions1.setId(1L);
		Transactions transactions2 = new Transactions();
		transactions2.setId(transactions1.getId());
		Assertions.assertThat(transactions1).isEqualTo(transactions2);
		transactions2.setId(2L);
		Assertions.assertThat(transactions1).isNotEqualTo(transactions2);
		transactions1.setId(null);
		Assertions.assertThat(transactions1).isNotEqualTo(transactions2);
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
		TestUtil.equalsVerifier(TransactionsDTO.class);
		TransactionsDTO transactionsDTO1 = new TransactionsDTO();
		transactionsDTO1.setId(1L);
		TransactionsDTO transactionsDTO2 = new TransactionsDTO();
		Assertions.assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
		transactionsDTO2.setId(transactionsDTO1.getId());
		Assertions.assertThat(transactionsDTO1).isEqualTo(transactionsDTO2);
		transactionsDTO2.setId(2L);
		Assertions.assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
		transactionsDTO1.setId(null);
		Assertions.assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.transactionsMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.transactionsMapper.fromId(null)).isNull();
	}
}
