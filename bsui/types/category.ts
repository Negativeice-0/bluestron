export interface Category {
  id: number;                // BIGSERIAL → number
  name: string;              // VARCHAR(100) → string
  slug: string;              // VARCHAR(100) → string
  description?: string;      // TEXT → optional string
  createdAt: string;         // TIMESTAMP → ISO string
  updatedAt: string;         // TIMESTAMP → ISO string
}
