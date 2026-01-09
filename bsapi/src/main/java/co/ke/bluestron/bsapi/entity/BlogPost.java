package co.ke.bluestron.bsapi.entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "blog_post")
public class BlogPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 150) private String slug;
    @Column(nullable = false, length = 200) private String title;
    @Column(columnDefinition = "TEXT") private String excerpt;
    @Column(columnDefinition = "TEXT") private String content;
    private String featuredImage;
    private String author;
    private LocalDate publishedDate;
    @Column(nullable = false, length = 20) private String status = "draft";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    private String createdBy; private String updatedBy;
    
    // Getters
    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getExcerpt() { return excerpt; }
    public String getContent() { return content; }
    public String getFeaturedImage() { return featuredImage; }
    public String getAuthor() { return author; }
    public LocalDate getPublishedDate() { return publishedDate; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setTitle(String title) { this.title = title; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
    public void setContent(String content) { this.content = content; }
    public void setFeaturedImage(String featuredImage) { this.featuredImage = featuredImage; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public void touch() { this.updatedAt = Instant.now(); }
    public void publish() { 
        this.status = "published";
        if (this.publishedDate == null) this.publishedDate = LocalDate.now();
        this.touch();
    }
    public boolean isPublished() { return "published".equals(status); }
}