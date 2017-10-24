/*
 * 
 */
package logia.quanlyso.web.rest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import logia.quanlyso.domain.Code;
import logia.quanlyso.repository.CodeRepository;
import logia.quanlyso.service.ChannelService;
import logia.quanlyso.service.CodeService;
import logia.quanlyso.service.dto.CodeDTO;
import logia.quanlyso.service.mapper.CodeMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CodeResource REST controller.
 *
 * @see CodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class CodeResourceIntTest {

	public static final String DEFAULT_CODE = "01";
	public static final String UPDATED_CODE = "02";

	public static final ZonedDateTime DEFAULT_OPEN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
			ZoneOffset.UTC);
	public static final ZonedDateTime UPDATED_OPEN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withHour(0)
			.withMinute(0).withSecond(0).withNano(0);

	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private CodeMapper codeMapper;

	@Autowired
	private CodeService codeService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCodeMockMvc;

	private Code code;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		CodeResource codeResource = new CodeResource(this.codeService, this.channelService);
		this.restCodeMockMvc = MockMvcBuilders.standaloneSetup(codeResource)
				.setCustomArgumentResolvers(this.pageableArgumentResolver).setControllerAdvice(this.exceptionTranslator)
				.setMessageConverters(this.jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Code createEntity(EntityManager em) {
		Code code = new Code().code(CodeResourceIntTest.DEFAULT_CODE).openDate(CodeResourceIntTest.DEFAULT_OPEN_DATE);
		return code;
	}

	@Before
	public void initTest() {
		this.code = CodeResourceIntTest.createEntity(this.em);
	}

	@Test
	@Transactional
	public void createCode() throws Exception {
		int databaseSizeBeforeCreate = this.codeRepository.findAll().size();

		// Create the Code
		CodeDTO codeDTO = this.codeMapper.toDto(this.code);
		this.restCodeMockMvc
				.perform(MockMvcRequestBuilders.post("/api/codes").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(codeDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Code in the database
		List<Code> codeList = this.codeRepository.findAll();
		Assertions.assertThat(codeList).hasSize(databaseSizeBeforeCreate + 1);
		Code testCode = codeList.get(codeList.size() - 1);
		Assertions.assertThat(testCode.getCode()).isEqualTo(CodeResourceIntTest.DEFAULT_CODE);
		Assertions.assertThat(testCode.getOpenDate()).isEqualTo(CodeResourceIntTest.DEFAULT_OPEN_DATE);
	}

	@Test
	@Transactional
	public void createCodeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.codeRepository.findAll().size();

		// Create the Code with an existing ID
		this.code.setId(1L);
		CodeDTO codeDTO = this.codeMapper.toDto(this.code);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		this.restCodeMockMvc
				.perform(MockMvcRequestBuilders.post("/api/codes").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(codeDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<Code> codeList = this.codeRepository.findAll();
		Assertions.assertThat(codeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCodes() throws Exception {
		// Initialize the database
		this.codeRepository.saveAndFlush(this.code);

		// Get all the codeList
		this.restCodeMockMvc.perform(MockMvcRequestBuilders.get("/api/codes?sort=id,desc"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id")
						.value(Matchers.hasItem(this.code.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].code")
						.value(Matchers.hasItem(CodeResourceIntTest.DEFAULT_CODE)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].openDate")
						.value(Matchers.hasItem(TestUtil.sameInstant(CodeResourceIntTest.DEFAULT_OPEN_DATE))));
	}

	@Test
	@Transactional
	public void getCode() throws Exception {
		// Initialize the database
		this.codeRepository.saveAndFlush(this.code);

		// Get the code
		this.restCodeMockMvc.perform(MockMvcRequestBuilders.get("/api/codes/{id}", this.code.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.code.getId().intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CodeResourceIntTest.DEFAULT_CODE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.openDate")
						.value(TestUtil.sameInstant(CodeResourceIntTest.DEFAULT_OPEN_DATE)));
	}

	@Test
	@Transactional
	public void getNonExistingCode() throws Exception {
		// Get the code
		this.restCodeMockMvc.perform(MockMvcRequestBuilders.get("/api/codes/{id}", Long.MAX_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCode() throws Exception {
		// Initialize the database
		this.codeRepository.saveAndFlush(this.code);
		int databaseSizeBeforeUpdate = this.codeRepository.findAll().size();

		// Update the code
		Code updatedCode = this.codeRepository.findOne(this.code.getId());
		updatedCode.code(CodeResourceIntTest.UPDATED_CODE).openDate(CodeResourceIntTest.UPDATED_OPEN_DATE);
		CodeDTO codeDTO = this.codeMapper.toDto(updatedCode);

		this.restCodeMockMvc
				.perform(MockMvcRequestBuilders.put("/api/codes").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(codeDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the Code in the database
		List<Code> codeList = this.codeRepository.findAll();
		Assertions.assertThat(codeList).hasSize(databaseSizeBeforeUpdate);
		Code testCode = codeList.get(codeList.size() - 1);
		Assertions.assertThat(testCode.getCode()).isEqualTo(CodeResourceIntTest.UPDATED_CODE);
		Assertions.assertThat(testCode.getOpenDate()).isEqualTo(CodeResourceIntTest.UPDATED_OPEN_DATE);
	}

	@Test
	@Transactional
	public void updateNonExistingCode() throws Exception {
		int databaseSizeBeforeUpdate = this.codeRepository.findAll().size();

		// Create the Code
		CodeDTO codeDTO = this.codeMapper.toDto(this.code);

		// If the entity doesn't have an ID, it will be created instead of just
		// being updated
		this.restCodeMockMvc
				.perform(MockMvcRequestBuilders.put("/api/codes").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(codeDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Code in the database
		List<Code> codeList = this.codeRepository.findAll();
		Assertions.assertThat(codeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteCode() throws Exception {
		// Initialize the database
		this.codeRepository.saveAndFlush(this.code);
		int databaseSizeBeforeDelete = this.codeRepository.findAll().size();

		// Get the code
		this.restCodeMockMvc.perform(MockMvcRequestBuilders.delete("/api/codes/{id}", this.code.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<Code> codeList = this.codeRepository.findAll();
		Assertions.assertThat(codeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Code.class);
		Code code1 = new Code();
		code1.setId(1L);
		Code code2 = new Code();
		code2.setId(code1.getId());
		Assertions.assertThat(code1).isEqualTo(code2);
		code2.setId(2L);
		Assertions.assertThat(code1).isNotEqualTo(code2);
		code1.setId(null);
		Assertions.assertThat(code1).isNotEqualTo(code2);
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(CodeDTO.class);
		CodeDTO codeDTO1 = new CodeDTO();
		codeDTO1.setId(1L);
		CodeDTO codeDTO2 = new CodeDTO();
		Assertions.assertThat(codeDTO1).isNotEqualTo(codeDTO2);
		codeDTO2.setId(codeDTO1.getId());
		Assertions.assertThat(codeDTO1).isEqualTo(codeDTO2);
		codeDTO2.setId(2L);
		Assertions.assertThat(codeDTO1).isNotEqualTo(codeDTO2);
		codeDTO1.setId(null);
		Assertions.assertThat(codeDTO1).isNotEqualTo(codeDTO2);
	}

	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.codeMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.codeMapper.fromId(null)).isNull();
	}
}
