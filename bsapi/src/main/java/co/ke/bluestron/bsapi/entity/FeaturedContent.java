package co.ke.bluestron.bsapi.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "featured_content")
public class FeaturedContent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 50) private String section; // homepage/landing
    @Column(nullable = false, length = 100) private String contentType; // course/blog/service
    private Long contentId; // ID of the course/blog/service
    @Column(length = 200) private String customTitle;
    @Column(columnDefinition = "TEXT") private String customDescription;
    @Column(nullable = false) private Integer displayOrder = 0;
    @Column(nullable = false, length = 20) private String status = "active";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    private String createdBy; private String updatedBy;
    
    // Getters
    public Long getId() { return id; }
    public String getSection() { return section; }
    public String getContentType() { return contentType; }
    public Long getContentId() { return contentId; }
    public String getCustomTitle() { return customTitle; }
    public String getCustomDescription() { return customDescription; }
    public Integer getDisplayOrder() { return displayOrder; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSection(String section) { this.section = section; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public void setCustomTitle(String customTitle) { this.customTitle = customTitle; }
    public void setCustomDescription(String customDescription) { this.customDescription = customDescription; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public void touch() { this.updatedAt = Instant.now(); }
    public void moveUp() { this.displayOrder--; this.touch(); }
    public void moveDown() { this.displayOrder++; this.touch(); }
}