package co.ke.bluestron.bsapi.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import co.ke.bluestron.bsapi.dto.CategoryDTO;
import co.ke.bluestron.bsapi.entity.CourseCategory;
import co.ke.bluestron.bsapi.repository.CourseCategoryRepository;
import jakarta.transaction.Transactional;

/**
 * Business logic for categories:
 * - Prevent duplicate slugs
 * - Set audit fields
 * - Provide list/get/create/update/delete operations
 */
@Service
public class CategoryService {
    private final CourseCategoryRepository repo;

    public CategoryService(CourseCategoryRepository repo) {
        this.repo = repo;
    }

    public List<CourseCategory> list() {
        return repo.findAll();
    }

    public CourseCategory getBySlug(String slug) {
        return repo.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Transactional
    public CourseCategory create(CategoryDTO dto, String actor) {
        if (repo.existsBySlug(dto.slug())) {
            throw new IllegalArgumentException("Slug already exists");
        }
        CourseCategory c = new CourseCategory();
        c.setSlug(dto.slug());
        c.setName(dto.name());
        c.setDescription(dto.description());
        c.setStatus(dto.status());
        c.setCreatedBy(actor);
        c.setUpdatedBy(actor);
        return repo.save(c);
    }

    @Transactional
    public CourseCategory update(String slug, CategoryDTO dto, String actor) {
        CourseCategory c = getBySlug(slug);
        c.setName(dto.name());
        c.setDescription(dto.description());
        c.setStatus(dto.status());
        c.setUpdatedBy(actor);
        c.setUpdatedAt(Instant.now());
        return repo.save(c);
    }

    @Transactional
    public void delete(String slug) {
        CourseCategory c = getBySlug(slug);
        repo.delete(c);
    }
}
