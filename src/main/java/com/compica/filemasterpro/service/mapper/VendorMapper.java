package com.compica.filemasterpro.service.mapper;

import com.compica.filemasterpro.domain.Vendor;
import com.compica.filemasterpro.service.dto.VendorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vendor} and its DTO {@link VendorDTO}.
 */
@Mapper(componentModel = "spring")
public interface VendorMapper extends EntityMapper<VendorDTO, Vendor> {}
