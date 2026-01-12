'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';

// This is the REGISTRATION PAGE
// Place this file at: src/app/register/page.tsx
// It handles user registration and then redirects to login.

export default function RegisterPage() {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [fullName, setFullName] = useState<string>('');
  const [error, setError] = useState<string>('');
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

    try {
      const res = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email,
          password,
          fullName,
          role: 'USER', // hardcoded default role
          phone: '+254700000000', // hardcoded placeholder
          organization: 'Bluestron Ltd', // hardcoded placeholder
        }),
      });

      if (res.ok) {
        router.push('/login'); // after registration, go to login
      } else {
        setError('Registration failed. Please try again.');
      }
    } catch (err) {
      setError('An error occurred during registration.');
      console.error(err);
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-softwhite">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-lg shadow-md w-96 border border-navyblue"
      >
        <h2 className="text-2xl mb-4 font-bold text-navyblue">Register</h2>

        {error && <p className="text-orange-500 text-sm mb-2">{error}</p>}

        <input
          type="text"
          placeholder="Full Name"
          required
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          className="w-full p-2 mb-3 border rounded focus:outline-orange-500"
        />
        <input
          type="email"
          placeholder="Email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full p-2 mb-3 border rounded focus:outline-orange-500"
        />
        <input
          type="password"
          placeholder="Password"
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full p-2 mb-3 border rounded focus:outline-orange-500"
        />
        <button
          type="submit"
          className="w-full bg-navyblue text-softwhite p-2 rounded hover:bg-orange-500 transition"
        >
          Register
        </button>
      </form>
    </div>
  );
}
