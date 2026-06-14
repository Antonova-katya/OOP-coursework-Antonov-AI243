package ua.opnu.labwork2.instructor.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.course.repository.CourseRepository;
import ua.opnu.labwork2.exception.DuplicateResourceException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.instructor.dto.InstructorCreateRequest;
import ua.opnu.labwork2.instructor.dto.InstructorUpdateRequest;
import ua.opnu.labwork2.instructor.model.Instructor;
import ua.opnu.labwork2.instructor.repository.InstructorRepository;

import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository repository;

    @Autowired
    private CourseRepository courseRepository;

    public Instructor create(InstructorCreateRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Instructor email already exists");
        }
        Instructor instructor = new Instructor();
        apply(instructor, request.firstName(), request.lastName(), request.email(), request.specialization());
        return repository.save(instructor);
    }

    public List<Instructor> getAll() {
        return repository.findAll();
    }

    public Instructor getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", id));
    }

    public Instructor update(Long id, InstructorUpdateRequest request) {
        Instructor instructor = getById(id);
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateResourceException("Instructor email already exists");
        }
        apply(instructor, request.firstName(), request.lastName(), request.email(), request.specialization());
        return repository.save(instructor);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Instructor", id);
        }
        repository.deleteById(id);
    }

    public List<Course> getCourses(Long instructorId) {
        getById(instructorId);
        return courseRepository.findAll()
                .stream()
                .filter(c -> c.getInstructor() != null &&
                        c.getInstructor().getId().equals(instructorId))
                .toList();
    }

    private void apply(Instructor instructor, String firstName, String lastName, String email, String specialization) {
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setEmail(email);
        instructor.setSpecialization(specialization);
    }
}