package com.rez.melee.slippi.domain;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import javax.validation.constraints.*;

import java.io.IOException;
import java.io.Serializable;

/**
 * A SlippiFile.
 */
@Document(collection = "slippi_file")
public class SlippiFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 5, max = 255)
    @Field("uploaded_by")
    private String uploadedBy;

    @NotNull
    @Size(min = 5, max = 255)
    @Field("file_name")
    private String fileName;

    @NotNull
    @Field("hash_value")
    private String hashValue;

    public SlippiFile(@Nonnull MultipartFile file) {
        this.file = file;
        this.setFileName(file.getOriginalFilename());
    }

    public SlippiFile() {
    }

    MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Transient
    @JsonDeserialize
    private MultipartFile file;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public SlippiFile uploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
        return this;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getFileName() {
        return fileName;
    }

    public SlippiFile fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHashValue() {
        return hashValue;
    }

    public SlippiFile hashValue(String hashValue) {
        this.hashValue = hashValue;
        return this;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlippiFile)) {
            return false;
        }
        return id != null && id.equals(((SlippiFile) o).id);
    }

    @Override
    public int hashCode() {
        return getHashValue().hashCode();
    }

    @Override
    public String toString() {
        return "SlippiFile{" +
            "id=" + getId() +
            ", uploadedBy='" + getUploadedBy() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", hashValue='" + getHashValue() + "'" +
            "}";
    }

    public byte[] fileBytes() throws IOException {
        if(file == null) return null;
        return file.getBytes();
    }
}
