import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

export default defineConfig({
  plugins: [react()],

  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },

  optimizeDeps: {
    exclude: ['lucide-react'],
  },

  server: {
    proxy: {
      '/api': {
        target: 'https://misty-painedly-cleotilde.ngrok-free.dev',
        changeOrigin: true,
        secure: true,
        headers: {
          'ngrok-skip-browser-warning': 'true',
        },
        configure: (proxy) => {
          proxy.on('proxyReq', (proxyReq) => {
            // Strip the browser's Origin + Referer so the backend's
            // CORS filter doesn't reject localhost:5174
            proxyReq.removeHeader('origin');
            proxyReq.removeHeader('referer');
          });
        },
      },
    },
  },
});