import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import {
  Code,
  WifiOff,
  CircleCheck,
  ServerOff, Shuffle, ShieldCheck
} from "lucide-react";

const features = [
  {
    icon: Shuffle,
    title: "Client Agnostic",
    description: "Independent of your app's code, so use any HTTP client!"
  },
  {
    icon: ShieldCheck,
    title: "Compile Safe",
    description: "Use your networking models for your mock, keeping everything compile safe!"
  },
  {
    icon: Code,
    title: "Developer Friendly",
    description: "Desktop app and built in UI allowing configuring your endpoints on the fly"
  },
  {
    icon: ServerOff,
    title: "No third party hosting",
    description: "Since everything's on device there's risk of your mocks going down"
  },
  {
    icon: WifiOff,
    title: "Works offline",
    description: "We're running our own server, so no internet required!"
  },
  {
    icon: CircleCheck,
    title: "Self Contained",
    description: "Mocking without leaving your app codebase"
  }
];

export function Features() {
  return (
    <section id="features" className="py-20 bg-muted/30">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center space-y-4 mb-16">
          <h2 className="text-3xl lg:text-4xl font-bold">
            Why choose Mockzilla?
          </h2>
          <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
            Built for modern applications with developer experience and performance in mind.
          </p>
        </div>
        
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {features.map((feature, index) => (
            <Card key={index} className="border-0 shadow-sm">
              <CardHeader>
                <div className="w-12 h-12 rounded-lg bg-primary/10 flex items-center justify-center mb-4">
                  <feature.icon className="h-6 w-6 text-primary" />
                </div>
                <CardTitle>{feature.title}</CardTitle>
              </CardHeader>
              <CardContent>
                <CardDescription className="text-base">
                  {feature.description}
                </CardDescription>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}