'use client'
import { useEffect, useState } from 'react'

export default function CategoriesPage() {
  const [categories, setCategories] = useState<unknown[]>([])

  useEffect(() => {
    fetch('http://localhost:8080/api/categories')
      .then(res => res.json())
      .then(setCategories)
      .catch(err => console.error(err))
  }, [])

  return (
    <section className="p-4">
      <h1 className="text-xl font-bold">Categories</h1>
      <ul>
        {categories.map((c, i) => (
          <li key={i}>{JSON.stringify(c)}</li>
        ))}
      </ul>
    </section>
  )
}
