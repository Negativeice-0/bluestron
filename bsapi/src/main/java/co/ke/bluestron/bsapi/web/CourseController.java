package co.ke.bluestron.bsapi.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.model.Course;
import co.ke.bluestron.bsapi.repository.CourseRepository;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseRepository repository;

    public CourseController(CourseRepository repo) {
        this.repository = repo;
    }

    @GetMapping
    public List<Course> getAll() {
        return repository.findAll();
    }

    @GetMapping("/featured")
    public List<Course> getFeatured() {
        return repository.findByIsFeaturedTrue();  // Shine light on the starred courses
    }

    @GetMapping("/{slug}")
    public Course getBySlug(@PathVariable String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}