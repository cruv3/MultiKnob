package controler.multiknobcontroller.prototype.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import controler.multiknobcontroller.prototype.controls.Controller
import controler.multiknobcontroller.prototype.controls.NodeManager
import controler.multiknobcontroller.utils.entities.AppPackage
import controler.multiknobcontroller.utils.entities.GlobalState
import controler.multiknobcontroller.utils.entities.Signal


class AccessibilityService : AccessibilityService(), BluetoothService.BluetoothDataCallback {
    companion object {
        private const val TAG = "AccessibilityService"
        private const val CHECK_INTERVAL = 1000L // Check every 1 second
    }

    private lateinit var nodeManager: NodeManager
    private lateinit var controller: controler.multiknobcontroller.prototype.controls.Controller
    private lateinit var bluetoothService: BluetoothService


    override fun onServiceConnected() {
        bluetoothService = BluetoothService(this, this)
        bluetoothService.startBluetoothScan("64:B7:08:29:37:8E")

        nodeManager = NodeManager(this)
        controller = controler.multiknobcontroller.prototype.controls.Controller(
            this,
            this,
            nodeManager,
            bluetoothService
        )
        controller.goToHomeScreen()
    }

    override fun onSignalReceived(globalSignal: Signal.GlobalSignal) {
        when (globalSignal) {
            Signal.GlobalSignal.GO_BACK -> controller.goBack()
            Signal.GlobalSignal.OPEN_GOOGLE_MAPS -> controller.openGoogleMaps()
            Signal.GlobalSignal.OPEN_SPOTIFY -> controller.openSpotify()
            Signal.GlobalSignal.LEFT -> controller.doNavigateLeft()
            Signal.GlobalSignal.RIGHT -> controller.doNavigateRight()
            Signal.GlobalSignal.UP -> controller.doNavigateUp()
            Signal.GlobalSignal.DOWN -> controller.doNavigateDown()
        }
    }

    override fun onSignalReceived(buttonSignal: Signal.GlobalSignal.ButtonSignal) {
        when (buttonSignal) {
            Signal.GlobalSignal.ButtonSignal.SHORT_TAP -> controller.clickCurrentNode()
            Signal.GlobalSignal.ButtonSignal.LONG_PRESS -> controller.goToHomeScreen()
            Signal.GlobalSignal.ButtonSignal.DOUBLE_TAP -> controller.recentApps()
            Signal.GlobalSignal.ButtonSignal.ALL_APPS -> controller.openAllApps()
        }
    }

    override fun onSignalReceived(signalGoogle: Signal.GoogleMaps) {
        when (signalGoogle) {
            Signal.GoogleMaps.ZOOM_IN -> controller.googleMapsControls.zoomIn()
            Signal.GoogleMaps.ZOOM_OUT -> controller.googleMapsControls.zoomOut()
            Signal.GoogleMaps.LEFT -> controller.googleMapsControls.moveLeft()
            Signal.GoogleMaps.RIGHT -> controller.googleMapsControls.moveRight()
            Signal.GoogleMaps.UP -> controller.googleMapsControls.moveUp()
            Signal.GoogleMaps.DOWN -> controller.googleMapsControls.moveDown()
        }
    }

    override fun onSignalReceived(buttonSignal: Signal.GoogleMaps.ButtonSignal) {
        when (buttonSignal) {
            Signal.GoogleMaps.ButtonSignal.SHORT_TAP -> controller.googleMapsControls.startNavigation()
            Signal.GoogleMaps.ButtonSignal.LONG_PRESS -> Log.d(TAG, "Long press not implemented")
            Signal.GoogleMaps.ButtonSignal.DOUBLE_TAP -> Log.d(TAG, "Double tap not implemented")
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            val packageName = event.packageName.toString()
            Log.d(TAG, packageName)
            val appPackage = AppPackage.fromPackageName(packageName)
            GlobalState.currentApp = appPackage

            val root = rootInActiveWindow ?: return
            nodeManager.setupNodes(root)
            val nodes = nodeManager.getNodes() ?: return
            for (node in nodes){
                val text = node.node.text?.toString() ?: ""
                if(text.contains("Route")){
                    Log.d(TAG, text)
                }
            }
        }
    }

    override fun onInterrupt() {
        bluetoothService.stopListeningThread()
    }
}