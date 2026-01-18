# issues

You’re at the point where you need **structured backend tests** to know what’s failing, and then a UI layer to exercise those endpoints. Let’s break this into two parts:

---

## 🧪 Backend Testing Suite

Spring Boot projects typically use **JUnit 5 + Spring Boot Test + MockMvc** for integration tests. You can also add **Postman/Newman** or **REST Assured** for end‑to‑end API testing.

## 1. Dependencies (in `pom.xml`)

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>io.rest-assured</groupId>
  <artifactId>rest-assured</artifactId>
  <scope>test</scope>
</dependency>
```

### 2. Example Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginShouldReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@bluestron.com\",\"password\":\"admin123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginWithBadCredentialsShouldFail() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@bluestron.com\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized());
    }
}
```

```java
@SpringBootTest
@AutoConfigureMockMvc
class PublicEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void categoriesShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testimonialsShouldNotThrow500() throws Exception {
        mockMvc.perform(get("/api/testimonials"))
            .andExpect(status().isOk()); // fix controller/service to avoid 500
    }
}
```

## 3. What’s failing in your curl logs

- `/api/auth/login` → 404 Not Found. That means **no controller mapped to that path**. You need an `AuthController` with `@PostMapping("/api/auth/login")`.
- `/api/testimonials` → 500 Internal Server Error. That means the controller exists but the service/repository is throwing (likely null pointer or missing table).
- `/api/admin/dashboard/stats` → 403 Forbidden. That means the endpoint exists but your JWT filter rejected the token (role mismatch or invalid signature).
- `/api/media/1/stream` → 404 Not Found. No controller mapped for media streaming.

So your tests should confirm these endpoints exist and return the right status codes.

---

## 🎨 UI Layer

Once the backend passes tests, you can scaffold a **React/Next.js frontend** (you already started with Next.js). Suggested structure:

- **Login Page** → POST `/api/auth/login`, store JWT in localStorage.
- **Admin Dashboard** → GET `/api/admin/dashboard/stats` with `Authorization: Bearer`.
- **Courses Page** → GET `/api/courses`.
- **Categories Page** → GET `/api/categories`.
- **Blog Page** → GET `/api/blog`.
- **Testimonials Page** → GET `/api/testimonials`.

Example React hook for fetching:

```tsx
function useApi(endpoint: string, token?: string) {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetch(endpoint, {
      headers: token ? { Authorization: `Bearer ${token}` } : {}
    })
      .then(res => res.json())
      .then(setData)
      .catch(console.error);
  }, [endpoint, token]);

  return data;
}
```

---

## 🔑 Next Steps

1. **Add missing controllers** (`AuthController`, `MediaController`, `DashboardController`).
2. **Write integration tests** for each endpoint so you catch 404/500/403 early.
3. **Fix failing services** (testimonials service, JWT role validation).
4. **Build UI pages** that call these endpoints and display results.

---

👉 Pros deal with this by **always writing tests first**: they run `mvn test` and see red/green before deploying. That way curl surprises don’t happen.  

Would you like me to generate a **full Postman collection** (JSON) with all your endpoints so you can run them automatically and see which ones fail?
