package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.CourseInstance;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Long> {
    @Query("SELECT ci FROM CourseInstance ci WHERE (:courseId IS NULL OR ci.course.id = :courseId) AND (:status IS NULL OR ci.status = :status)")
    List<CourseInstance> findByFilters(Long courseId, String status);
}