package ua.opnu.labwork2.module.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.course.model.Course;
import ua.opnu.labwork2.course.repository.CourseRepository;
import ua.opnu.labwork2.exception.ModuleOrderException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.module.dto.ModuleCreateRequest;
import ua.opnu.labwork2.module.dto.ModuleUpdateRequest;
import ua.opnu.labwork2.module.model.CourseModule;
import ua.opnu.labwork2.module.repository.ModuleRepository;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository repository;

    @Autowired
    private CourseRepository courseRepository;

    public CourseModule create(ModuleCreateRequest request) {
        if (repository.existsByCourseIdAndOrderIndex(request.courseId(), request.orderIndex())) {
            throw new ModuleOrderException("Module with this order index already exists in the course");
        }
        CourseModule module = new CourseModule();
        apply(module, request.title(), request.description(), request.orderIndex(), request.courseId());
        return repository.save(module);
    }

    public List<CourseModule> getAll() {
        return repository.findAll();
    }

    public CourseModule getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", id));
    }

    public CourseModule update(Long id, ModuleUpdateRequest request) {
        CourseModule module = getById(id);
        if (repository.existsByCourseIdAndOrderIndexAndIdNot(request.courseId(), request.orderIndex(), id)) {
            throw new ModuleOrderException("Module with this order index already exists in the course");
        }
        apply(module, request.title(), request.description(), request.orderIndex(), request.courseId());
        return repository.save(module);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Module", id);
        }
        repository.deleteById(id);
    }

    public CourseModule addToCourse(Long courseId, Long moduleId) {
        CourseModule module = getById(moduleId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
        if (repository.existsByCourseIdAndOrderIndexAndIdNot(courseId, module.getOrderIndex(), moduleId)) {
            throw new ModuleOrderException("Module with this order index already exists in the course");
        }
        module.setCourse(course);
        return repository.save(module);
    }

    public void removeFromCourse(Long courseId, Long moduleId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", courseId);
        }
        CourseModule module = getById(moduleId);
        if (module.getCourse() == null || !module.getCourse().getId().equals(courseId)) {
            throw new ResourceNotFoundException("Module in course", moduleId);
        }
        module.setCourse(null);
        repository.save(module);
    }

    private void apply(CourseModule module, String title, String description, Integer orderIndex, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
        module.setTitle(title);
        module.setDescription(description);
        module.setOrderIndex(orderIndex);
        module.setCourse(course);
    }
}