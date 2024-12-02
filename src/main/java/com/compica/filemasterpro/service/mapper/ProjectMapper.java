package com.compica.filemasterpro.service.mapper;

import com.compica.filemasterpro.domain.Project;
import com.compica.filemasterpro.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
