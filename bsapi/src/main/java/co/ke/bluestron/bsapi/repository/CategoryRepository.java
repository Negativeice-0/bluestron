package co.ke.bluestron.bsapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.bluestron.bsapi.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);  // Find a category by its URL handle
}