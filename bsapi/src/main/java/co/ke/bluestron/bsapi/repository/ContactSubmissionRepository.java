package co.ke.bluestron.bsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.ContactSubmission;

@Repository
public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {
    // You can add custom queries here if needed, e.g. findByEmail, findByName, etc.
}
