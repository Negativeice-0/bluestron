'use client';

import { createContext, useEffect, useState } from 'react';

interface AuthContextType {
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  // Initialize state directly from localStorage
  const [token, setToken] = useState<string | null>(
    () => (typeof window !== 'undefined' ? localStorage.getItem('bluestron_token') : null)
  );

  useEffect(() => {
    // If you want to re‑sync on mount, do it asynchronously
    Promise.resolve().then(() => {
      const stored = localStorage.getItem('bluestron_token');
      if (stored) setToken(stored);
    });
  }, []);

  const login = async (email: string, password: string) => {
    const res = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    const data = await res.json();
    setToken(data.token);
    localStorage.setItem('bluestron_token', data.token);
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem('bluestron_token');
  };

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
