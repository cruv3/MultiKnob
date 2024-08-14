package controler.multiknobcontroller.controls

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import controler.multiknobcontroller.controls.actions.goBackPressed
import controler.multiknobcontroller.controls.actions.navigateToHomeScreen
import controler.multiknobcontroller.controls.actions.navigations.navigateDown
import controler.multiknobcontroller.controls.actions.navigations.navigateLeft
import controler.multiknobcontroller.controls.actions.navigations.navigateRight
import controler.multiknobcontroller.controls.actions.navigations.navigateUp
import controler.multiknobcontroller.controls.actions.openAllAppsScreen
import controler.multiknobcontroller.controls.actions.openAppWithPackageName
import controler.multiknobcontroller.controls.actions.openRecentApps
import controler.multiknobcontroller.service.BluetoothService
import controler.multiknobcontroller.utils.delayFunction
import controler.multiknobcontroller.utils.delayFunctionWithCallback

class Controller(private val context: Context, private val service: AccessibilityService, private val nodeManager: NodeManager, private val bluetoothService: BluetoothService) {
    companion object{
        const val TAG = "Controller_MultiKnob"
        private const val Navigation_DELAY = 200L
    }
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val displayMetrics = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }

    private val gestureControls = GestureControls(service)

    val googleMapsControls = GoogleMapsControls(nodeManager, gestureControls, displayMetrics, bluetoothService)

    fun doNavigateUp() {
        delayFunction<Unit>(Navigation_DELAY, nodeManager, "Navigate Up"){
            navigateUp(nodeManager)
        }.invoke(Unit)
    }

    fun doNavigateDown() {
        delayFunction<Unit>(Navigation_DELAY, nodeManager, "Navigate Down") {
            navigateDown(nodeManager)
        }.invoke(Unit)
    }

    fun doNavigateLeft() {
        delayFunction<Unit>(Navigation_DELAY, nodeManager, "Navigate Left") {
            navigateLeft(nodeManager, gestureControls)
        }.invoke(Unit)
    }

    fun doNavigateRight() {
        delayFunction<Unit>(Navigation_DELAY, nodeManager, "Navigate Right") {
            navigateRight(nodeManager, gestureControls)
        }.invoke(Unit)
    }

    fun clickCurrentNode() {
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Clicked",{
                _: Unit -> nodeManager.clickCurrentNode()
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun goToHomeScreen() {
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Navigate to Home Screen",{
                _: Unit -> navigateToHomeScreen(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun recentApps(){
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Open Recent Apps",{
            _: Unit -> openRecentApps(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)

    }

    fun goBack() {
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Go Back",{
                _: Unit -> goBackPressed(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openGoogleMaps(){
        val packageName = "com.google.android.apps.maps"
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Start Google Maps",{
                _: Unit -> openAppWithPackageName(context, packageName)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openSpotify(){
        val packageName = "com.spotify.music"
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Start Spotify",{
                _: Unit -> openAppWithPackageName(context, packageName)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openAllApps(){
        delayFunctionWithCallback(Navigation_DELAY, Navigation_DELAY, nodeManager, "Open All Apps",{
                _: Unit -> openAllAppsScreen(service, nodeManager, gestureControls)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }
}
