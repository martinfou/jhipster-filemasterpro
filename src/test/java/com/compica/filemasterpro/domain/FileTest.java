package com.compica.filemasterpro.domain;

import static com.compica.filemasterpro.domain.FileTestSamples.*;
import static com.compica.filemasterpro.domain.ProjectTestSamples.*;
import static com.compica.filemasterpro.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.compica.filemasterpro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(File.class);
        File file1 = getFileSample1();
        File file2 = new File();
        assertThat(file1).isNotEqualTo(file2);

        file2.setId(file1.getId());
        assertThat(file1).isEqualTo(file2);

        file2 = getFileSample2();
        assertThat(file1).isNotEqualTo(file2);
    }

    @Test
    void projectTest() {
        File file = getFileRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        file.setProject(projectBack);
        assertThat(file.getProject()).isEqualTo(projectBack);

        file.project(null);
        assertThat(file.getProject()).isNull();
    }

    @Test
    void vendorTest() {
        File file = getFileRandomSampleGenerator();
        Vendor vendorBack = getVendorRandomSampleGenerator();

        file.setVendor(vendorBack);
        assertThat(file.getVendor()).isEqualTo(vendorBack);

        file.vendor(null);
        assertThat(file.getVendor()).isNull();
    }
}
