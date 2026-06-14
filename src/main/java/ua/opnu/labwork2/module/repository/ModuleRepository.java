package ua.opnu.labwork2.module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.opnu.labwork2.module.model.CourseModule;

@Repository
public interface ModuleRepository extends JpaRepository<CourseModule, Long> {
    boolean existsByCourseIdAndOrderIndex(Long courseId, Integer orderIndex);

    boolean existsByCourseIdAndOrderIndexAndIdNot(Long courseId, Integer orderIndex, Long id);
}