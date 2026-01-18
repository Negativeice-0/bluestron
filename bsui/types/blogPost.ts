export interface BlogPost {
  id: number;                // BIGSERIAL → number
  title: string;             // VARCHAR → string
  slug: string;              // unique URL identifier
  excerpt?: string;          // TEXT → optional string
  content: string;           // TEXT → string
  authorName?: string;       // VARCHAR → optional string
  coverImagePath?: string;   // VARCHAR → optional string (path to image file)
  published: boolean;        // BOOLEAN → true/false
  publishedAt?: string;      // TIMESTAMP → ISO string (optional if draft)
  createdAt?: string;        // TIMESTAMP → ISO string
  updatedAt?: string;        // TIMESTAMP → ISO string
}
