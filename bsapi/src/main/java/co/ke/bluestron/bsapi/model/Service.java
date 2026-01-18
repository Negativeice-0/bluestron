package co.ke.bluestron.bsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional link to a category; ignore during JSON serialization to prevent lazy-loading issues
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String title;                // Service name like "Data Analysis"

    @Column(nullable = false, unique = true)
    private String slug;                 // URL identifier

    @Column(columnDefinition = "TEXT")
    private String description;          // Detailed service offering

    private String icon;                 // Visual symbol representing the service
    private String priceModel;           // How this service is priced

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getPriceModel() { return priceModel; }
    public void setPriceModel(String priceModel) { this.priceModel = priceModel; }

    // --- Constructors ---
    public Service() {}

    public Service(Category category, String title, String slug,
                           String description, String icon, String priceModel) {
        this.category = category;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.icon = icon;
        this.priceModel = priceModel;
    }
}
