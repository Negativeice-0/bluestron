package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    // Find all registrations for a given course
    List<Registration> findByCourse_Id(Long courseId);

    // Find registrations by email
    List<Registration> findByEmail(String email);

    // Optional: filter by status
    List<Registration> findByStatus(String status);
}
