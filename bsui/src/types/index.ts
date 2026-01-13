export interface Category {
  id: number;              // Unique fingerprint of this category
  name: string;            // Public title
  slug: string;            // URL‑safe alias
  description?: string;    // Brief compass pointing to its content
  createdAt: string;       // ISO timestamp when born
  updatedAt: string;       // ISO timestamp when polished
}

export interface Course {
  id: number;
  category: Category;
  title: string;
  slug: string;
  description?: string;
  durationHours?: number;
  price?: number;
  isFeatured: boolean;
  learningOutcomes?: string[]; // optional array for "What You'll Learn"
}
