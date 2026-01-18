export interface ContactSubmission {
  id?: number;
  name: string;
  email: string;
  subject?: string;
  message: string;
  submittedAt?: string; // TIMESTAMP → ISO string
}
