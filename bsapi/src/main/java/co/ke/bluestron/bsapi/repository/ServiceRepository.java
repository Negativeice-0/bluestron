// File: src/main/java/co/ke/bluestron/bsapi/repository/ServiceRepository.java
package co.ke.bluestron.bsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    // Find services by category slug (navigates into Category entity)
    List<Service> findByCategory_Slug(String slug);
}
