import { Badge } from "./ui/badge";
import {AndroidIcon, FlutterIcon, KotlinIcon, SwiftIcon} from "./ui/icons";

const platforms = [
  {
    name: "Android",
    icon: AndroidIcon,
    description: "Native Android",
    color: "text-orange-600"
  },
  {
    name: "Swift",
    icon: SwiftIcon,
    description: "Native iOS",
    color: "text-orange-600"
  },
  {
    name: "Kotlin Multiplatform",
    icon: KotlinIcon,
    description: "Cross-platform Kotlin",
    color: "text-purple-600"
  },
  {
    name: "Flutter",
    icon:
    FlutterIcon,
    description: "Cross-platform Flutter",
    color: "text-blue-600"
  }
];

export function PlatformSupport() {
  return (
    <section className="py-16 bg-muted/30">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center space-y-4 mb-12">
          <Badge variant="secondary" className="w-fit mx-auto">
            Multi-Platform Support
          </Badge>
          <h2 className="text-2xl lg:text-3xl font-bold">
            Works with various frameworks
          </h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Mockzilla works out of the box in Mobile KMP & Flutter apps.
          </p>
        </div>
        
        <div className="grid md:grid-cols-4 gap-8 max-w-4.5xl mx-auto">
          {platforms.map((platform, index) => (
            <div key={index} className="text-center space-y-4 p-6 bg-card rounded-lg border shadow-sm hover:shadow-md transition-shadow">
              <div className={`${platform.color} mx-auto w-16 h-16 flex items-center justify-center bg-secondary/50 rounded-lg`}>
                <platform.icon height="35px"/>
              </div>
              <div className="space-y-2">
                <h3 className="font-semibold text-lg">{platform.name}</h3>
                <p className="text-muted-foreground text-sm">{platform.description}</p>
              </div>
              <div className="flex justify-center">
                <Badge variant="outline" className="text-xs">
                  Fully Supported
                </Badge>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}