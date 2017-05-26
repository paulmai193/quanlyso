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
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    
    /** The Constant UPDATED_NAME. */
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    /** The style repository. */
    @Autowired
    private StyleRepository styleRepository;

    /** The style mapper. */
    @Autowired
    private StyleMapper styleMapper;

    /** The style service. */
    @Autowired
    private StyleService styleService;

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

    /** The rest style mock mvc. */
    private MockMvc restStyleMockMvc;

    /** The style. */
    private Style style;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StyleResource styleResource = new StyleResource(styleService);
        this.restStyleMockMvc = MockMvcBuilders.standaloneSetup(styleResource)
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
     * @return the style
     */
    public static Style createEntity(EntityManager em) {
        Style style = new Style()
            .name(DEFAULT_NAME);
        return style;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        style = createEntity(em);
    }

    /**
     * Creates the style.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createStyle() throws Exception {
        int databaseSizeBeforeCreate = styleRepository.findAll().size();

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);
        restStyleMockMvc.perform(post("/api/styles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isCreated());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeCreate + 1);
        Style testStyle = styleList.get(styleList.size() - 1);
        assertThat(testStyle.getName()).isEqualTo(DEFAULT_NAME);
    }

    /**
     * Creates the style with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createStyleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = styleRepository.findAll().size();

        // Create the Style with an existing ID
        style.setId(1L);
        StyleDTO styleDTO = styleMapper.toDto(style);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStyleMockMvc.perform(post("/api/styles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeCreate);
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
        styleRepository.saveAndFlush(style);

        // Get all the styleList
        restStyleMockMvc.perform(get("/api/styles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(style.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
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
        styleRepository.saveAndFlush(style);

        // Get the style
        restStyleMockMvc.perform(get("/api/styles/{id}", style.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(style.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
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
        restStyleMockMvc.perform(get("/api/styles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
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
        styleRepository.saveAndFlush(style);
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();

        // Update the style
        Style updatedStyle = styleRepository.findOne(style.getId());
        updatedStyle
            .name(UPDATED_NAME);
        StyleDTO styleDTO = styleMapper.toDto(updatedStyle);

        restStyleMockMvc.perform(put("/api/styles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isOk());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
        Style testStyle = styleList.get(styleList.size() - 1);
        assertThat(testStyle.getName()).isEqualTo(UPDATED_NAME);
    }

    /**
     * Update non existing style.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStyleMockMvc.perform(put("/api/styles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isCreated());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate + 1);
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
        styleRepository.saveAndFlush(style);
        int databaseSizeBeforeDelete = styleRepository.findAll().size();

        // Get the style
        restStyleMockMvc.perform(delete("/api/styles/{id}", style.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeDelete - 1);
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
        assertThat(style1).isEqualTo(style2);
        style2.setId(2L);
        assertThat(style1).isNotEqualTo(style2);
        style1.setId(null);
        assertThat(style1).isNotEqualTo(style2);
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
        assertThat(styleDTO1).isNotEqualTo(styleDTO2);
        styleDTO2.setId(styleDTO1.getId());
        assertThat(styleDTO1).isEqualTo(styleDTO2);
        styleDTO2.setId(2L);
        assertThat(styleDTO1).isNotEqualTo(styleDTO2);
        styleDTO1.setId(null);
        assertThat(styleDTO1).isNotEqualTo(styleDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(styleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(styleMapper.fromId(null)).isNull();
    }
}
