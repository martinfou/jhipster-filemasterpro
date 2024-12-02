package com.compica.filemasterpro.web.rest;

import static com.compica.filemasterpro.domain.FileAsserts.*;
import static com.compica.filemasterpro.web.rest.TestUtil.createUpdateProxyForBean;
import static com.compica.filemasterpro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.compica.filemasterpro.IntegrationTest;
import com.compica.filemasterpro.domain.File;
import com.compica.filemasterpro.domain.enumeration.FileType;
import com.compica.filemasterpro.repository.FileRepository;
import com.compica.filemasterpro.service.dto.FileDTO;
import com.compica.filemasterpro.service.mapper.FileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final FileType DEFAULT_TYPE = FileType.INVOICE;
    private static final FileType UPDATED_TYPE = FileType.DOCUMENT;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final LocalDate DEFAULT_FILE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FILE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final byte[] DEFAULT_RAW_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RAW_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RAW_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RAW_FILE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_UPLOADED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileMockMvc;

    private File file;

    private File insertedFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity() {
        return new File()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .fileDate(DEFAULT_FILE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .hash(DEFAULT_HASH)
            .fileSize(DEFAULT_FILE_SIZE)
            .path(DEFAULT_PATH)
            .rawFile(DEFAULT_RAW_FILE)
            .rawFileContentType(DEFAULT_RAW_FILE_CONTENT_TYPE)
            .uploadedDate(DEFAULT_UPLOADED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity() {
        return new File()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .fileDate(UPDATED_FILE_DATE)
            .description(UPDATED_DESCRIPTION)
            .hash(UPDATED_HASH)
            .fileSize(UPDATED_FILE_SIZE)
            .path(UPDATED_PATH)
            .rawFile(UPDATED_RAW_FILE)
            .rawFileContentType(UPDATED_RAW_FILE_CONTENT_TYPE)
            .uploadedDate(UPDATED_UPLOADED_DATE);
    }

    @BeforeEach
    public void initTest() {
        file = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFile != null) {
            fileRepository.delete(insertedFile);
            insertedFile = null;
        }
    }

    @Test
    @Transactional
    void createFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);
        var returnedFileDTO = om.readValue(
            restFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FileDTO.class
        );

        // Validate the File in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFile = fileMapper.toEntity(returnedFileDTO);
        assertFileUpdatableFieldsEquals(returnedFile, getPersistedFile(returnedFile));

        insertedFile = returnedFile;
    }

    @Test
    @Transactional
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);
        FileDTO fileDTO = fileMapper.toDto(file);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        file.setName(null);

        // Create the File, which fails.
        FileDTO fileDTO = fileMapper.toDto(file);

        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        file.setType(null);

        // Create the File, which fails.
        FileDTO fileDTO = fileMapper.toDto(file);

        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(DEFAULT_FILE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].rawFileContentType").value(hasItem(DEFAULT_RAW_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].rawFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_RAW_FILE))))
            .andExpect(jsonPath("$.[*].uploadedDate").value(hasItem(DEFAULT_UPLOADED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc
            .perform(get(ENTITY_API_URL_ID, file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.fileDate").value(DEFAULT_FILE_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.rawFileContentType").value(DEFAULT_RAW_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.rawFile").value(Base64.getEncoder().encodeToString(DEFAULT_RAW_FILE)))
            .andExpect(jsonPath("$.uploadedDate").value(DEFAULT_UPLOADED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .fileDate(UPDATED_FILE_DATE)
            .description(UPDATED_DESCRIPTION)
            .hash(UPDATED_HASH)
            .fileSize(UPDATED_FILE_SIZE)
            .path(UPDATED_PATH)
            .rawFile(UPDATED_RAW_FILE)
            .rawFileContentType(UPDATED_RAW_FILE_CONTENT_TYPE)
            .uploadedDate(UPDATED_UPLOADED_DATE);
        FileDTO fileDTO = fileMapper.toDto(updatedFile);

        restFileMockMvc
            .perform(put(ENTITY_API_URL_ID, fileDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isOk());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFileToMatchAllProperties(updatedFile);
    }

    @Test
    @Transactional
    void putNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL_ID, fileDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .rawFile(UPDATED_RAW_FILE)
            .rawFileContentType(UPDATED_RAW_FILE_CONTENT_TYPE);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFile, file), getPersistedFile(file));
    }

    @Test
    @Transactional
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .fileDate(UPDATED_FILE_DATE)
            .description(UPDATED_DESCRIPTION)
            .hash(UPDATED_HASH)
            .fileSize(UPDATED_FILE_SIZE)
            .path(UPDATED_PATH)
            .rawFile(UPDATED_RAW_FILE)
            .rawFileContentType(UPDATED_RAW_FILE_CONTENT_TYPE)
            .uploadedDate(UPDATED_UPLOADED_DATE);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(partialUpdatedFile, getPersistedFile(partialUpdatedFile));
    }

    @Test
    @Transactional
    void patchNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the file
        restFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, file.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fileRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected File getPersistedFile(File file) {
        return fileRepository.findById(file.getId()).orElseThrow();
    }

    protected void assertPersistedFileToMatchAllProperties(File expectedFile) {
        assertFileAllPropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }

    protected void assertPersistedFileToMatchUpdatableProperties(File expectedFile) {
        assertFileAllUpdatablePropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }
}
