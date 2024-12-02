package com.compica.filemasterpro.domain;

import static com.compica.filemasterpro.domain.FileTestSamples.*;
import static com.compica.filemasterpro.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.compica.filemasterpro.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void filesTest() {
        Project project = getProjectRandomSampleGenerator();
        File fileBack = getFileRandomSampleGenerator();

        project.addFiles(fileBack);
        assertThat(project.getFiles()).containsOnly(fileBack);
        assertThat(fileBack.getProject()).isEqualTo(project);

        project.removeFiles(fileBack);
        assertThat(project.getFiles()).doesNotContain(fileBack);
        assertThat(fileBack.getProject()).isNull();

        project.files(new HashSet<>(Set.of(fileBack)));
        assertThat(project.getFiles()).containsOnly(fileBack);
        assertThat(fileBack.getProject()).isEqualTo(project);

        project.setFiles(new HashSet<>());
        assertThat(project.getFiles()).doesNotContain(fileBack);
        assertThat(fileBack.getProject()).isNull();
    }
}
