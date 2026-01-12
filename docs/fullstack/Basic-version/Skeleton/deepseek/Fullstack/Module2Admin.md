# Admin

I think i already wired up the backedn, but the links in the content below seem to do nothing, can you give me the curl commands to confirm id the backedn is working and then figure out why the frontend is not managing content (or is it becasue i have not yet set up sourse detail and servie pages?).


Exactly Billy — let’s separate backend verification from frontend routing so you know where the problem lies.

---

## 🔧 Curl commands to confirm backend endpoints

Since your dashboard fetches `/api/courses`, `/api/registrations`, and `/api/categories`, let’s hit those directly with curl using the JWT you got from login.

### 1. Test courses
```bash
curl -v http://localhost:8080/api/courses \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

### 2. Test registrations
```bash
curl -v http://localhost:8080/api/registrations \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

### 3. Test categories
```bash
curl -v http://localhost:8080/api/categories \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

Replace `<YOUR_JWT_TOKEN>` with the token you got from `/auth/login`.

👉 If each returns JSON (even empty arrays), the backend is wired correctly.

---

## 🔍 Why frontend links “do nothing”

- In your dashboard you have links like:

```tsx
<Link href="/admin/courses">Manage Courses</Link>
<Link href="/admin/categories">Manage Categories</Link>
<Link href="/admin/registrations">Registrations</Link>
```

- But unless you’ve created `src/app/admin/courses/page.tsx`, `src/app/admin/categories/page.tsx`, and `src/app/admin/registrations/page.tsx`, clicking those links will just render a blank page or 404.  
- Next.js App Router requires a **page.tsx file per route**. Right now you only have `/admin/page.tsx`.

---

## ✅ What to do next

1. **Confirm backend** with the curl commands above.  
   - If they return data, backend is fine.  
   - If not, fix controllers/services.

2. **Create frontend pages** for each link. For example:

`src/app/admin/courses/page.tsx`
```tsx
export default function CoursesPage() {
  return <h1>Courses management coming soon</h1>;
}
```

`src/app/admin/categories/page.tsx`
```tsx
export default function CategoriesPage() {
  return <h1>Categories management coming soon</h1>;
}
```

`src/app/admin/registrations/page.tsx`
```tsx
export default function RegistrationsPage() {
  return <h1>Registrations management coming soon</h1>;
}
```

3. Later, wire those pages to fetch data from the backend endpoints you confirmed with curl.

---

## 🚀 Bottom line
- Use curl to prove backend endpoints are alive.  
- Frontend links “do nothing” because you haven’t created the corresponding Next.js pages yet.  

Would you like me to scaffold those three admin sub‑pages with **real fetch calls to backend** (instead of placeholders) so you immediately see content when clicking the links?