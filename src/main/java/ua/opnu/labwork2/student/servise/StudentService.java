package ua.opnu.labwork2.student.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.enrollment.model.Enrollment;
import ua.opnu.labwork2.enrollment.repository.EnrollmentRepository;
import ua.opnu.labwork2.exception.DuplicateResourceException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.student.dto.StudentCreateRequest;
import ua.opnu.labwork2.student.dto.StudentUpdateRequest;
import ua.opnu.labwork2.student.model.Student;
import ua.opnu.labwork2.student.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Student create(StudentCreateRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Student email already exists");
        }
        Student student = new Student();
        apply(student, request);
        return repository.save(student);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    public Student update(Long id, StudentUpdateRequest request) {
        Student student = getById(id);
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateResourceException("Student email already exists");
        }
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setRegistrationDate(request.registrationDate());
        return repository.save(student);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Student", id);
        }
        repository.deleteById(id);
    }

    public List<Enrollment> getCourses(Long studentId) {
        getById(studentId);
        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getStudent() != null &&
                        e.getStudent().getId().equals(studentId))
                .toList();
    }

    private void apply(Student student, StudentCreateRequest request) {
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setRegistrationDate(request.registrationDate());
    }
}