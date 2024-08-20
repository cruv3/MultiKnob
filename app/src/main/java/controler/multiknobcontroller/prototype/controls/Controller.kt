package controler.multiknobcontroller.prototype.controls

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import controler.multiknobcontroller.prototype.controls.actions.goBackPressed
import controler.multiknobcontroller.prototype.controls.actions.navigateToHomeScreen
import controler.multiknobcontroller.prototype.controls.actions.navigations.navigateDown
import controler.multiknobcontroller.prototype.controls.actions.navigations.navigateLeft
import controler.multiknobcontroller.prototype.controls.actions.navigations.navigateRight
import controler.multiknobcontroller.prototype.controls.actions.navigations.navigateUp
import controler.multiknobcontroller.prototype.controls.actions.openAllAppsScreen
import controler.multiknobcontroller.prototype.controls.actions.openAppWithPackageName
import controler.multiknobcontroller.prototype.controls.actions.openRecentApps
import controler.multiknobcontroller.prototype.service.BluetoothService
import controler.multiknobcontroller.utils.delayFunction
import controler.multiknobcontroller.utils.delayFunctionWithCallback

class Controller(private val context: Context, private val service: AccessibilityService, private val nodeManager: controler.multiknobcontroller.prototype.controls.NodeManager, private val bluetoothService: BluetoothService) {
    companion object{
        const val TAG = "Controller_MultiKnob"
        private const val Navigation_DELAY = 200L
    }
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val displayMetrics = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }

    private val gestureControls =
        controler.multiknobcontroller.prototype.controls.GestureControls(service)

    val googleMapsControls = controler.multiknobcontroller.prototype.controls.GoogleMapsControls(
        nodeManager,
        gestureControls,
        displayMetrics,
        bluetoothService
    )

    fun doNavigateUp() {
        delayFunction<Unit>(controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Navigate Up"){
            navigateUp(nodeManager)
        }.invoke(Unit)
    }

    fun doNavigateDown() {
        delayFunction<Unit>(controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Navigate Down") {
            navigateDown(nodeManager)
        }.invoke(Unit)
    }

    fun doNavigateLeft() {
        delayFunction<Unit>(controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Navigate Left") {
            navigateLeft(nodeManager, gestureControls)
        }.invoke(Unit)
    }

    fun doNavigateRight() {
        delayFunction<Unit>(controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Navigate Right") {
            navigateRight(nodeManager, gestureControls)
        }.invoke(Unit)
    }

    fun clickCurrentNode() {
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Clicked",{
                _: Unit -> nodeManager.clickCurrentNode()
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun goToHomeScreen() {
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Navigate to Home Screen",{
                _: Unit -> navigateToHomeScreen(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun recentApps(){
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Open Recent Apps",{
            _: Unit -> openRecentApps(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)

    }

    fun goBack() {
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Go Back",{
                _: Unit -> goBackPressed(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openGoogleMaps(){
        val packageName = "com.google.android.apps.maps"
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Start Google Maps",{
                _: Unit -> openAppWithPackageName(context, packageName)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openSpotify(){
        val packageName = "com.spotify.music"
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Start Spotify",{
                _: Unit -> openAppWithPackageName(context, packageName)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openAllApps(){
        delayFunctionWithCallback(
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY,
            controler.multiknobcontroller.prototype.controls.Controller.Companion.Navigation_DELAY, nodeManager, "Open All Apps",{
                _: Unit -> openAllAppsScreen(service, nodeManager, gestureControls)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }
}
