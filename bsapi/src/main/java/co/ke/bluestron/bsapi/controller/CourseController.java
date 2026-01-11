package co.ke.bluestron.bsapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.repository.CourseRepository;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @GetMapping("/featured")
    public List<Course> getFeaturedCourses() {
        return courseRepository.findByFeaturedTrue();
    }
    
    @GetMapping("/category/{categoryId}")
    public List<Course> getCoursesByCategory(@PathVariable Integer categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }
    
    @GetMapping("/{slug}")
    public Course getCourseBySlug(@PathVariable String slug) {
        return courseRepository.findBySlug(slug);
    }
}