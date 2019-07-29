package com.rez.melee.slippi.web.rest;

import com.rez.melee.slippi.UploadserviceApp;
import com.rez.melee.slippi.domain.SlippiFile;
import com.rez.melee.slippi.repository.SlippiFileRepository;
import com.rez.melee.slippi.service.SlippiFileService;
import com.rez.melee.slippi.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static com.rez.melee.slippi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link SlippiFileResource} REST controller.
 */
@SpringBootTest(classes = UploadserviceApp.class)
public class SlippiFileResourceIT {

    private static final String DEFAULT_UPLOADED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPLOADED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HASH_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_HASH_VALUE = "BBBBBBBBBB";

    @Autowired
    private SlippiFileRepository slippiFileRepository;

    @Autowired
    private SlippiFileService slippiFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSlippiFileMockMvc;

    private SlippiFile slippiFile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlippiFileResource slippiFileResource = new SlippiFileResource(slippiFileService);
        this.restSlippiFileMockMvc = MockMvcBuilders.standaloneSetup(slippiFileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlippiFile createEntity() {
        SlippiFile slippiFile = new SlippiFile()
            .uploadedBy(DEFAULT_UPLOADED_BY)
            .fileName(DEFAULT_FILE_NAME)
            .hashValue(DEFAULT_HASH_VALUE);
        return slippiFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlippiFile createUpdatedEntity() {
        SlippiFile slippiFile = new SlippiFile()
            .uploadedBy(UPDATED_UPLOADED_BY)
            .fileName(UPDATED_FILE_NAME)
            .hashValue(UPDATED_HASH_VALUE);
        return slippiFile;
    }

    @BeforeEach
    public void initTest() {
        slippiFileRepository.deleteAll();
        slippiFile = createEntity();
    }

    @Test
    public void createSlippiFile() throws Exception {
        int databaseSizeBeforeCreate = slippiFileRepository.findAll().size();

        // Create the SlippiFile
        restSlippiFileMockMvc.perform(post("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slippiFile)))
            .andExpect(status().isCreated());

        // Validate the SlippiFile in the database
        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeCreate + 1);
        SlippiFile testSlippiFile = slippiFileList.get(slippiFileList.size() - 1);
        assertThat(testSlippiFile.getUploadedBy()).isEqualTo(DEFAULT_UPLOADED_BY);
        assertThat(testSlippiFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testSlippiFile.getHashValue()).isEqualTo(DEFAULT_HASH_VALUE);
    }

    @Test
    public void createSlippiFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slippiFileRepository.findAll().size();

        // Create the SlippiFile with an existing ID
        slippiFile.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlippiFileMockMvc.perform(post("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slippiFile)))
            .andExpect(status().isBadRequest());

        // Validate the SlippiFile in the database
        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkUploadedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = slippiFileRepository.findAll().size();
        // set the field null
        slippiFile.setUploadedBy(null);

        // Create the SlippiFile, which fails.

        restSlippiFileMockMvc.perform(post("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slippiFile)))
            .andExpect(status().isBadRequest());

        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = slippiFileRepository.findAll().size();
        // set the field null
        slippiFile.setFileName(null);

        // Create the SlippiFile, which fails.

        restSlippiFileMockMvc.perform(post("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slippiFile)))
            .andExpect(status().isBadRequest());

        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkHashValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = slippiFileRepository.findAll().size();
        // set the field null
        slippiFile.setHashValue(null);

        // Create the SlippiFile, which fails.

        restSlippiFileMockMvc.perform(post("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slippiFile)))
            .andExpect(status().isBadRequest());

        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSlippiFiles() throws Exception {
        // Initialize the database
        slippiFileRepository.save(slippiFile);

        // Get all the slippiFileList
        restSlippiFileMockMvc.perform(get("/api/slippi-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slippiFile.getId())))
            .andExpect(jsonPath("$.[*].uploadedBy").value(hasItem(DEFAULT_UPLOADED_BY.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].hashValue").value(hasItem(DEFAULT_HASH_VALUE.toString())));
    }
    
    @Test
    public void getSlippiFile() throws Exception {
        // Initialize the database
        slippiFileRepository.save(slippiFile);

        // Get the slippiFile
        restSlippiFileMockMvc.perform(get("/api/slippi-files/{id}", slippiFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slippiFile.getId()))
            .andExpect(jsonPath("$.uploadedBy").value(DEFAULT_UPLOADED_BY.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.hashValue").value(DEFAULT_HASH_VALUE.toString()));
    }

    @Test
    public void getNonExistingSlippiFile() throws Exception {
        // Get the slippiFile
        restSlippiFileMockMvc.perform(get("/api/slippi-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSlippiFile() throws Exception {
        // Initialize the database
        slippiFileService.save(slippiFile);

        int databaseSizeBeforeUpdate = slippiFileRepository.findAll().size();

        // Update the slippiFile
        SlippiFile updatedSlippiFile = slippiFileRepository.findById(slippiFile.getId()).get();
        updatedSlippiFile
            .uploadedBy(UPDATED_UPLOADED_BY)
            .fileName(UPDATED_FILE_NAME)
            .hashValue(UPDATED_HASH_VALUE);

        restSlippiFileMockMvc.perform(put("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlippiFile)))
            .andExpect(status().isOk());

        // Validate the SlippiFile in the database
        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeUpdate);
        SlippiFile testSlippiFile = slippiFileList.get(slippiFileList.size() - 1);
        assertThat(testSlippiFile.getUploadedBy()).isEqualTo(UPDATED_UPLOADED_BY);
        assertThat(testSlippiFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSlippiFile.getHashValue()).isEqualTo(UPDATED_HASH_VALUE);
    }

    @Test
    public void updateNonExistingSlippiFile() throws Exception {
        int databaseSizeBeforeUpdate = slippiFileRepository.findAll().size();

        // Create the SlippiFile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlippiFileMockMvc.perform(put("/api/slippi-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slippiFile)))
            .andExpect(status().isBadRequest());

        // Validate the SlippiFile in the database
        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSlippiFile() throws Exception {
        // Initialize the database
        slippiFileService.save(slippiFile);

        int databaseSizeBeforeDelete = slippiFileRepository.findAll().size();

        // Delete the slippiFile
        restSlippiFileMockMvc.perform(delete("/api/slippi-files/{id}", slippiFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SlippiFile> slippiFileList = slippiFileRepository.findAll();
        assertThat(slippiFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlippiFile.class);
        SlippiFile slippiFile1 = new SlippiFile();
        slippiFile1.setId("id1");
        SlippiFile slippiFile2 = new SlippiFile();
        slippiFile2.setId(slippiFile1.getId());
        assertThat(slippiFile1).isEqualTo(slippiFile2);
        slippiFile2.setId("id2");
        assertThat(slippiFile1).isNotEqualTo(slippiFile2);
        slippiFile1.setId(null);
        assertThat(slippiFile1).isNotEqualTo(slippiFile2);
    }
}
