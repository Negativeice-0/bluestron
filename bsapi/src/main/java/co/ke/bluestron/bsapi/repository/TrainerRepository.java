package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findBySlug(String slug);
    @Query("SELECT t FROM Trainer t WHERE (:status IS NULL OR t.status = :status)")
    List<Trainer> findByFilters(String status);
}