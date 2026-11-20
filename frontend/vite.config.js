import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
export default defineConfig({
  plugins: [react()],
  server: { port: 5173, proxy: {
    '/api/citizens': 'http://localhost:8081',
    '/api/grievances': 'http://localhost:8082',
    '/api/dashboard': 'http://localhost:8082'
  }}
})
