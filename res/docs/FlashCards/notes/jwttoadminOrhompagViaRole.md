Right now your `JwtService` only sets the subject (`username`) in the token. That’s why your frontend never sees a `role` or `authorities` claim when it decodes the JWT. To fix this, you need to add the user’s authorities into the claims before signing.

---

## 🔧 How to add roles/authorities into your JWT

Update your `generateToken` method like this:

```java
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    // Add authorities (roles) into the claims
    claims.put("authorities", userDetails.getAuthorities()
            .stream()
            .map(Object::toString)
            .toList());

    return generateToken(claims, userDetails);
}
```

Your existing `generateToken(Map<String,Object>, UserDetails)` will then include those claims when building the JWT.

---

## 🔄 Full updated class (only changed part shown)

```java
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", userDetails.getAuthorities()
            .stream()
            .map(Object::toString)
            .toList());

    return generateToken(claims, userDetails);
}

public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSignInKey())
            .compact();
}
```

---

## ✅ Result
- When an admin logs in, the JWT payload will look like:

```json
{
  "sub": "newadmin@bluestron.co.ke",
  "authorities": ["ROLE_ADMIN"],
  "iat": 1768144772,
  "exp": 1768231172
}
```

- Your frontend can now decode the token and check:

```ts
const payload = JSON.parse(atob(token.split('.')[1]));
const role = payload.authorities?.[0] || 'USER';

if (role.includes('ADMIN')) {
  router.push('/admin');
} else {
  router.push('/');
}
```

---

👉 This way the JWT itself carries the role, so both frontend and backend can branch correctly.  

Would you like me to also show you how to **enforce role checks in Spring Security** (e.g. `/admin/**` requires `ROLE_ADMIN`) so you don’t rely only on frontend redirects?