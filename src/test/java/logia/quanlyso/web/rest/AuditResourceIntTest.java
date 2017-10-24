/*
 * 
 */
package logia.quanlyso.web.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.config.audit.AuditEventConverter;
import logia.quanlyso.domain.PersistentAuditEvent;
import logia.quanlyso.repository.PersistenceAuditEventRepository;
import logia.quanlyso.service.AuditEventService;

/**
 * Test class for the AuditResource REST controller.
 *
 * @see AuditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
@Transactional
public class AuditResourceIntTest {

	/** The Constant SAMPLE_PRINCIPAL. */
	private static final String SAMPLE_PRINCIPAL = "SAMPLE_PRINCIPAL";

	/** The Constant SAMPLE_TYPE. */
	private static final String SAMPLE_TYPE = "SAMPLE_TYPE";

	/** The Constant SAMPLE_TIMESTAMP. */
	private static final LocalDateTime SAMPLE_TIMESTAMP = LocalDateTime.parse("2015-08-04T10:11:30");

	/** The Constant FORMATTER. */
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/** The audit event repository. */
	@Autowired
	private PersistenceAuditEventRepository auditEventRepository;

	/** The audit event converter. */
	@Autowired
	private AuditEventConverter auditEventConverter;

	/** The jackson message converter. */
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	/** The formatting conversion service. */
	@Autowired
	private FormattingConversionService formattingConversionService;

	/** The pageable argument resolver. */
	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	/** The audit event. */
	private PersistentAuditEvent auditEvent;

	/** The rest audit mock mvc. */
	private MockMvc restAuditMockMvc;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		AuditEventService auditEventService = new AuditEventService(this.auditEventRepository,
				this.auditEventConverter);
		AuditResource auditResource = new AuditResource(auditEventService);
		this.restAuditMockMvc = MockMvcBuilders.standaloneSetup(auditResource)
				.setCustomArgumentResolvers(this.pageableArgumentResolver)
				.setConversionService(this.formattingConversionService)
				.setMessageConverters(this.jacksonMessageConverter).build();
	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.auditEventRepository.deleteAll();
		this.auditEvent = new PersistentAuditEvent();
		this.auditEvent.setAuditEventType(AuditResourceIntTest.SAMPLE_TYPE);
		this.auditEvent.setPrincipal(AuditResourceIntTest.SAMPLE_PRINCIPAL);
		this.auditEvent.setAuditEventDate(AuditResourceIntTest.SAMPLE_TIMESTAMP);
	}

	/**
	 * Gets the all audits.
	 *
	 * @return the all audits
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getAllAudits() throws Exception {
		// Initialize the database
		this.auditEventRepository.save(this.auditEvent);

		// Get all the audits
		this.restAuditMockMvc.perform(MockMvcRequestBuilders.get("/management/audits"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].principal")
						.value(Matchers.hasItem(AuditResourceIntTest.SAMPLE_PRINCIPAL)));
	}

	/**
	 * Gets the audit.
	 *
	 * @return the audit
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getAudit() throws Exception {
		// Initialize the database
		this.auditEventRepository.save(this.auditEvent);

		// Get the audit
		this.restAuditMockMvc.perform(MockMvcRequestBuilders.get("/management/audits/{id}", this.auditEvent.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.principal").value(AuditResourceIntTest.SAMPLE_PRINCIPAL));
	}

	/**
	 * Gets the audits by date.
	 *
	 * @return the audits by date
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getAuditsByDate() throws Exception {
		// Initialize the database
		this.auditEventRepository.save(this.auditEvent);

		// Generate dates for selecting audits by date, making sure the period
		// will contain the
		// audit
		String fromDate = AuditResourceIntTest.SAMPLE_TIMESTAMP.minusDays(1).format(AuditResourceIntTest.FORMATTER);
		String toDate = AuditResourceIntTest.SAMPLE_TIMESTAMP.plusDays(1).format(AuditResourceIntTest.FORMATTER);

		// Get the audit
		this.restAuditMockMvc
				.perform(MockMvcRequestBuilders.get("/management/audits?fromDate=" + fromDate + "&toDate=" + toDate))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].principal")
						.value(Matchers.hasItem(AuditResourceIntTest.SAMPLE_PRINCIPAL)));
	}

	/**
	 * Gets the non existing audits by date.
	 *
	 * @return the non existing audits by date
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getNonExistingAuditsByDate() throws Exception {
		// Initialize the database
		this.auditEventRepository.save(this.auditEvent);

		// Generate dates for selecting audits by date, making sure the period
		// will not contain the
		// sample audit
		String fromDate = AuditResourceIntTest.SAMPLE_TIMESTAMP.minusDays(2).format(AuditResourceIntTest.FORMATTER);
		String toDate = AuditResourceIntTest.SAMPLE_TIMESTAMP.minusDays(1).format(AuditResourceIntTest.FORMATTER);

		// Query audits but expect no results
		this.restAuditMockMvc
				.perform(MockMvcRequestBuilders.get("/management/audits?fromDate=" + fromDate + "&toDate=" + toDate))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "0"));
	}

	/**
	 * Gets the non existing audit.
	 *
	 * @return the non existing audit
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getNonExistingAudit() throws Exception {
		// Get the audit
		this.restAuditMockMvc.perform(MockMvcRequestBuilders.get("/management/audits/{id}", Long.MAX_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
