package controler.multiknobcontroller.prototype.controls.actions

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS
import android.os.Handler
import android.os.Looper
import controler.multiknobcontroller.prototype.controls.GestureControls
import controler.multiknobcontroller.prototype.controls.NodeManager

fun navigateToHomeScreen(service: AccessibilityService) {
    service.performGlobalAction(GLOBAL_ACTION_HOME)
}

fun openRecentApps(service: AccessibilityService){
    service.performGlobalAction(GLOBAL_ACTION_RECENTS)
}

fun goBackPressed(service: AccessibilityService){
    service.performGlobalAction(GLOBAL_ACTION_BACK)
}

fun openAllAppsScreen(service: AccessibilityService, nodeManager: NodeManager, gestureControls: GestureControls){
    navigateToHomeScreen(service)

    Handler(Looper.getMainLooper()).postDelayed({
        nodeManager.setupNodes()
        val nodes = nodeManager.getNodes() ?: return@postDelayed
        for(node in nodes){
            val text = node.node.text?.toString() ?: ""
            if(text.contains("Alle Apps")){
                gestureControls.performClick(node.bounds.exactCenterY(), node.bounds.exactCenterX()){}
            }
        }
    },500)
}