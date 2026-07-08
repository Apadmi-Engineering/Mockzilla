import { renderToStaticMarkup } from 'react-dom/server'
import App from './App'

export function render(): string {
  return renderToStaticMarkup(<App />)
}
