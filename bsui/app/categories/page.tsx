// app/categories/page.tsx
import { getCategories } from '@/lib/api';
import { Category } from '@/types/category';

export default async function CategoriesPage() {
  const categories: Category[] = await getCategories();

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold text-purple-500 mb-8">
        Course Categories
      </h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {categories.map((cat) => (
          <div
            key={cat.id}
            className="bg-white rounded-xl shadow-lg p-6 border border-blue-100 hover:shadow-xl transition-shadow duration-300"
          >
            <h2 className="text-2xl font-semibold text-blue-500 mb-2">
              {cat.name}
            </h2>
            <p className="text-gray-600">{cat.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
