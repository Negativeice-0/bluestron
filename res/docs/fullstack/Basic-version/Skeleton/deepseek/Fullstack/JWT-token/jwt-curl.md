Based on the conversation in that page, you get the JWT token from the **login endpoint response**. Here's how to obtain it:

### Get your JWT token:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"newadmin@bluestron.co.ke","password":"Admin123!"}'
```

**The response will be a plain JWT string** like:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNjE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

Real string
```
eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sInN1YiI6Im5ld2FkbWluQGJsdWVzdHJvbi5jby5rZSIsImlhdCI6MTc2ODE5NzU1NCwiZXhwIjoxNzY4MjgzOTU0fQ.vAV286Qhkgh8rRNiuDh9Mnv9uXj3xv_zwwUDPQ516t8
```

# Jwt is not permanent, so these will be fake when testing another time
That jwtExpiration property (from application.properties) defines how long the token is valid. Example: jwt.expiration=86400000 → 24 hours.


### Then use it in your test commands:
# Courses
```bash
# Copy the token from login response, then:
curl -v http://localhost:8080/api/courses \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sInN1YiI6Im5ld2FkbWluQGJsdWVzdHJvbi5jby5rZSIsImlhdCI6MTc2ODE5NzU1NCwiZXhwIjoxNzY4MjgzOTU0fQ.vAV286Qhkgh8rRNiuDh9Mnv9uXj3xv_zwwUDPQ516t8"
```

# REgistrations
```bash
curl -v http://localhost:8080/api/registrations \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sInN1YiI6Im5ld2FkbWluQGJsdWVzdHJvbi5jby5rZSIsImlhdCI6MTc2ODE5NzU1NCwiZXhwIjoxNzY4MjgzOTU0fQ.vAV286Qhkgh8rRNiuDh9Mnv9uXj3xv_zwwUDPQ516t8"
```
# categories
```bash
curl -v http://localhost:8080/api/categories \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sInN1YiI6Im5ld2FkbWluQGJsdWVzdHJvbi5jby5rZSIsImlhdCI6MTc2ODE5NzU1NCwiZXhwIjoxNzY4MjgzOTU0fQ.vAV286Qhkgh8rRNiuDh9Mnv9uXj3xv_zwwUDPQ516t8"
```

**Pro tip:** Save the token to a variable for easier reuse:
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"newadmin@bluestron.co.ke","password":"Admin123!"}')

# Now use $TOKEN in your requests:
curl -v http://localhost:8080/api/courses \
  -H "Authorization: Bearer $TOKEN"
```