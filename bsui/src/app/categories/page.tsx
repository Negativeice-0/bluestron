'use client'
import { useEffect, useState } from 'react'

interface Category {
  id: number
  slug: string
  name: string
  description?: string
  status: string
}

export default function CategoriesPage() {
  const [categories, setCategories] = useState<Category[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/categories')
      .then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        return res.json()
      })
      .then(setCategories)
      .catch(err => setError(err.message))
  }, [])

  return (
    <div className="min-h-screen flex flex-col bg-softwhite text-navy">
      <main className="flex-1 px-4 py-10 max-w-4xl mx-auto">
        <h1 className="text-2xl md:text-3xl font-bold mb-6">Course Categories</h1>
        {error && (
          <div className="p-4 bg-red-100 text-red-700 rounded-md mb-4">
            Failed to load categories: {error}
          </div>
        )}
        <ul className="grid gap-6 md:grid-cols-2">
          {categories.map(cat => (
            <li key={cat.id} className="p-6 border border-navy/10 rounded-lg shadow-sm hover:shadow-md transition">
              <h2 className="text-xl font-semibold mb-2">{cat.name}</h2>
              <p className="text-navy/70 mb-2">{cat.description || 'No description provided.'}</p>
              <span className={`inline-block px-2 py-1 text-xs rounded-md ${
                cat.status === 'active' ? 'bg-green-100 text-green-700' : 'bg-gray-200 text-gray-600'
              }`}>
                {cat.status}
              </span>
            </li>
          ))}
        </ul>
      </main>
    </div>
  )
}
