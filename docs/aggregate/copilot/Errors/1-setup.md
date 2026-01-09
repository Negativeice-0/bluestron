i rm readonly role, since java will handle it better. tailwind config colors are not working, use nextjs best practices like link/next instead of <a>. I had to move navbar into "./components", i expect a more throrough footer as well to be in the same folder. ConsentBanner has an error:"Error: Calling setState synchronously within an effect can trigger cascading renders

Effects are intended to synchronize state between React and external systems such as manually updating the DOM, state management libraries, or other platform APIs. In general, the body of an effect should do one or both of the following:
* Update external systems with the latest state from React.
* Subscribe for updates from some external system, calling setState in a callback function when external state changes.

Calling setState synchronously within an effect body causes cascading renders that can hurt performance, and is not recommended. (https://react.dev/learn/you-might-not-need-an-effect).

/home/lsetga/Advance/ambrosia/bluestron/bsui/src/app/components/ConsentBanner.tsx:9:5
   7 |   useEffect(() => {
   8 |     const v = localStorage.getItem('bs-consent')
>  9 |     setConsented(v === 'accepted')
     |     ^^^^^^^^^^^^ Avoid calling setState() directly within an effect
  10 |   }, [])
  11 |
  12 |   useEffect(() => {", can front end handle it or will backedn be needed -- sufice to say i have not yet added it and don't think we need yet as per the requirements maybe we will add it later if reqiested.