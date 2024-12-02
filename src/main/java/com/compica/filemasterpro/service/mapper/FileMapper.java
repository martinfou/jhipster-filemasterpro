package com.compica.filemasterpro.service.mapper;

import com.compica.filemasterpro.domain.File;
import com.compica.filemasterpro.domain.Project;
import com.compica.filemasterpro.domain.Vendor;
import com.compica.filemasterpro.service.dto.FileDTO;
import com.compica.filemasterpro.service.dto.ProjectDTO;
import com.compica.filemasterpro.service.dto.VendorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileMapper extends EntityMapper<FileDTO, File> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectId")
    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "vendorId")
    FileDTO toDto(File s);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);

    @Named("vendorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VendorDTO toDtoVendorId(Vendor vendor);
}
