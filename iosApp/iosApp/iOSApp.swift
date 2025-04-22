import SwiftUI
import ComposeApp
//import Firebase

@main
struct iOSApp: App {

    init() {
        AppModuleKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
