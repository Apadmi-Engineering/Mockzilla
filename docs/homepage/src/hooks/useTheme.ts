import { useEffect, useState } from 'react';

export function useTheme() {
  // Initialize based on prefers-color-scheme and apply immediately
  const [theme, setTheme] = useState<'light' | 'dark'>(() => {
    if (typeof window !== 'undefined') {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      document.documentElement.classList.toggle('dark', prefersDark);
      return prefersDark ? 'dark' : 'light';
    }
    return 'dark';
  });

  useEffect(() => {
    // Check if running in iframe
    const isInIframe = window.self !== window.top;

    if (isInIframe) {
      // Listen for theme changes from parent MkDocs frame
      const handleMessage = (event: MessageEvent) => {
        if (event.data?.type === 'theme-change') {
          const newTheme = event.data.theme === 'slate' ? 'dark' : 'light';
          setTheme(newTheme);
          document.documentElement.classList.toggle('dark', newTheme === 'dark');
        }
      };

      window.addEventListener('message', handleMessage);

      // Request current theme from parent after a short delay to ensure parent is ready
      setTimeout(() => {
        window.parent.postMessage({ type: 'get-theme' }, '*');
      }, 100);

      return () => window.removeEventListener('message', handleMessage);
    } else {
      // Fallback to prefers-color-scheme if not in iframe
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
      const handleChange = (e: MediaQueryListEvent) => {
        const newTheme = e.matches ? 'dark' : 'light';
        setTheme(newTheme);
        document.documentElement.classList.toggle('dark', newTheme === 'dark');
      };

      setTheme(mediaQuery.matches ? 'dark' : 'light');
      document.documentElement.classList.toggle('dark', mediaQuery.matches);

      mediaQuery.addEventListener('change', handleChange);
      return () => mediaQuery.removeEventListener('change', handleChange);
    }
  }, []);

  return theme;
}
