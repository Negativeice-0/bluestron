I knew it was security, 404 means unauthorised.

A `401 Unauthorized` means Spring Security is intercepting the request before it ever reaches your controller. That’s different from the earlier `404` (no mapping). Here’s how to reason through it:

---

### Why you’re seeing 401
- **Spring Security default behavior:** If you include `spring-boot-starter-security` but don’t override it, Spring generates a default login page and requires authentication for all endpoints. That’s why you saw a random security key earlier.
- **Your SecurityConfig:** Even though you added `permitAll()` for `/api/courses`, if the matcher isn’t correct or the config isn’t being applied, Spring still requires auth → 401.
- **Curl vs browser:** Curl doesn’t send any credentials, so you get 401. Browser may show the login page.

---

### How to fix for Module 1 (no auth yet)
You want all public endpoints open until Module 2 introduces JWT. Adjust your `SecurityConfig`:

```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/actuator/**",
                    "/api/status",
                    "/api/categories/**",
                    "/api/courses/**",
                    "/api/trainers/**",
                    "/api/testimonials/**",
                    "/api/service-offerings/**",
                    "/api/service-enquiries/**",
                    "/api/registrations/**",
                    "/api/featured-content/**",
                    "/api/blog-posts/**",
                    "/api/homepage/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**"
                ).permitAll()
                .anyRequest().permitAll() // allow everything for now
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

Key points:
- Use `anyRequest().permitAll()` at the end for Module 1.
- Make sure your `SecurityConfig` is in a package scanned by Spring (`co.ke.bluestron.bsapi.config`).
- Restart the app after changes.

---

### Verification
1. Restart backend.
2. Run curl again:
   ```bash
   curl http://localhost:8080/api/courses
   ```
   → Should return JSON (empty array if no courses).
3. If still 401, check actuator mappings:
   ```bash
   curl http://localhost:8080/actuator/mappings
   ```
   → Confirm `/api/courses` is listed.
4. If mappings exist but 401 persists, check logs: Spring Security will log which filter rejected the request.

---

### Next step
Once curl works, we can move on to the **Course detail page + Registration flow**. But first, confirm that `/api/courses` is accessible without credentials after updating `SecurityConfig`.

Would you like me to scaffold the **Course detail page** (UI + backend endpoint for `/api/courses/{slug}`) right after you confirm curl returns JSON?