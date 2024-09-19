package controler.multiknobcontroller.prototype.controls

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
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
        GestureControls(service)

    val googleMapsControls = GoogleMapsControls(
        nodeManager,
        gestureControls,
        displayMetrics,
        bluetoothService
    )

    fun doNavigateUp() {
        delayFunction<Unit>(0, nodeManager, "Navigate Up"){
            navigateUp(nodeManager)
        }.invoke(Unit)
    }

    fun doNavigateDown() {
        delayFunction<Unit>(0, nodeManager, "Navigate Down") {
            navigateDown(nodeManager)
        }.invoke(Unit)
    }

    fun doNavigateLeft() {
        delayFunction<Unit>(0, nodeManager, "Navigate Left") {
            navigateLeft(nodeManager, gestureControls)
        }.invoke(Unit)
    }

    fun doNavigateRight() {
        delayFunction<Unit>(0, nodeManager, "Navigate Right") {
            navigateRight(nodeManager, gestureControls)
        }.invoke(Unit)
    }

    fun clickCurrentNode() {
        delayFunctionWithCallback(
            0,
            Navigation_DELAY, nodeManager, "Clicked",{
                _: Unit -> nodeManager.clickCurrentNode()
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun goToHomeScreen() {
        delayFunctionWithCallback(
            0,
            Navigation_DELAY, nodeManager, "Navigate to Home Screen",{
                _: Unit -> navigateToHomeScreen(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun recentApps(){
        delayFunctionWithCallback(
            Navigation_DELAY,
            Navigation_DELAY, nodeManager, "Open Recent Apps",{
            _: Unit -> openRecentApps(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)

    }

    fun goBack() {
        delayFunctionWithCallback(
            Navigation_DELAY,
            Navigation_DELAY, nodeManager, "Go Back",{
                _: Unit -> goBackPressed(service)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun startAppWithPackageName(packageName: String){
        delayFunctionWithCallback(
            Navigation_DELAY,
            Navigation_DELAY, nodeManager, packageName,{
                    _: Unit ->
                val launchIntent: Intent? =
                    context.packageManager.getLaunchIntentForPackage(packageName)
                launchIntent?.addCategory(Intent.CATEGORY_LAUNCHER);
                if(launchIntent != null){
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntent)
                }
            }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }


    fun startAppWithIntent(intent: Intent){
        delayFunctionWithCallback(
            Navigation_DELAY,
            Navigation_DELAY, nodeManager, intent.toString(),{
                    _: Unit ->
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }

    fun openAllApps(){
        delayFunctionWithCallback(
            Navigation_DELAY,
            Navigation_DELAY, nodeManager, "Open All Apps",{
                _: Unit -> openAllAppsScreen(service, nodeManager, gestureControls)
        }){
            nodeManager.setupNodes()
        }.invoke(Unit)
    }
}
