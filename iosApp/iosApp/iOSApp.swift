import SwiftUI
import shared
import ComposeApp
import FirebaseCore

@main
struct iOSApp: App {

    init() {
        AppModuleKt.doInitKoinIos()
        FirebaseApp.configure()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
