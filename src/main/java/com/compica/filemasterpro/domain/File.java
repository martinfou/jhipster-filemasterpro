package com.compica.filemasterpro.domain;

import com.compica.filemasterpro.domain.enumeration.FileType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FileType type;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "file_date")
    private LocalDate fileDate;

    @Column(name = "description")
    private String description;

    @Column(name = "hash")
    private String hash;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "path")
    private String path;

    @Lob
    @Column(name = "raw_file")
    private byte[] rawFile;

    @Column(name = "raw_file_content_type")
    private String rawFileContentType;

    @Column(name = "uploaded_date")
    private Instant uploadedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "files" }, allowSetters = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "files" }, allowSetters = true)
    private Vendor vendor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public File name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return this.type;
    }

    public File type(FileType type) {
        this.setType(type);
        return this;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public File amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getFileDate() {
        return this.fileDate;
    }

    public File fileDate(LocalDate fileDate) {
        this.setFileDate(fileDate);
        return this;
    }

    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    public String getDescription() {
        return this.description;
    }

    public File description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHash() {
        return this.hash;
    }

    public File hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public File fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return this.path;
    }

    public File path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getRawFile() {
        return this.rawFile;
    }

    public File rawFile(byte[] rawFile) {
        this.setRawFile(rawFile);
        return this;
    }

    public void setRawFile(byte[] rawFile) {
        this.rawFile = rawFile;
    }

    public String getRawFileContentType() {
        return this.rawFileContentType;
    }

    public File rawFileContentType(String rawFileContentType) {
        this.rawFileContentType = rawFileContentType;
        return this;
    }

    public void setRawFileContentType(String rawFileContentType) {
        this.rawFileContentType = rawFileContentType;
    }

    public Instant getUploadedDate() {
        return this.uploadedDate;
    }

    public File uploadedDate(Instant uploadedDate) {
        this.setUploadedDate(uploadedDate);
        return this;
    }

    public void setUploadedDate(Instant uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public File project(Project project) {
        this.setProject(project);
        return this;
    }

    public Vendor getVendor() {
        return this.vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public File vendor(Vendor vendor) {
        this.setVendor(vendor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return getId() != null && getId().equals(((File) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", fileDate='" + getFileDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", hash='" + getHash() + "'" +
            ", fileSize=" + getFileSize() +
            ", path='" + getPath() + "'" +
            ", rawFile='" + getRawFile() + "'" +
            ", rawFileContentType='" + getRawFileContentType() + "'" +
            ", uploadedDate='" + getUploadedDate() + "'" +
            "}";
    }
}
