'use client';

import { motion } from 'framer-motion';
import { useEffect, useState } from 'react';

interface DashboardStats {
  totalCourses: number;
  totalRegistrations: number;
  totalPosts: number;
}

export default function AdminDashboard({ token }: { token: string }) {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardStats = async () => {
      try {
        const res = await fetch('/api/admin/dashboard/stats', {
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await res.json();
        setStats(data);
      } catch (error) {
        console.error('Failed to fetch dashboard stats:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchDashboardStats();
  }, [token]);

  if (loading) {
    return <div className="text-center py-12">Loading dashboard...</div>;
  }

  const statCards = [
    {
      title: 'Total Courses',
      value: stats?.totalCourses || 0,
      color: 'from-purple-500 to-purple-600',
      icon: '📚',
    },
    {
      title: 'Total Registrations',
      value: stats?.totalRegistrations || 0,
      color: 'from-blue-500 to-blue-600',
      icon: '👥',
    },
    {
      title: 'Total Blog Posts',
      value: stats?.totalPosts || 0,
      color: 'from-green-500 to-green-600',
      icon: '📝',
    },
  ];

  return (
    <div>
      <h2 className="text-3xl font-bold text-gray-800 mb-8">Dashboard Overview</h2>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
        {statCards.map((card, index) => (
          <motion.div
            key={card.title}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1 }}
            className="bg-white rounded-xl shadow-lg p-6 border border-gray-100"
          >
            <div className="flex items-center justify-between mb-4">
              <span className="text-3xl">{card.icon}</span>
              <div
                className={`h-12 w-12 rounded-lg bg-linear-to-r ${card.color} flex items-center justify-center text-white font-bold`}
              >
                {card.value}
              </div>
            </div>
            <h3 className="text-lg font-semibold text-gray-700">{card.title}</h3>
          </motion.div>
        ))}
      </div>
    </div>
  );
}
