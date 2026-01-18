'use client';

import { Course, Registration } from '@/types/index';
import React, { useState } from 'react';

interface Props {
  course: Course;
}

export default function RegistrationForm({ course }: Props) {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  // We mirror the exact JSON structure from your successful curl command
  const [formData, setFormData] = useState<Registration>({
    fullName: '',
    email: '',
    phone: '',
    organization: '',
    role: '',
    status: 'PENDING',
    paymentOption: 'Invoice',
    course: { id: course.id } // Pre-populated from the current course
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/registrations', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setSuccess(true);
        // Page 9 PDF requirement: Show confirmation message
      } else {
        const errorData = await response.json();
        console.error("Backend validation error:", errorData);
      }
    } catch (err) {
      console.error("Connection failed:", err);
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="p-8 text-center bg-green-50 rounded-xl border border-green-200">
        <h3 className="text-2xl font-bold text-green-800">Registration Received!</h3>
        <p className="text-green-700 mt-2">Check your email for the course details and invoice.</p>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded-xl shadow-lg border border-slate-200 space-y-4">
      <h3 className="text-xl font-bold text-blue-900 mb-4">Enroll in {course.title}</h3>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input 
          type="text" placeholder="Full Name" required
          className="p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
          onChange={e => setFormData({...formData, fullName: e.target.value})}
        />
        <input 
          type="email" placeholder="Email Address" required
          className="p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
          onChange={e => setFormData({...formData, email: e.target.value})}
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input 
          type="tel" placeholder="Phone Number"
          className="p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
          onChange={e => setFormData({...formData, phone: e.target.value})}
        />
        <select 
          className="p-3 border rounded-lg bg-white"
          onChange={e => setFormData({...formData, paymentOption: e.target.value as 'Online' | 'Invoice'})}
        >
          <option value="Invoice">Invoice Me</option>
          <option value="Online">Pay Online</option>
        </select>
      </div>

      <button 
        type="submit" 
        disabled={loading}
        className="w-full py-4 bg-orange-500 hover:bg-orange-600 text-white font-bold rounded-lg transition-all disabled:bg-slate-300 cursor-pointer"
      >
        {loading ? "Processing..." : "CONFIRM ENROLLMENT"}
      </button>
    </form>
  );
}