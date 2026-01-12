import { cookies } from 'next/headers';
import { redirect } from 'next/navigation';

// Define interfaces for API responses
interface Course { id: number; title: string; }
interface Category { id: number; name: string; }
interface Registration { id: number; status: string; }

async function getStats(token: string) {
  const headers = { Authorization: `Bearer ${token}` };

  const [coursesRes, registrationsRes, categoriesRes] = await Promise.all([
    fetch('http://localhost:8080/api/courses', { headers, cache: 'no-store' }),
    fetch('http://localhost:8080/api/registrations', { headers, cache: 'no-store' }),
    fetch('http://localhost:8080/api/categories', { headers, cache: 'no-store' }),
  ]);

  const courses: Course[] = await coursesRes.json();
  const registrations: Registration[] = await registrationsRes.json();
  const categories: Category[] = await categoriesRes.json();

  return {
    totalCourses: courses.length,
    totalRegistrations: registrations.length,
    totalCategories: categories.length,
    pendingRegistrations: registrations.filter(r => r.status === 'PENDING').length,
  };
}

export default async function AdminDashboard() {
  const token = (await cookies()).get('token')?.value;
  if (!token) redirect('/login');

  // Decode JWT payload
  const payload = JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString());
  const role = payload?.authorities?.[0] || 'USER';
  if (!role.includes('ADMIN')) redirect('/');

  const stats = await getStats(token);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Admin Dashboard</h1>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard label="Total Courses" value={stats.totalCourses} color="blue" />
        <StatCard label="Registrations" value={stats.totalRegistrations} color="green" />
        <StatCard label="Pending" value={stats.pendingRegistrations} color="yellow" />
        <StatCard label="Categories" value={stats.totalCategories} color="purple" />
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <QuickLink href="/admin/courses" label="Manage Courses" desc="Add, edit, or delete training courses" color="blue" />
        <QuickLink href="/admin/categories" label="Manage Categories" desc="Organize courses into categories" color="green" />
        <QuickLink href="/admin/registrations" label="Registrations" desc="View and manage course registrations" color="yellow" />
      </div>
    </div>
  );
}

function StatCard({ label, value, color }: { label: string; value: number; color: string }) {
  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center">
        <div className={`bg-${color}-100 p-3 rounded-lg`}>
          <div className={`w-6 h-6 text-${color}-600`}>★</div>
        </div>
        <div className="ml-4">
          <p className="text-sm font-medium text-gray-600">{label}</p>
          <p className="text-2xl font-semibold text-gray-900">{value}</p>
        </div>
      </div>
    </div>
  );
}

function QuickLink({ href, label, desc, color }: { href: string; label: string; desc: string; color: string }) {
  return (
    <a href={href} className="block p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow">
      <div className="flex items-center">
        <div className={`bg-${color}-100 p-3 rounded-lg`}>
          <div className={`w-6 h-6 text-${color}-600`}>→</div>
        </div>
        <div className="ml-4">
          <h3 className="text-lg font-semibold text-gray-900">{label}</h3>
          <p className="text-gray-600 mt-1">{desc}</p>
        </div>
      </div>
    </a>
  );
}
