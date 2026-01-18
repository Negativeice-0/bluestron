export interface Course {
  id: number;
  title: string;
  description: string;
  price: number;
  duration: string;
  isFeatured: boolean;
  categoryName?: string;
  imageUrl?: string;
}

export interface BlogPost {
  id: number;
  title: string;
  slug: string;
  content: string;
  authorName: string | null;
  published: boolean;
  createdAt: string | null;
}

export interface Registration {
  fullName: string;
  email: string;
  phone: string;
  organization?: string; // Optional per PDF
  role?: string;         // Optional per PDF
  status: 'PENDING' | 'CONFIRMED';
  paymentOption: 'Online' | 'Invoice'; 
  course: {
    id: number;
  };
}