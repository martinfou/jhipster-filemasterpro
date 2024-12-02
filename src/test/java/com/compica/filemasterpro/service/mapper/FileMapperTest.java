package com.compica.filemasterpro.service.mapper;

import static com.compica.filemasterpro.domain.FileAsserts.*;
import static com.compica.filemasterpro.domain.FileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileMapperTest {

    private FileMapper fileMapper;

    @BeforeEach
    void setUp() {
        fileMapper = new FileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFileSample1();
        var actual = fileMapper.toEntity(fileMapper.toDto(expected));
        assertFileAllPropertiesEquals(expected, actual);
    }
}
