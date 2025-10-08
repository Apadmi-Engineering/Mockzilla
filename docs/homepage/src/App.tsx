import { Features } from "./components/Features";
import { Footer } from "./components/Footer";
import { Hero } from "./components/Hero";
import { PlatformBanner } from "./components/PlatformBanner";
import { PlatformSupport } from "./components/PlatformSupport";
import { useTheme } from "./hooks/useTheme";

export default function App() {
    useTheme();

    return (
        <div className="min-h-screen bg-background">
            <main>
                <div className="flex flex-col md:h-screen bg-muted/30">
                    <Hero className="flex-1 flex items-center" />
                    <PlatformBanner />
                </div>
                <Features />
                <PlatformSupport />
            </main>
            <Footer />
        </div>
    );
}
