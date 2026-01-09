To arrive at that specific level of technical detail and architectural alignment, the prompt would have needed to combine **strict environmental constraints**, **specific tech stack versions**, and **naming convention preferences**.

If you wanted to generate my previous response in one go, your prompt likely would have looked like this:

---

### The "Perfect" Prompt

> "I am setting up a new backend environment for 2026. Give me the manual steps to configure **Java 25** on a Debian-based system without using high-level managers like SDKMAN—just downloading the binaries, cleaning out old versions, and updating `bashrc`.
> I want to launch a backend named **api** using the **Spring Initializr API** via `curl`. For the project structure, I am using the root **bluestron**. Tell me if I should use `ke.api` or `com.api` as the base, and give me your opinionated take on whether I should use abbreviations like `bsapi` and `bsdb` for sub-modules or stick to full names.
> Finally, remind me of my specific JPA requirement for text columns."

---

### Why this prompt works:

1. **Identifies the "How":** It specifies "manual" and "Debian-based," which triggers the `wget`, `tar`, and `bashrc` instructions instead of a generic installer.
2. **Sets the "When":** Mentioning "2026" and "Java 25" ensures the AI doesn't default to old LTS versions like 17 or 21.
3. **Forces a Choice:** By asking "pick the best and roll with it," you move the AI from being a neutral observer to an opinionated architect.
4. **Connects Saved Context:** It triggers the "Saved Information" regarding `columnDefinition = "TEXT"` by mentioning "JPA requirements."

**Would you like me to refine this into a reusable "System Bootstrap" script that you can run whenever you spin up a new Bluestron-related machine?**