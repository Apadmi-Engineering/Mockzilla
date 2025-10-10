import {AndroidIcon, FlutterIcon, KotlinIcon, SwiftIcon} from "./ui/icons";

export function PlatformBanner() {
  return (
    <div id="platform-banner" className="bg-[#232323] text-white py-3 border-b border-[#232323]">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8">
        <div className="relative flex items-center justify-center space-x-8">

          <div className="flex items-center space-x-6">
            <div className="flex items-center space-x-2">
              <div className="text-orange-400">
                <AndroidIcon height="20px" />
              </div>
              <span className="text-sm font-medium">Android</span>
            </div>

            <div className="flex items-center space-x-2">
              <div className="text-orange-400">
                <SwiftIcon height="20px" />
              </div>
              <span className="text-sm font-medium">iOS</span>
            </div>
            
            <div className="flex items-center space-x-2">
              <div className="text-purple-400">
                <KotlinIcon height="20px"/>
              </div>
              <span className="text-sm font-medium hidden sm:inline">Kotlin Multiplatform</span>
              <span className="text-sm font-medium sm:hidden">Kotlin</span>
            </div>
            
            <div className="flex items-center space-x-2">
              <div className="text-blue-400">
                <FlutterIcon height="20px"/>
              </div>
              <span className="text-sm font-medium">Flutter</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}