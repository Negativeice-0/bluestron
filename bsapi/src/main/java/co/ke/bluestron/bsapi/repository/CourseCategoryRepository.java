package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.CourseCategory;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    Optional<CourseCategory> findBySlug(String slug);
    @Query("SELECT cc FROM CourseCategory cc WHERE (:status IS NULL OR cc.status = :status)")
    List<CourseCategory> findByFilters(String status);
}