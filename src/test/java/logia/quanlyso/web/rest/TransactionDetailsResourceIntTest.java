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
    static final Float DEFAULT_AMOUNT = 1F;
    
    /** The Constant UPDATED_AMOUNT. */
    static final Float UPDATED_AMOUNT = 2F;

    /** The Constant DEFAULT_PROFIT. */
    static final Float DEFAULT_PROFIT = 1F;
    
    /** The Constant UPDATED_PROFIT. */
    static final Float UPDATED_PROFIT = 2F;

    /** The Constant DEFAULT_COSTS. */
    static final Float DEFAULT_COSTS = 1F;
    
    /** The Constant UPDATED_COSTS. */
    static final Float UPDATED_COSTS = 2F;

    /** The transaction details repository. */
    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    /** The transaction details mapper. */
    @Autowired
    private TransactionDetailsMapper transactionDetailsMapper;

    /** The transaction details service. */
    @Autowired
    private TransactionDetailsService transactionDetailsService;

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

    /** The rest transaction details mock mvc. */
    private MockMvc restTransactionDetailsMockMvc;

    /** The transaction details. */
    private TransactionDetails transactionDetails;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransactionDetailsResource transactionDetailsResource = new TransactionDetailsResource(transactionDetailsService);
        this.restTransactionDetailsMockMvc = MockMvcBuilders.standaloneSetup(transactionDetailsResource)
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
     * @return the transaction details
     */
    public static TransactionDetails createEntity(EntityManager em) {
        TransactionDetails transactionDetails = new TransactionDetails()
            .amount(DEFAULT_AMOUNT)
            .profit(DEFAULT_PROFIT)
            .costs(DEFAULT_COSTS);
        return transactionDetails;
    }
    
    /**
     * Creates the and save entity.
     *
     * @param em the em
     * @return the transaction details
     */
    public static TransactionDetails createAndSaveEntity(EntityManager em) {
    	TransactionDetails transactionDetails = createEntity(em);
    	em.persist(transactionDetails);
    	em.flush();
    	return transactionDetails;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        transactionDetails = createEntity(em);
    }

    /**
     * Creates the transaction details.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createTransactionDetails() throws Exception {
        int databaseSizeBeforeCreate = transactionDetailsRepository.findAll().size();

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);
        restTransactionDetailsMockMvc.perform(post("/api/transaction-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionDetails testTransactionDetails = transactionDetailsList.get(transactionDetailsList.size() - 1);
        assertThat(testTransactionDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransactionDetails.getProfit()).isEqualTo(DEFAULT_PROFIT);
        assertThat(testTransactionDetails.getCosts()).isEqualTo(DEFAULT_COSTS);
    }

    /**
     * Creates the transaction details with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createTransactionDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionDetailsRepository.findAll().size();

        // Create the TransactionDetails with an existing ID
        transactionDetails.setId(1L);
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionDetailsMockMvc.perform(post("/api/transaction-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeCreate);
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
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList
        restTransactionDetailsMockMvc.perform(get("/api/transaction-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(DEFAULT_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].costs").value(hasItem(DEFAULT_COSTS.doubleValue())));
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
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get the transactionDetails
        restTransactionDetailsMockMvc.perform(get("/api/transaction-details/{id}", transactionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionDetails.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.profit").value(DEFAULT_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.costs").value(DEFAULT_COSTS.doubleValue()));
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
        restTransactionDetailsMockMvc.perform(get("/api/transaction-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
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
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();

        // Update the transactionDetails
        TransactionDetails updatedTransactionDetails = transactionDetailsRepository.findOne(transactionDetails.getId());
        updatedTransactionDetails
            .amount(UPDATED_AMOUNT)
            .profit(UPDATED_PROFIT)
            .costs(UPDATED_COSTS);
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(updatedTransactionDetails);

        restTransactionDetailsMockMvc.perform(put("/api/transaction-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);
        TransactionDetails testTransactionDetails = transactionDetailsList.get(transactionDetailsList.size() - 1);
        assertThat(testTransactionDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransactionDetails.getProfit()).isEqualTo(UPDATED_PROFIT);
        assertThat(testTransactionDetails.getCosts()).isEqualTo(UPDATED_COSTS);
    }

    /**
     * Update non existing transaction details.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionDetailsMockMvc.perform(put("/api/transaction-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
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
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        int databaseSizeBeforeDelete = transactionDetailsRepository.findAll().size();

        // Get the transactionDetails
        restTransactionDetailsMockMvc.perform(delete("/api/transaction-details/{id}", transactionDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
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
        assertThat(transactionDetails1).isEqualTo(transactionDetails2);
        transactionDetails2.setId(2L);
        assertThat(transactionDetails1).isNotEqualTo(transactionDetails2);
        transactionDetails1.setId(null);
        assertThat(transactionDetails1).isNotEqualTo(transactionDetails2);
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
        assertThat(transactionDetailsDTO1).isNotEqualTo(transactionDetailsDTO2);
        transactionDetailsDTO2.setId(transactionDetailsDTO1.getId());
        assertThat(transactionDetailsDTO1).isEqualTo(transactionDetailsDTO2);
        transactionDetailsDTO2.setId(2L);
        assertThat(transactionDetailsDTO1).isNotEqualTo(transactionDetailsDTO2);
        transactionDetailsDTO1.setId(null);
        assertThat(transactionDetailsDTO1).isNotEqualTo(transactionDetailsDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionDetailsMapper.fromId(null)).isNull();
    }
}
