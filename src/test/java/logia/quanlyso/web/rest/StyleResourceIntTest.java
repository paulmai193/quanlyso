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
import logia.quanlyso.domain.Style;
import logia.quanlyso.repository.StyleRepository;
import logia.quanlyso.service.StyleService;
import logia.quanlyso.service.dto.StyleDTO;
import logia.quanlyso.service.mapper.StyleMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the StyleResource REST controller.
 *
 * @see StyleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class StyleResourceIntTest {

	/** The Constant DEFAULT_NAME. */
	private static final String						DEFAULT_NAME	= "AAAAAAAAAA";

	/** The Constant UPDATED_NAME. */
	private static final String						UPDATED_NAME	= "BBBBBBBBBB";

	/** The style repository. */
	@Autowired
	private StyleRepository							styleRepository;

	/** The style mapper. */
	@Autowired
	private StyleMapper								styleMapper;

	/** The style service. */
	@Autowired
	private StyleService							styleService;

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

	/** The rest style mock mvc. */
	private MockMvc									restStyleMockMvc;

	/** The style. */
	private Style									style;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		StyleResource styleResource = new StyleResource(this.styleService);
		this.restStyleMockMvc = MockMvcBuilders.standaloneSetup(styleResource)
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
	 * @return the style
	 */
	public static Style createEntity(EntityManager em) {
		Style style = new Style().name(StyleResourceIntTest.DEFAULT_NAME);
		return style;
	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.style = StyleResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the style.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createStyle() throws Exception {
		int databaseSizeBeforeCreate = this.styleRepository.findAll().size();

		// Create the Style
		StyleDTO styleDTO = this.styleMapper.toDto(this.style);
		this.restStyleMockMvc
		.perform(MockMvcRequestBuilders.post("/api/styles").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(styleDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Style in the database
		List<Style> styleList = this.styleRepository.findAll();
		Assertions.assertThat(styleList).hasSize(databaseSizeBeforeCreate + 1);
		Style testStyle = styleList.get(styleList.size() - 1);
		Assertions.assertThat(testStyle.getName()).isEqualTo(StyleResourceIntTest.DEFAULT_NAME);
	}

	/**
	 * Creates the style with existing id.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createStyleWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.styleRepository.findAll().size();

		// Create the Style with an existing ID
		this.style.setId(1L);
		StyleDTO styleDTO = this.styleMapper.toDto(this.style);

		// An entity with an existing ID cannot be created, so this API call must fail
		this.restStyleMockMvc
		.perform(MockMvcRequestBuilders.post("/api/styles").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(styleDTO)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<Style> styleList = this.styleRepository.findAll();
		Assertions.assertThat(styleList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all styles.
	 *
	 * @return the all styles
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getAllStyles() throws Exception {
		// Initialize the database
		this.styleRepository.saveAndFlush(this.style);

		// Get all the styleList
		this.restStyleMockMvc.perform(MockMvcRequestBuilders.get("/api/styles?sort=id,desc")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.style.getId().intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").value(Matchers.hasItem(StyleResourceIntTest.DEFAULT_NAME.toString())));
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getStyle() throws Exception {
		// Initialize the database
		this.styleRepository.saveAndFlush(this.style);

		// Get the style
		this.restStyleMockMvc.perform(MockMvcRequestBuilders.get("/api/styles/{id}", this.style.getId())).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.style.getId().intValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(StyleResourceIntTest.DEFAULT_NAME.toString()));
	}

	/**
	 * Gets the non existing style.
	 *
	 * @return the non existing style
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getNonExistingStyle() throws Exception {
		// Get the style
		this.restStyleMockMvc.perform(MockMvcRequestBuilders.get("/api/styles/{id}", Long.MAX_VALUE))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update style.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateStyle() throws Exception {
		// Initialize the database
		this.styleRepository.saveAndFlush(this.style);
		int databaseSizeBeforeUpdate = this.styleRepository.findAll().size();

		// Update the style
		Style updatedStyle = this.styleRepository.findOne(this.style.getId());
		updatedStyle.name(StyleResourceIntTest.UPDATED_NAME);
		StyleDTO styleDTO = this.styleMapper.toDto(updatedStyle);

		this.restStyleMockMvc
		.perform(MockMvcRequestBuilders.put("/api/styles").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(styleDTO)))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the Style in the database
		List<Style> styleList = this.styleRepository.findAll();
		Assertions.assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
		Style testStyle = styleList.get(styleList.size() - 1);
		Assertions.assertThat(testStyle.getName()).isEqualTo(StyleResourceIntTest.UPDATED_NAME);
	}

	/**
	 * Update non existing style.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingStyle() throws Exception {
		int databaseSizeBeforeUpdate = this.styleRepository.findAll().size();

		// Create the Style
		StyleDTO styleDTO = this.styleMapper.toDto(this.style);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		this.restStyleMockMvc
		.perform(MockMvcRequestBuilders.put("/api/styles").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(styleDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Style in the database
		List<Style> styleList = this.styleRepository.findAll();
		Assertions.assertThat(styleList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete style.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void deleteStyle() throws Exception {
		// Initialize the database
		this.styleRepository.saveAndFlush(this.style);
		int databaseSizeBeforeDelete = this.styleRepository.findAll().size();

		// Get the style
		this.restStyleMockMvc.perform(
				MockMvcRequestBuilders.delete("/api/styles/{id}", this.style.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<Style> styleList = this.styleRepository.findAll();
		Assertions.assertThat(styleList).hasSize(databaseSizeBeforeDelete - 1);
	}

	/**
	 * Equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Style.class);
		Style style1 = new Style();
		style1.setId(1L);
		Style style2 = new Style();
		style2.setId(style1.getId());
		Assertions.assertThat(style1).isEqualTo(style2);
		style2.setId(2L);
		Assertions.assertThat(style1).isNotEqualTo(style2);
		style1.setId(null);
		Assertions.assertThat(style1).isNotEqualTo(style2);
	}

	/**
	 * Dto equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(StyleDTO.class);
		StyleDTO styleDTO1 = new StyleDTO();
		styleDTO1.setId(1L);
		StyleDTO styleDTO2 = new StyleDTO();
		Assertions.assertThat(styleDTO1).isNotEqualTo(styleDTO2);
		styleDTO2.setId(styleDTO1.getId());
		Assertions.assertThat(styleDTO1).isEqualTo(styleDTO2);
		styleDTO2.setId(2L);
		Assertions.assertThat(styleDTO1).isNotEqualTo(styleDTO2);
		styleDTO1.setId(null);
		Assertions.assertThat(styleDTO1).isNotEqualTo(styleDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.styleMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.styleMapper.fromId(null)).isNull();
	}
}
