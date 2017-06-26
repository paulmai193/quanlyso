package logia.quanlyso.web.rest;

import logia.quanlyso.QuanlysoApp;

import logia.quanlyso.domain.Code;
import logia.quanlyso.repository.CodeRepository;
import logia.quanlyso.service.CodeService;
import logia.quanlyso.service.dto.CodeDTO;
import logia.quanlyso.service.mapper.CodeMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static logia.quanlyso.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CodeResource REST controller.
 *
 * @see CodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class CodeResourceIntTest {

    public static final Integer DEFAULT_CODE = 1;
    public static final Integer UPDATED_CODE = 2;

    public static final ZonedDateTime DEFAULT_OPEN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_OPEN_DATE = ZonedDateTime.now(ZoneId.systemDefault())
    		.withHour(0).withMinute(0).withSecond(0).withNano(0);

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CodeService codeService;

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
        CodeResource codeResource = new CodeResource(codeService);
        this.restCodeMockMvc = MockMvcBuilders.standaloneSetup(codeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Code createEntity(EntityManager em) {
        Code code = new Code()
            .code(DEFAULT_CODE)
            .openDate(DEFAULT_OPEN_DATE);
        return code;
    }

    @Before
    public void initTest() {
        code = createEntity(em);
    }

    @Test
    @Transactional
    public void createCode() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code
        CodeDTO codeDTO = codeMapper.toDto(code);
        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeCreate + 1);
        Code testCode = codeList.get(codeList.size() - 1);
        assertThat(testCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCode.getOpenDate()).isEqualTo(DEFAULT_OPEN_DATE);
    }

    @Test
    @Transactional
    public void createCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code with an existing ID
        code.setId(1L);
        CodeDTO codeDTO = codeMapper.toDto(code);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCodes() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get all the codeList
        restCodeMockMvc.perform(get("/api/codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(code.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].openDate").value(hasItem(sameInstant(DEFAULT_OPEN_DATE))));
    }

    @Test
    @Transactional
    public void getCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", code.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(code.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.openDate").value(sameInstant(DEFAULT_OPEN_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingCode() throws Exception {
        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Update the code
        Code updatedCode = codeRepository.findOne(code.getId());
        updatedCode
            .code(UPDATED_CODE)
            .openDate(UPDATED_OPEN_DATE);
        CodeDTO codeDTO = codeMapper.toDto(updatedCode);

        restCodeMockMvc.perform(put("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isOk());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeUpdate);
        Code testCode = codeList.get(codeList.size() - 1);
        assertThat(testCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCode.getOpenDate()).isEqualTo(UPDATED_OPEN_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCode() throws Exception {
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Create the Code
        CodeDTO codeDTO = codeMapper.toDto(code);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCodeMockMvc.perform(put("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeDelete = codeRepository.findAll().size();

        // Get the code
        restCodeMockMvc.perform(delete("/api/codes/{id}", code.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Code.class);
        Code code1 = new Code();
        code1.setId(1L);
        Code code2 = new Code();
        code2.setId(code1.getId());
        assertThat(code1).isEqualTo(code2);
        code2.setId(2L);
        assertThat(code1).isNotEqualTo(code2);
        code1.setId(null);
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeDTO.class);
        CodeDTO codeDTO1 = new CodeDTO();
        codeDTO1.setId(1L);
        CodeDTO codeDTO2 = new CodeDTO();
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO2.setId(codeDTO1.getId());
        assertThat(codeDTO1).isEqualTo(codeDTO2);
        codeDTO2.setId(2L);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO1.setId(null);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(codeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(codeMapper.fromId(null)).isNull();
    }
}
