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
import logia.quanlyso.domain.Types;
import logia.quanlyso.repository.TypesRepository;
import logia.quanlyso.service.TypesService;
import logia.quanlyso.service.dto.TypesDTO;
import logia.quanlyso.service.mapper.TypesMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the TypesResource REST controller.
 *
 * @see TypesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class TypesResourceIntTest {

	/** The Constant DEFAULT_NAME. */
	private static final String						DEFAULT_NAME	= "AAAAAAAAAA";

	/** The Constant UPDATED_NAME. */
	private static final String						UPDATED_NAME	= "BBBBBBBBBB";

	/** The types repository. */
	@Autowired
	private TypesRepository							typesRepository;

	/** The types mapper. */
	@Autowired
	private TypesMapper								typesMapper;

	/** The types service. */
	@Autowired
	private TypesService							typesService;

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

	/** The rest types mock mvc. */
	private MockMvc									restTypesMockMvc;

	/** The types. */
	private Types									types;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TypesResource typesResource = new TypesResource(this.typesService);
		this.restTypesMockMvc = MockMvcBuilders.standaloneSetup(typesResource)
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
	 * @return the types
	 */
	public static Types createEntity(EntityManager em) {
		Types types = new Types().name(TypesResourceIntTest.DEFAULT_NAME);
		return types;
	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.types = TypesResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the types.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createTypes() throws Exception {
		int databaseSizeBeforeCreate = this.typesRepository.findAll().size();

		// Create the Types
		TypesDTO typesDTO = this.typesMapper.toDto(this.types);
		this.restTypesMockMvc
		.perform(MockMvcRequestBuilders.post("/api/types").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(typesDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Types in the database
		List<Types> typesList = this.typesRepository.findAll();
		Assertions.assertThat(typesList).hasSize(databaseSizeBeforeCreate + 1);
		Types testTypes = typesList.get(typesList.size() - 1);
		Assertions.assertThat(testTypes.getName()).isEqualTo(TypesResourceIntTest.DEFAULT_NAME);
	}

	/**
	 * Creates the types with existing id.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createTypesWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.typesRepository.findAll().size();

		// Create the Types with an existing ID
		this.types.setId(1L);
		TypesDTO typesDTO = this.typesMapper.toDto(this.types);

		// An entity with an existing ID cannot be created, so this API call must fail
		this.restTypesMockMvc
		.perform(MockMvcRequestBuilders.post("/api/types").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(typesDTO)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<Types> typesList = this.typesRepository.findAll();
		Assertions.assertThat(typesList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all types.
	 *
	 * @return the all types
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getAllTypes() throws Exception {
		// Initialize the database
		this.typesRepository.saveAndFlush(this.types);

		// Get all the typesList
		this.restTypesMockMvc.perform(MockMvcRequestBuilders.get("/api/types?sort=id,desc")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.types.getId().intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").value(Matchers.hasItem(TypesResourceIntTest.DEFAULT_NAME.toString())));
	}

	/**
	 * Gets the types.
	 *
	 * @return the types
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getTypes() throws Exception {
		// Initialize the database
		this.typesRepository.saveAndFlush(this.types);

		// Get the types
		this.restTypesMockMvc.perform(MockMvcRequestBuilders.get("/api/types/{id}", this.types.getId())).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.types.getId().intValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(TypesResourceIntTest.DEFAULT_NAME.toString()));
	}

	/**
	 * Gets the non existing types.
	 *
	 * @return the non existing types
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getNonExistingTypes() throws Exception {
		// Get the types
		this.restTypesMockMvc.perform(MockMvcRequestBuilders.get("/api/types/{id}", Long.MAX_VALUE))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update types.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateTypes() throws Exception {
		// Initialize the database
		this.typesRepository.saveAndFlush(this.types);
		int databaseSizeBeforeUpdate = this.typesRepository.findAll().size();

		// Update the types
		Types updatedTypes = this.typesRepository.findOne(this.types.getId());
		updatedTypes.name(TypesResourceIntTest.UPDATED_NAME);
		TypesDTO typesDTO = this.typesMapper.toDto(updatedTypes);

		this.restTypesMockMvc
		.perform(MockMvcRequestBuilders.put("/api/types").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(typesDTO)))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the Types in the database
		List<Types> typesList = this.typesRepository.findAll();
		Assertions.assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
		Types testTypes = typesList.get(typesList.size() - 1);
		Assertions.assertThat(testTypes.getName()).isEqualTo(TypesResourceIntTest.UPDATED_NAME);
	}

	/**
	 * Update non existing types.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingTypes() throws Exception {
		int databaseSizeBeforeUpdate = this.typesRepository.findAll().size();

		// Create the Types
		TypesDTO typesDTO = this.typesMapper.toDto(this.types);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		this.restTypesMockMvc
		.perform(MockMvcRequestBuilders.put("/api/types").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(typesDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Types in the database
		List<Types> typesList = this.typesRepository.findAll();
		Assertions.assertThat(typesList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete types.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void deleteTypes() throws Exception {
		// Initialize the database
		this.typesRepository.saveAndFlush(this.types);
		int databaseSizeBeforeDelete = this.typesRepository.findAll().size();

		// Get the types
		this.restTypesMockMvc.perform(
				MockMvcRequestBuilders.delete("/api/types/{id}", this.types.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<Types> typesList = this.typesRepository.findAll();
		Assertions.assertThat(typesList).hasSize(databaseSizeBeforeDelete - 1);
	}

	/**
	 * Equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Types.class);
		Types types1 = new Types();
		types1.setId(1L);
		Types types2 = new Types();
		types2.setId(types1.getId());
		Assertions.assertThat(types1).isEqualTo(types2);
		types2.setId(2L);
		Assertions.assertThat(types1).isNotEqualTo(types2);
		types1.setId(null);
		Assertions.assertThat(types1).isNotEqualTo(types2);
	}

	/**
	 * Dto equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(TypesDTO.class);
		TypesDTO typesDTO1 = new TypesDTO();
		typesDTO1.setId(1L);
		TypesDTO typesDTO2 = new TypesDTO();
		Assertions.assertThat(typesDTO1).isNotEqualTo(typesDTO2);
		typesDTO2.setId(typesDTO1.getId());
		Assertions.assertThat(typesDTO1).isEqualTo(typesDTO2);
		typesDTO2.setId(2L);
		Assertions.assertThat(typesDTO1).isNotEqualTo(typesDTO2);
		typesDTO1.setId(null);
		Assertions.assertThat(typesDTO1).isNotEqualTo(typesDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.typesMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.typesMapper.fromId(null)).isNull();
	}
}
