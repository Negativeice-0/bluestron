package co.ke.bluestron.bsapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "testimonials")
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fixed to use TEXT as per your preference
    @Column(nullable = false, columnDefinition = "TEXT")
    private String authorName;          

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;             

    // FIXED: Mapped to 'author_role' to resolve your SQL 42703 error
    @Column(name = "author_role", columnDefinition = "TEXT")
    private String authorTitle;         

    // FIXED: Explicitly defined as TEXT to match your column addition
    @Column(name = "author_image_path", columnDefinition = "TEXT")
    private String authorImagePath;     

    @Column(nullable = false)
    private Boolean featured = false;   

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Constructors ---
    public Testimonial() {}

    public Testimonial(String authorName, String content, String authorTitle,
                       String authorImagePath, Boolean featured) {
        this.authorName = authorName;
        this.content = content;
        this.authorTitle = authorTitle;
        this.authorImagePath = authorImagePath;
        this.featured = featured;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters (No changes needed here) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorTitle() { return authorTitle; }
    public void setAuthorTitle(String authorTitle) { this.authorTitle = authorTitle; }

    public String getAuthorImagePath() { return authorImagePath; }
    public void setAuthorImagePath(String authorImagePath) { this.authorImagePath = authorImagePath; }

    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}