package com.compica.filemasterpro.service.mapper;

import static com.compica.filemasterpro.domain.VendorAsserts.*;
import static com.compica.filemasterpro.domain.VendorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VendorMapperTest {

    private VendorMapper vendorMapper;

    @BeforeEach
    void setUp() {
        vendorMapper = new VendorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVendorSample1();
        var actual = vendorMapper.toEntity(vendorMapper.toDto(expected));
        assertVendorAllPropertiesEquals(expected, actual);
    }
}
