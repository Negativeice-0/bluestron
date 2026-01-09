package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.Testimonial;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    @Query("SELECT t FROM Testimonial t WHERE (:status IS NULL OR t.status = :status)")
    List<Testimonial> findByFilters(String status);
}