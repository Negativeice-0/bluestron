package co.ke.bluestron.bsapi.entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "course_instance")
public class CourseInstance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "course_id") private Course course;
    @Column(nullable = false, length = 20) private String mode; // online/in_person
    private LocalDate startDate; private LocalDate endDate;
    private Integer capacity;
    @Column(nullable = false, length = 20) private String status = "open";
    @ManyToOne @JoinColumn(name = "venue_id") private Venue venue;
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    private String createdBy; private String updatedBy;
    
    // Getters
    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public String getMode() { return mode; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Integer getCapacity() { return capacity; }
    public String getStatus() { return status; }
    public Venue getVenue() { return venue; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCourse(Course course) { this.course = course; }
    public void setMode(String mode) { this.mode = mode; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public void setStatus(String status) { this.status = status; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    // Laravel-like touch method
    public void touch() { this.updatedAt = Instant.now(); }
    
    // Business logic methods
    public boolean isOnline() { return "online".equals(mode); }
    public boolean isInPerson() { return "in_person".equals(mode); }
    public boolean isFull() { return capacity != null && capacity <= 0; }
    
    // Status change methods
    public void closeRegistration() { 
        this.status = "closed";
        this.touch();
    }
    
    public void openRegistration() {
        this.status = "open";
        this.touch();
    }
    
    // Duration helper (like Laravel accessor)
    public long getDurationInDays() {
        if (startDate == null || endDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
}