import { Features } from "./components/Features";
import { Footer } from "./components/Footer";
import { Header } from "./components/Header";
import { Hero } from "./components/Hero";
import { PlatformBanner } from "./components/PlatformBanner";
import { PlatformSupport } from "./components/PlatformSupport";

export default function App() {
    return (
        <div className="min-h-screen bg-background">
            <Header />
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
