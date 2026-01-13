'use client';

import { Course } from '@/types';
import { useState } from 'react';

interface Props {
  course: Course;
}

export default function RegistrationForm({ course }: Props) {
  const [status, setStatus] = useState<string | null>(null);

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const payload = Object.fromEntries(formData.entries());

    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/registrations`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...payload, courseSlug: course.slug }),
    });

    if (res.ok) {
      setStatus('Thank you for registering! Check your email for next steps.');
      e.currentTarget.reset();
    } else {
      setStatus('Registration failed. Please try again.');
    }
  }

  return (
    <div className="bg-white p-6 rounded-xl shadow">
      <h2 className="text-2xl font-bold text-purple-500 mb-4">Register Now</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input name="name" placeholder="Full Name" required className="w-full border p-2 rounded" />
        <input name="email" type="email" placeholder="Email Address" required className="w-full border p-2 rounded" />
        <input name="phone" type="tel" placeholder="Phone Number" required className="w-full border p-2 rounded" />
        <input name="organization" placeholder="Organization" className="w-full border p-2 rounded" />
        <input name="role" placeholder="Role/Designation" className="w-full border p-2 rounded" />
        <input name="preferredDate" type="date" className="w-full border p-2 rounded" />
        <select name="paymentOption" required className="w-full border p-2 rounded">
          <option value="">Select Payment Option</option>
          <option value="offline">Offline Payment</option>
          <option value="invoice">Invoice Me</option>
        </select>
        <button type="submit" className="w-full bg-orange-500 text-white py-2 rounded font-semibold hover:bg-orange-600">
          Enrol Now
        </button>
      </form>
      {status && <p className="mt-4 text-sm text-blue-600">{status}</p>}
    </div>
  );
}
