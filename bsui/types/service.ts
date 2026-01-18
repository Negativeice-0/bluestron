import { Category } from "./category";

// types/service.ts
export interface Service {
  id: number;                // BIGSERIAL → number
  category?: Category;       // ManyToOne → optional Category object
  title: string;             // VARCHAR → string
  slug: string;              // unique URL identifier
  description?: string;      // TEXT → optional string
  icon?: string;             // VARCHAR → optional string (used for mapping to emoji/icons)
  priceModel?: string;       // VARCHAR → optional string (e.g. "Hourly", "Fixed", "Subscription")
}
