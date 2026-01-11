package co.ke.bluestron.bsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ke.bluestron.bsapi.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Spring Data JPA will implement basic CRUD operations automatically
    // Custom query methods can be added here
}