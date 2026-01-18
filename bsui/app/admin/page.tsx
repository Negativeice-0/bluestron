'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import AdminBlog from '../components/admin/Blog';
import AdminCourses from '../components/admin/Courses';
import AdminDashboard from '../components/admin/Dashboard';
import AdminRegistrations from '../components/admin/Registrations';

export default function AdminPage() {
  // Initialize token directly from localStorage
  const [token, setToken] = useState<string | null>(() => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('bluestron_token');
    }
    return null;
  });

  const [activeTab, setActiveTab] = useState('dashboard');
  const router = useRouter();

  useEffect(() => {
    // Redirect if no token
    if (!token) {
      router.push('/login?redirect=/admin');
    }
  }, [token, router]);

  if (!token) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl text-gray-600">Loading admin panel...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Admin Header */}
      <header className="bg-linear-to-r from-purple-500 to-blue-500 text-white shadow-lg">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <h1 className="text-2xl font-bold">Bluestron Admin</h1>
            <div className="flex items-center gap-4">
              <button
                onClick={() => {
                  localStorage.removeItem('bluestron_token');
                  setToken(null); // clear state
                  router.push('/');
                }}
                className="px-4 py-2 bg-white text-purple-500 rounded-lg hover:bg-gray-100"
              >
                Logout
              </button>
            </div>
          </div>

          {/* Admin Navigation */}
          <nav className="mt-4">
            <div className="flex flex-wrap gap-2">
              {['dashboard', 'courses', 'registrations', 'blog', 'settings'].map((tab) => (
                <button
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`px-4 py-2 rounded-lg capitalize transition ${
                    activeTab === tab
                      ? 'bg-white text-purple-500'
                      : 'text-white hover:bg-white/20'
                  }`}
                >
                  {tab}
                </button>
              ))}
            </div>
          </nav>
        </div>
      </header>

      {/* Admin Content */}
      <main className="container mx-auto px-4 py-8">
        {activeTab === 'dashboard' && <AdminDashboard token={token} />}
        {activeTab === 'courses' && <AdminCourses />}
        {activeTab === 'registrations' && <AdminRegistrations />}
        {activeTab === 'blog' && <AdminBlog />}
        {activeTab === 'settings' && (
          <div className="bg-white rounded-xl shadow p-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">Admin Settings</h2>
            <p className="text-gray-600">Platform configuration options coming soon.</p>
          </div>
        )}
      </main>
    </div>
  );
}
