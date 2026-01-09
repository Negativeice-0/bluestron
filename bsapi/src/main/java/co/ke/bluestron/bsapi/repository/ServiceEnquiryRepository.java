package co.ke.bluestron.bsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.bluestron.bsapi.entity.ServiceEnquiry;

public interface ServiceEnquiryRepository extends JpaRepository<ServiceEnquiry, Long> {
    // Basic CRUD operations provided by JpaRepository
}