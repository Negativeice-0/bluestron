export interface Testimonial {
  id: number;                  // BIGSERIAL → number
  authorName: string;          // VARCHAR → string
  content: string;             // TEXT → string
  authorRole?: string;         // optional role/title
  authorCompany?: string;      // optional company
  authorImagePath?: string;    // optional avatar path
  rating?: number;             // optional rating (0–5 stars)
  featured?: boolean;          // optional featured flag
  createdAt?: string;          // TIMESTAMP → ISO string
}
