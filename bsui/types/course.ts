export interface Course {
  id: number;                // BIGSERIAL → number
  title: string;             // VARCHAR → string
  slug: string;              // URL-safe identifier
  description?: string;      // TEXT → optional string
  durationHours?: number;
  price?: number;
  isFeatured?: boolean;
}
