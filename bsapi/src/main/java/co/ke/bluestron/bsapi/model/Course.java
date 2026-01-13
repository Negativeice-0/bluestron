package co.ke.bluestron.bsapi.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;          // The parent category that hosts this course

    @Column(nullable = false)
    private String title;               // The course's banner headline

    @Column(nullable = false, unique = true)
    private String slug;                // The course's digital call sign

    private String description;         // The detailed map of what will be learned
    
    private Integer durationHours;      // The time investment required
    
    private BigDecimal price;           // The toll for passage
    
    private Boolean isFeatured = false; // Whether this course is spotlighted

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public Integer getDurationHours() {
        return durationHours;
    }
    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }
    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    // --- Constructors (optional convenience) ---
    public Course() {
        // Default constructor
    }

    public Course(Category category, String title, String slug, String description,
                  Integer durationHours, BigDecimal price, Boolean isFeatured) {
        this.category = category;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.durationHours = durationHours;
        this.price = price;
        this.isFeatured = isFeatured;
    }
}
