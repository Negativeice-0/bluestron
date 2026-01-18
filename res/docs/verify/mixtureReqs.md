# missing or wrong

Great question — let’s get surgical so we can fix things quickly. Based on your curl logs and the stack traces, the pieces we need to look at are:

---

## 🔑 Backend code we need to review

1. **AuthController**  
   - You’re hitting `/api/auth/login` but getting `404 Not Found`. That means there’s no controller mapped to that path.  
   - We need to see your `AuthController` (or whatever class handles login) to confirm the `@PostMapping` path and whether it returns a JWT.

2. **DashboardController (Admin)**  
   - `/api/admin/dashboard/stats` is returning `403 Forbidden`. That means the endpoint exists but your JWT filter is rejecting the token.  
   - We need to see the controller method for `/api/admin/dashboard/stats` and the security configuration (`SecurityConfig`) to check role requirements.

3. **TestimonialsController / Service**  
   - `/api/testimonials` is returning `500 Internal Server Error`. That means the controller exists but the service/repository is throwing.  
   - We need to see the controller and service code for testimonials.

4. **MediaController**  
   - `/api/media/1/stream` is returning `404 Not Found`. That means there’s no controller mapped to that path.  
   - We need to see if you have a `MediaController` or any code handling file streaming.

5. **SecurityConfig**  
   - Since JWT is central, we need to see your `SecurityConfig` class to confirm how filters are registered and what endpoints are protected.

---

## 🧩 Why these matter

- **AuthController** → fixes login (so you can get a token).  
- **DashboardController + SecurityConfig** → fixes admin stats access (so token works).  
- **TestimonialsController/Service** → fixes the 500 error.  
- **MediaController** → fixes streaming endpoint.  

Everything else (categories, courses, blog) is already returning data, so those are fine.

---

👉 If you share those five code files (`AuthController`, `DashboardController`, `TestimonialsController/Service`, `MediaController`, and `SecurityConfig`), I can pinpoint the missing mappings, role checks, and service bugs. That will let us fix the backend fast and then wire the UI cleanly.  

Would you like to start by pasting your **AuthController** first, since login is the gateway to everything else?
