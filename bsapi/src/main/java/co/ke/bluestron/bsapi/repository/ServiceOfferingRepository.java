package co.ke.bluestron.bsapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.ServiceOffering;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
    Optional<ServiceOffering> findBySlug(String slug);
    @Query("SELECT s FROM ServiceOffering s WHERE (:status IS NULL OR s.status = :status)")
    List<ServiceOffering> findByFilters(String status);
}