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

@Entity @Table(name = "registration")
public class Registration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "course_id") private Course course;
    @ManyToOne @JoinColumn(name = "course_instance_id") private CourseInstance courseInstance;
    @Column(nullable = false, length = 150) private String fullName;
    @Column(nullable = false, length = 150) private String email;
    private String phone; private String organization; private String role;
    private LocalDate preferredDate;
    @Column(nullable = false, length = 20) private String paymentOption; // online/invoice
    @Column(nullable = false, length = 20) private String paymentStatus = "pending";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    private String createdBy; private String updatedBy;
    
    // Getters
    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public CourseInstance getCourseInstance() { return courseInstance; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getOrganization() { return organization; }
    public String getRole() { return role; }
    public LocalDate getPreferredDate() { return preferredDate; }
    public String getPaymentOption() { return paymentOption; }
    public String getPaymentStatus() { return paymentStatus; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCourse(Course course) { this.course = course; }
    public void setCourseInstance(CourseInstance courseInstance) { this.courseInstance = courseInstance; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setOrganization(String organization) { this.organization = organization; }
    public void setRole(String role) { this.role = role; }
    public void setPreferredDate(LocalDate preferredDate) { this.preferredDate = preferredDate; }
    public void setPaymentOption(String paymentOption) { this.paymentOption = paymentOption; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    // Laravel-like touch method
    public void touch() { this.updatedAt = Instant.now(); }
    
    // Payment status management (like Laravel state transitions)
    public void markAsPaid() {
        this.paymentStatus = "paid";
        this.touch();
    }
    
    public void markAsPending() {
        this.paymentStatus = "pending";
        this.touch();
    }
    
    public void markAsFailed() {
        this.paymentStatus = "failed";
        this.touch();
    }
    
    // Business logic methods
    public boolean isPaid() { return "paid".equals(paymentStatus); }
    public boolean isPending() { return "pending".equals(paymentStatus); }
    public boolean isOnlinePayment() { return "online".equals(paymentOption); }
    public boolean isInvoicePayment() { return "invoice".equals(paymentOption); }
    
    // Laravel-like attribute accessor for display
    public String getFormattedRegistration() {
        return String.format("%s - %s (%s)", fullName, email, course != null ? course.getTitle() : "N/A");
    }
}