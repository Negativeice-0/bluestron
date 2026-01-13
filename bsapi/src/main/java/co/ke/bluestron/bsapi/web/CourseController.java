package co.ke.bluestron.bsapi.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import co.ke.bluestron.bsapi.model.Course;
import co.ke.bluestron.bsapi.repository.CourseRepository;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseRepository repository;

    public CourseController(CourseRepository repo) {
        this.repository = repo;
    }

    // Unified GET endpoint with optional filters
    @GetMapping
    public List<Course> getCourses(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Boolean featured,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice
    ) {
        if (search != null) {
            return repository.findByTitleContainingIgnoreCase(search);
        } else if (categoryId != null) {
            return repository.findByCategory_Id(categoryId);
        } else if (featured != null && featured) {
            return repository.findByIsFeaturedTrue();
        } else if (minPrice != null && maxPrice != null) {
            return repository.findByPriceBetween(minPrice, maxPrice);
        }
        return repository.findAll();
    }

    // Dedicated endpoint for featured courses (optional convenience)
    @GetMapping("/featured")
    public List<Course> getFeatured() {
        return repository.findByIsFeaturedTrue();
    }

    // Get course by slug
    @GetMapping("/{slug}")
    public Course getBySlug(@PathVariable String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }
}
