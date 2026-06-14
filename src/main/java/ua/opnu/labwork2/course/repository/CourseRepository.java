package ua.opnu.labwork2.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.opnu.labwork2.course.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    @Query("""
            select c from Course c
            where (:level is null or lower(c.level) = lower(:level))
              and (lower(c.title) like lower(concat('%', :query, '%'))
                   or lower(c.description) like lower(concat('%', :query, '%')))
            """)
    List<Course> searchByQueryAndLevel(@Param("query") String query, @Param("level") String level);

    @Query("select c.level, count(c) from Course c group by c.level")
    List<Object[]> countCoursesByLevel();
}