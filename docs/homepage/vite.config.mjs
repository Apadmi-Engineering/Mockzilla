
  import { defineConfig } from 'vite';
  import react from '@vitejs/plugin-react-swc';
  import * as path from "node:path";
  import tailwindcss from '@tailwindcss/vite'


  export default defineConfig({
    plugins: [react({
      babel: {
        plugins: [['babel-plugin-react-compiler']],
      },
    }), tailwindcss()],
    resolve: {
      extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    build: {
      target: 'esnext',
      outDir: 'build',
      assetsDir: "homepage-assets"
    },
    server: {
      port: 3010,
      open: true,
    },
  });