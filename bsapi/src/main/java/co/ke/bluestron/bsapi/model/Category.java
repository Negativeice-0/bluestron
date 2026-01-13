package co.ke.bluestron.bsapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // The unique fingerprint of this category
    
    @Column(nullable = false, unique = true)
    private String name;                // The public title
    
    @Column(nullable = false, unique = true)
    private String slug;                // The URL‑safe alias
    
    private String description;         // A brief compass pointing to its content
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;    // The moment this category was born
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;    // The last time it was polished

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- Constructors (optional convenience) ---
    public Category() {
        // Default constructor
    }

    public Category(String name, String slug, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
