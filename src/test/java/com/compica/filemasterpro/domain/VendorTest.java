package com.compica.filemasterpro.domain;

import static com.compica.filemasterpro.domain.FileTestSamples.*;
import static com.compica.filemasterpro.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.compica.filemasterpro.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VendorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendor.class);
        Vendor vendor1 = getVendorSample1();
        Vendor vendor2 = new Vendor();
        assertThat(vendor1).isNotEqualTo(vendor2);

        vendor2.setId(vendor1.getId());
        assertThat(vendor1).isEqualTo(vendor2);

        vendor2 = getVendorSample2();
        assertThat(vendor1).isNotEqualTo(vendor2);
    }

    @Test
    void fileTest() {
        Vendor vendor = getVendorRandomSampleGenerator();
        File fileBack = getFileRandomSampleGenerator();

        vendor.addFile(fileBack);
        assertThat(vendor.getFiles()).containsOnly(fileBack);
        assertThat(fileBack.getVendor()).isEqualTo(vendor);

        vendor.removeFile(fileBack);
        assertThat(vendor.getFiles()).doesNotContain(fileBack);
        assertThat(fileBack.getVendor()).isNull();

        vendor.files(new HashSet<>(Set.of(fileBack)));
        assertThat(vendor.getFiles()).containsOnly(fileBack);
        assertThat(fileBack.getVendor()).isEqualTo(vendor);

        vendor.setFiles(new HashSet<>());
        assertThat(vendor.getFiles()).doesNotContain(fileBack);
        assertThat(fileBack.getVendor()).isNull();
    }
}
