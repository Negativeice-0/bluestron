export interface Category {
  id: number;              // Unique fingerprint of this category
  name: string;            // Public title
  slug: string;            // URL‑safe alias
  description?: string;    // Brief compass pointing to its content
  createdAt: string;       // ISO timestamp when born
  updatedAt: string;       // ISO timestamp when polished
}
