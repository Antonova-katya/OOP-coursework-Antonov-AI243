package ua.opnu.labwork2.instructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.opnu.labwork2.instructor.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
