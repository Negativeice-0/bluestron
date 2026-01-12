import type { Config } from 'tailwindcss'

const config: Config = {
  content: ['./src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      animation: {
        'spin-slow': 'spin 6s linear infinite', // Slower, more elegant spin
         'button-glow': 'glow 3s infinite',
      },
      colors: {
        navyblue: '#001f3f', 
        orange: '#ff851b', 
        softwhite: '#f8f9fa',
       
      },
      fontFamily: {
        sans: ['ui-sans-serif', 'system-ui', '-apple-system', 'Segoe UI', 'Roboto', 'Ubuntu', 'Cantarell', 'Noto Sans', 'sans-serif'],
      },
      // Optional: Add a custom animation for a constant subtle glow pulse
      keyframes: {
        glow: {
          '0%, 100%': { boxShadow: '0 0 5px rgba(249, 115, 22, 0.4)' },
          '50%': { boxShadow: '0 0 20px rgba(249, 115, 22, 0.7)' },
        }
      },
    },
  },
  plugins: [],
}
export default config
