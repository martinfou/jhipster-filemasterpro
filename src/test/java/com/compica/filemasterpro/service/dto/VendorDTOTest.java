package com.compica.filemasterpro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.compica.filemasterpro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VendorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VendorDTO.class);
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setId(1L);
        VendorDTO vendorDTO2 = new VendorDTO();
        assertThat(vendorDTO1).isNotEqualTo(vendorDTO2);
        vendorDTO2.setId(vendorDTO1.getId());
        assertThat(vendorDTO1).isEqualTo(vendorDTO2);
        vendorDTO2.setId(2L);
        assertThat(vendorDTO1).isNotEqualTo(vendorDTO2);
        vendorDTO1.setId(null);
        assertThat(vendorDTO1).isNotEqualTo(vendorDTO2);
    }
}
