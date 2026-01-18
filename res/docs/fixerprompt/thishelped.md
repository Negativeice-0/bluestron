# when ai gives you more error prone code after you gave it error prone code to fix

If you want me to stay locked into the “fix and modernize” mode — where I scan your code for deprecated APIs, anti‑patterns, or runtime errors and then hand back clean, idiomatic source code without drifting into assumptions — the most effective prompt is something like:

---

## 🔑 Example Prompt

> “Here’s my code snippet. Please check for deprecated APIs, improper practices, or runtime errors, and rewrite it for **Java 21 / Spring Boot 3 / Next.js 16** with best practices. Keep the same names and structure unless a change is absolutely required for correctness. Return the full corrected source code only.”

---

## Why this works

- **Explicit scope**: you tell me exactly what to do (scan for deprecated APIs, anti‑patterns, runtime errors).  
- **Target environment**: you specify the versions (Java 21, Spring Boot 3, Next.js 16) so I don’t drift into older or experimental APIs.  
- **Constraints**: you ask me to keep names and structure unless correctness demands a change, so I won’t rename classes or invent DTOs unless necessary.  
- **Output format**: you ask for “full corrected source code only,” which keeps me focused on delivering the fixed file rather than speculative commentary.  

---

## Variants you can use

- For backend files:  
  > “Fix this Spring Boot file for Java 21 with modern JPA/JJWT practices, no deprecated code, keep names as‑is.”  
- For frontend files:  
  > “Fix this Next.js 16 React component, remove anti‑patterns, keep the same props and names, return the corrected code.”  

---

👉 Using that style of prompt will keep me consistent, prevent drift, and ensure you get the same kind of precise fixes you saw with your first Java file and the Next.js UI component.  

Would you like me to draft a **ready‑to‑reuse template prompt** you can copy‑paste for both backend and frontend fixes, so you don’t have to re‑invent the wording each time?
