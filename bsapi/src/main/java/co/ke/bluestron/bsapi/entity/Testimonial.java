package co.ke.bluestron.bsapi.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "testimonial")
public class Testimonial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 150) private String authorName;
    private String authorRole; private String authorCompany;
    @Column(columnDefinition = "TEXT", nullable = false) private String content;
    private String photoUrl;
    private Integer rating; // 1-5 stars
    @Column(nullable = false, length = 20) private String status = "active";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    private String createdBy; private String updatedBy;
    
    // Getters
    public Long getId() { return id; }
    public String getAuthorName() { return authorName; }
    public String getAuthorRole() { return authorRole; }
    public String getAuthorCompany() { return authorCompany; }
    public String getContent() { return content; }
    public String getPhotoUrl() { return photoUrl; }
    public Integer getRating() { return rating; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }
    public void setAuthorCompany(String authorCompany) { this.authorCompany = authorCompany; }
    public void setContent(String content) { this.content = content; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public void touch() { this.updatedAt = Instant.now(); }
    public String getAuthorInfo() {
        if (authorRole == null && authorCompany == null) return authorName;
        if (authorCompany == null) return authorName + ", " + authorRole;
        return authorName + ", " + authorRole + " at " + authorCompany;
    }
}