package co.ke.bluestron.bsapi.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "trainer")
public class Trainer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 100) private String slug;
    @Column(nullable = false, length = 150) private String fullName;
    private String title; // e.g., "Senior Developer"
    @Column(columnDefinition = "TEXT") private String bio;
    private String photoUrl;
    private String twitter; private String linkedin; private String github;
    @Column(nullable = false, length = 20) private String status = "active";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    private String createdBy; private String updatedBy;
    
    // Getters
    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getFullName() { return fullName; }
    public String getTitle() { return title; }
    public String getBio() { return bio; }
    public String getPhotoUrl() { return photoUrl; }
    public String getTwitter() { return twitter; }
    public String getLinkedin() { return linkedin; }
    public String getGithub() { return github; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setTitle(String title) { this.title = title; }
    public void setBio(String bio) { this.bio = bio; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setTwitter(String twitter) { this.twitter = twitter; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public void setGithub(String github) { this.github = github; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public void touch() { this.updatedAt = Instant.now(); }
}