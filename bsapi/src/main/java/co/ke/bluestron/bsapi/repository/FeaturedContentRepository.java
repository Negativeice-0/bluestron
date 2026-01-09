package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.FeaturedContent;

public interface FeaturedContentRepository extends JpaRepository<FeaturedContent, Long> {
    @Query("SELECT f FROM FeaturedContent f WHERE (:section IS NULL OR f.section = :section) AND (:status IS NULL OR f.status = :status)")
    List<FeaturedContent> findByFilters(String section, String status);
}