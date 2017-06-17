package logia.quanlyso.web.rest;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.domain.Transactions;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.TransactionsRepository;
import logia.quanlyso.service.TransactionsService;
import logia.quanlyso.service.dto.TransactionsDTO;
import logia.quanlyso.service.mapper.TransactionsMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransactionsResource REST controller.
 *
 * @see TransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class TransactionsResourceIntTest {

    /** The Constant DEFAULT_CHOSEN_NUMBER. */
    static final Integer DEFAULT_CHOSEN_NUMBER = 1;
    
    /** The Constant UPDATED_CHOSEN_NUMBER. */
    static final Integer UPDATED_CHOSEN_NUMBER = 2;

    /** The Constant DEFAULT_NET_VALUE. */
    static final Float DEFAULT_NET_VALUE = 1F;
    
    /** The Constant UPDATED_NET_VALUE. */
    static final Float UPDATED_NET_VALUE = 2F;

    /** The transactions repository. */
    @Autowired
    private TransactionsRepository transactionsRepository;

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

    /** The transactions. */
    private Transactions transactions;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransactionsResource transactionsResource = new TransactionsResource(transactionsService);
        this.restTransactionsMockMvc = MockMvcBuilders.standaloneSetup(transactionsResource)
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
     * @return the transactions
     */
    public static Transactions createEntity(EntityManager em) {
    	TransactionDetails transactionDetails = TransactionDetailsResourceIntTest.createEntity(em);
    	User user = UserResourceIntTest.createAndSaveEntity(em);
        Transactions transactions = new Transactions()
            .chosenNumber(DEFAULT_CHOSEN_NUMBER)
            .netValue(DEFAULT_NET_VALUE)
            .users(user)
            .addTransactionDetails(transactionDetails);
        return transactions;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        transactions = createEntity(em);
    }

    /**
     * Creates the transactions.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createTransactions() throws Exception {
        int databaseSizeBeforeCreate = transactionsRepository.findAll().size();

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);
        restTransactionsMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeCreate + 1);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getChosenNumber()).isEqualTo(DEFAULT_CHOSEN_NUMBER);
        assertThat(testTransactions.getNetValue()).isEqualTo(DEFAULT_NET_VALUE);
        assertThat(testTransactions.getTransactionDetails().size()).isEqualTo(1);
        assertThat(testTransactions.getUsers().getLogin()).isEqualTo(UserResourceIntTest.DEFAULT_LOGIN);
        assertThat(testTransactions.getUsers().getFirstName()).isEqualTo(UserResourceIntTest.DEFAULT_FIRSTNAME);
        assertThat(testTransactions.getUsers().getLastName()).isEqualTo(UserResourceIntTest.DEFAULT_LASTNAME);
        assertThat(testTransactions.getUsers().getEmail()).isEqualTo(UserResourceIntTest.DEFAULT_EMAIL);
        assertThat(testTransactions.getUsers().getImageUrl()).isEqualTo(UserResourceIntTest.DEFAULT_IMAGEURL);
        assertThat(testTransactions.getUsers().getLangKey()).isEqualTo(UserResourceIntTest.DEFAULT_LANGKEY);
        testTransactions.getTransactionDetails().forEach(testTransactionDetails -> {
        	assertThat(testTransactionDetails.getAmount()).isEqualTo(TransactionDetailsResourceIntTest.DEFAULT_AMOUNT);
            assertThat(testTransactionDetails.getProfit()).isEqualTo(TransactionDetailsResourceIntTest.DEFAULT_PROFIT);
            assertThat(testTransactionDetails.getCosts()).isEqualTo(TransactionDetailsResourceIntTest.DEFAULT_COSTS);
        });
    }

    /**
     * Creates the transactions with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionsRepository.findAll().size();

        // Create the Transactions with an existing ID
        transactions.setId(1L);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionsMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all transactions.
     *
     * @return the all transactions
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList
        restTransactionsMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].chosenNumber").value(hasItem(DEFAULT_CHOSEN_NUMBER)))
            .andExpect(jsonPath("$.[*].netValue").value(hasItem(DEFAULT_NET_VALUE.doubleValue())));
    }

    /**
     * Gets the transactions.
     *
     * @return the transactions
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get the transactions
        restTransactionsMockMvc.perform(get("/api/transactions/{id}", transactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactions.getId().intValue()))
            .andExpect(jsonPath("$.chosenNumber").value(DEFAULT_CHOSEN_NUMBER))
            .andExpect(jsonPath("$.netValue").value(DEFAULT_NET_VALUE.doubleValue()));
    }

    /**
     * Gets the non existing transactions.
     *
     * @return the non existing transactions
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getNonExistingTransactions() throws Exception {
        // Get the transactions
        restTransactionsMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    /**
     * Update transactions.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Update the transactions
        Transactions updatedTransactions = transactionsRepository.findOne(transactions.getId());
        updatedTransactions
            .chosenNumber(UPDATED_CHOSEN_NUMBER)
            .netValue(UPDATED_NET_VALUE);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(updatedTransactions);

        restTransactionsMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isOk());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getChosenNumber()).isEqualTo(UPDATED_CHOSEN_NUMBER);
        assertThat(testTransactions.getNetValue()).isEqualTo(UPDATED_NET_VALUE);
    }

    /**
     * Update non existing transactions.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionsMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete transactions.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void deleteTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);
        int databaseSizeBeforeDelete = transactionsRepository.findAll().size();

        // Get the transactions
        restTransactionsMockMvc.perform(delete("/api/transactions/{id}", transactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transactions.class);
        Transactions transactions1 = new Transactions();
        transactions1.setId(1L);
        Transactions transactions2 = new Transactions();
        transactions2.setId(transactions1.getId());
        assertThat(transactions1).isEqualTo(transactions2);
        transactions2.setId(2L);
        assertThat(transactions1).isNotEqualTo(transactions2);
        transactions1.setId(null);
        assertThat(transactions1).isNotEqualTo(transactions2);
    }

    /**
     * Dto equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionsDTO.class);
        TransactionsDTO transactionsDTO1 = new TransactionsDTO();
        transactionsDTO1.setId(1L);
        TransactionsDTO transactionsDTO2 = new TransactionsDTO();
        assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
        transactionsDTO2.setId(transactionsDTO1.getId());
        assertThat(transactionsDTO1).isEqualTo(transactionsDTO2);
        transactionsDTO2.setId(2L);
        assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
        transactionsDTO1.setId(null);
        assertThat(transactionsDTO1).isNotEqualTo(transactionsDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionsMapper.fromId(null)).isNull();
    }
}
