package com.compica.filemasterpro.service.dto;

import com.compica.filemasterpro.domain.enumeration.FileType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.compica.filemasterpro.domain.File} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private FileType type;

    private BigDecimal amount;

    private LocalDate fileDate;

    private String description;

    private String hash;

    private Long fileSize;

    private String path;

    @Lob
    private byte[] rawFile;

    private String rawFileContentType;

    private Instant uploadedDate;

    private ProjectDTO project;

    private VendorDTO vendor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getFileDate() {
        return fileDate;
    }

    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getRawFile() {
        return rawFile;
    }

    public void setRawFile(byte[] rawFile) {
        this.rawFile = rawFile;
    }

    public String getRawFileContentType() {
        return rawFileContentType;
    }

    public void setRawFileContentType(String rawFileContentType) {
        this.rawFileContentType = rawFileContentType;
    }

    public Instant getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Instant uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public VendorDTO getVendor() {
        return vendor;
    }

    public void setVendor(VendorDTO vendor) {
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDTO)) {
            return false;
        }

        FileDTO fileDTO = (FileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDTO{" +
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
            ", uploadedDate='" + getUploadedDate() + "'" +
            ", project=" + getProject() +
            ", vendor=" + getVendor() +
            "}";
    }
}
