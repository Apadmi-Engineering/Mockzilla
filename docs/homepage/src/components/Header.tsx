import { GithubIcon } from "./ui/icons";

const logo = "/img/icon.svg";

export function Header() {
    return (
        <header className="sticky top-0 z-50 border-b border-border bg-background/95 backdrop-blur-sm">
            <div className="container mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex h-16 items-center">
                    <a href="/" className="flex items-center space-x-2">
                        <img src={logo} alt="Mockzilla" className="h-7" />
                        <span className="font-semibold text-lg">Mockzilla</span>
                    </a>
                    <nav className="hidden md:flex items-center space-x-6 ml-auto">
                        <button
                            id="theme-toggle"
                            aria-label="Toggle theme"
                            className="rounded-md p-2 text-muted-foreground hover:text-foreground hover:bg-muted transition-colors">
                            <svg
                                id="icon-sun"
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-5 w-5"
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                strokeWidth="2">
                                <circle cx="12" cy="12" r="5" />
                                <line x1="12" y1="1" x2="12" y2="3" />
                                <line x1="12" y1="21" x2="12" y2="23" />
                                <line x1="4.22" y1="4.22" x2="5.64" y2="5.64" />
                                <line x1="18.36" y1="18.36" x2="19.78" y2="19.78" />
                                <line x1="1" y1="12" x2="3" y2="12" />
                                <line x1="21" y1="12" x2="23" y2="12" />
                                <line x1="4.22" y1="19.78" x2="5.64" y2="18.36" />
                                <line x1="18.36" y1="5.64" x2="19.78" y2="4.22" />
                            </svg>
                            <svg
                                id="icon-moon"
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-5 w-5"
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                strokeWidth="2">
                                <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
                            </svg>
                        </button>
                        <a
                            href="/quick-start"
                            className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                            Docs
                        </a>
                        <a
                            href="/dokka"
                            className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                            API Reference
                        </a>
                        <a
                            href="https://github.com/Apadmi-Engineering/Mockzilla/"
                            className="text-sm text-muted-foreground hover:text-foreground transition-colors flex items-center gap-1.5">
                            <GithubIcon className="h-4 w-4" />
                            GitHub
                        </a>
                    </nav>
                </div>
            </div>
        </header>
    );
}
