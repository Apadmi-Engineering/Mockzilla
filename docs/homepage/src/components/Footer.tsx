import { Button } from "./ui/button";
import { GithubIcon } from "./ui/icons";
import logo from "../assets/logo.svg";

export function Footer() {
    return (
        <footer className="border-t bg-muted/30">
            <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-12">
                <div className="grid md:grid-cols-4 gap-8">
                    <div className="space-y-4">
                        <div className="flex items-center space-x-2">
                            <img
                                src={logo}
                                alt="Mockzilla Logo"
                                className="h-5"
                            />
                        </div>
                    </div>

                    <div className="space-y-4">
                        <h3 className="font-semibold">Documentation</h3>
                        <ul className="space-y-2 text-sm text-muted-foreground">
                            <li>
                                <a
                                    target="_top"
                                    href="/quick-start"
                                    className="hover:text-foreground transition-colors">
                                    Getting Started
                                </a>
                            </li>
                            <li>
                                <a
                                    target="_top"
                                    href="/dokka"
                                    className="hover:text-foreground transition-colors">
                                    API Reference
                                </a>
                            </li>
                            <li>
                                <a
                                    target="_top"
                                    href="https://github.com/Apadmi-Engineering/Mockzilla/tree/develop/samples"
                                    className="hover:text-foreground transition-colors">
                                    Examples
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div className="space-y-4">
                        <h3 className="font-semibold">Community</h3>
                        <ul className="space-y-2 text-sm text-muted-foreground">
                            <li>
                                <a
                                    target="_top"
                                    href="https://github.com/Apadmi-Engineering/Mockzilla/"
                                    className="hover:text-foreground transition-colors">
                                    GitHub
                                </a>
                            </li>
                            <li>
                                <a
                                    target="_top"
                                    href="/contributing"
                                    className="hover:text-foreground transition-colors">
                                    Contributing
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div className="space-y-4">
                        <h3 className="font-semibold">Resources</h3>
                        <ul className="space-y-2 text-sm text-muted-foreground">
                            <li>
                                <a
                                    target="_top"
                                    href="https://github.com/Apadmi-Engineering/Mockzilla/issues"
                                    className="hover:text-foreground transition-colors">
                                    Support
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <div className="border-t mt-12 pt-8 flex flex-col sm:flex-row justify-between items-center">
                    <p className="text-sm text-muted-foreground">
                        © 2025 Apadmi Ltd.
                    </p>
                    <div className="flex space-x-6 text-sm text-muted-foreground mt-4 sm:mt-0">
                        <div className="flex space-x-2">
                            <Button
                                variant="ghost"
                                size="icon"
                                onClick={() =>
                                    (window.location.href =
                                        "https://github.com/Apadmi-Engineering/Mockzilla/")
                                }
                                className="hover:text-accent">
                                <GithubIcon className="h-4 w-4" />
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    );
}
