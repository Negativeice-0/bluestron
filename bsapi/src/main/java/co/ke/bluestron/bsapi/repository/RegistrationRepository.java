package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ke.bluestron.bsapi.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    @Query("SELECT r FROM Registration r WHERE (:courseId IS NULL OR r.course.id = :courseId) AND (:paymentStatus IS NULL OR r.paymentStatus = :paymentStatus)")
    List<Registration> findByFilters(Long courseId, String paymentStatus);
}