import {Button} from "./ui/button";
import {Badge} from "./ui/badge";
import {ArrowRight} from "lucide-react";
import {MockzillaLogoString} from "./ui/icons";
import SyntaxHighlighter from 'react-syntax-highlighter';
import { colorBrewer} from "react-syntax-highlighter/dist/cjs/styles/hljs";

export function Hero(props: any) {
    return (
        <section {...props} style={{
            backgroundImage: `${MockzillaLogoString}`,
            backgroundSize: "cover",
        }}>
            <div
                className="py-20 lg:py-32 container mx-auto px-4 sm:px-6 lg:px-8 bg-white/60 lg:bg-transparent md:rounded-2xl">
                <div className="flex flex-col-reverse lg:flex-row gap-12 items-center">

                    <div className="bg-card border rounded-lg p-6 shadow-sm overflow-x-auto hidden md:inline">
                        <div className="space-y-4">
                            <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                                <div className="w-3 h-3 bg-red-500 rounded-full"></div>
                                <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
                                <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                                <span>Example.kt</span>
                            </div>
                            <pre className="text-sm">
                <SyntaxHighlighter customStyle={{paddingLeft: "0", paddingRight: "0"}}
                                   language="kotlin"
                                   showLineNumbers
                                   style={colorBrewer}>{`// Configure Server
val endpoint = EndpointConfiguration
  .Builder("GET - Customer")
    .setDefaultHandler {
      MockzillaHttpResponse(
        statusCode = HttpStatusCode.OK,
        headers = mapOf("Custom-Headers" to "Are-Cool"),
        body = Customer(name = "Rosa Pankhurst").toJson()
      )
    }

val config = MockzillaConfig.Builder()
  .addEndpoint(endpoint)
  
// Start Server
startMockzilla(config)`}</SyntaxHighlighter>
              </pre>
                        </div>
                    </div>

                    <div className="space-y-8">
                        <div className="space-y-4">
                            <Badge variant="secondary" className="w-fit">
                                { `v${import.meta.env.VITE_VERSION_NAME}` } Released
                            </Badge>
                            <h1 className="text-4xl lg:text-6xl font-bold tracking-tight">
                                Build API mocks with ease
                            </h1>
                            <p className="text-xl text-muted-foreground max-w-md">
                                A compile safe solution for running and configuring a local Http server for your mobile
                                apps.
                            </p>
                        </div>

                        <div className="flex flex-col sm:flex-row gap-4">
                            <Button size="lg" className="w-fit"
                                    onClick={() => (window.top || window).location.href = "/quick-start"}>
                                Get Started
                                <ArrowRight className="ml-2 h-4 w-4"/>
                            </Button>
                            <Button
                                variant="outline"
                                size="lg"
                                onClick={() => window.location.href = "#platform-banner"}
                                className="w-fit">
                                Learn More
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}