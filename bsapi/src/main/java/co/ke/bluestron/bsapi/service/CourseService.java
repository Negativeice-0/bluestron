package co.ke.bluestron.bsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.ke.bluestron.bsapi.dto.CourseDTO;
import co.ke.bluestron.bsapi.entity.Course;
import co.ke.bluestron.bsapi.entity.CourseCategory;
import co.ke.bluestron.bsapi.repository.CourseCategoryRepository;
import co.ke.bluestron.bsapi.repository.CourseRepository;
import jakarta.transaction.Transactional;

@Service
public class CourseService {
    private final CourseRepository courseRepo;
    private final CourseCategoryRepository categoryRepo;

    public CourseService(CourseRepository courseRepo, CourseCategoryRepository categoryRepo) {
        this.courseRepo = courseRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Course> list(Long categoryId, String status) {
        return courseRepo.findByFilters(categoryId, status);
    }

    public Course getBySlug(String slug) {
        return courseRepo.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    @Transactional
    public Course create(CourseDTO dto, String actor) {
        CourseCategory cat = categoryRepo.findById(dto.categoryId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Course c = new Course();
        c.setSlug(dto.slug());
        c.setTitle(dto.title());
        c.setShortDescription(dto.shortDescription());
        c.setFullDescription(dto.fullDescription());
        c.setLearningOutcomes(dto.learningOutcomes());
        c.setThumbnailUrl(dto.thumbnailUrl());
        c.setCategory(cat);
        c.setStatus(dto.status() == null ? "draft" : dto.status());
        c.setCreatedBy(actor);
        c.setUpdatedBy(actor);
        return courseRepo.save(c);
    }
}
