'use client';

import { useState } from 'react';

interface Props {
  courseId: number;
}

export default function RegistrationForm({ courseId }: Props) {
  const [email, setEmail] = useState('');
  const [status, setStatus] = useState<'idle' | 'loading' | 'success' | 'error'>('idle');

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setStatus('loading');
    try {
      const res = await fetch(`${process.env.API_URL}/api/courses/${courseId}/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email }),
      });
      if (!res.ok) throw new Error('Registration failed');
      setStatus('success');
    } catch {
      setStatus('error');
    }
  }

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded-xl shadow">
      <h3 className="text-xl font-bold mb-4">Register for this course</h3>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Your email"
        className="w-full border rounded p-2 mb-4"
        required
      />
      <button
        type="submit"
        disabled={status === 'loading'}
        className="w-full bg-purple-500 text-white py-2 rounded hover:bg-purple-600 transition"
      >
        {status === 'loading' ? 'Submitting...' : 'Register'}
      </button>
      {status === 'success' && <p className="text-green-600 mt-2">Registration successful!</p>}
      {status === 'error' && <p className="text-red-600 mt-2">Something went wrong.</p>}
    </form>
  );
}
