package controler.multiknobcontroller.prototype.controls.actions.maps

import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import controler.multiknobcontroller.prototype.controls.GestureControls
import controler.multiknobcontroller.prototype.controls.NodeManager
import controler.multiknobcontroller.utils.delayFunction

fun panToLocationMaps(gestureControls: GestureControls, startX: Float, startY: Float, endX: Float, endY: Float, onComplete: () -> Unit = {}) {
    gestureControls.performSwipeGesture(startX, startY, endX, endY, onComplete)
}

fun selectLocationMaps(gestureControls: GestureControls, x: Float, y: Float, onComplete: () -> Unit = {}) {
    // Tap gesture to select a location
    gestureControls.performSwipeGesture(x, y, x, y, onComplete)
}

fun startMapNavigation(gestureControls: GestureControls, nodeManager : NodeManager){
    Handler(Looper.getMainLooper()).postDelayed({
        nodeManager.setupNodes()
        val nodes = nodeManager.getNodes() ?: return@postDelayed
        for(node in nodes){
            val text = node.node.text?.toString() ?: ""
            if(text.contains("Starten")){
                gestureControls.performClick(node.bounds.exactCenterY(), node.bounds.exactCenterX()){}
            }
        }
    },500)
}

private fun waitForUINodes(nodeManager: NodeManager, retries: Int = 5){
    if (retries <= 0) {
        return
    }

    nodeManager.setupNodes()
    val nodes = nodeManager.getNodes()
    val routeButton = nodes?.find { it.node.text?.toString()?.contains("Route", ignoreCase = true) == true }
    if(routeButton == null){
        Handler(Looper.getMainLooper()).postDelayed({
            waitForUINodes(nodeManager, retries - 1)
        }, 500)
    }else{
        nodeManager.focusNode(routeButton)
        routeButton.node.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        val startButton = nodes.find { it.node.text?.toString()?.contains("Start", ignoreCase = true) == true }
        if (startButton != null) {
            nodeManager.focusNode(startButton)
            startButton.node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    if (nodes.isNullOrEmpty()) {
        Handler(Looper.getMainLooper()).postDelayed({
            waitForUINodes(nodeManager, retries - 1)
        }, 500) // Delay in milliseconds before retrying
    } else {
        //val names = nodes.map { it.node.text.toString() }

        // Now look for the "Start" button
        val routeButton = nodes.find { it.node.text?.toString()?.contains("Route", ignoreCase = true) == true }
        if (routeButton != null) {
            nodeManager.focusNode(routeButton)
            routeButton.node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }

        // Now look for the "Start" button
        val startButton = nodes.find { it.node.text?.toString()?.contains("Start", ignoreCase = true) == true }
        if (startButton != null) {
            nodeManager.focusNode(startButton)
            startButton.node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }
}

fun startNavigationAtCenterPosition(gestureControls: GestureControls, metrics: DisplayMetrics){
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = (screenHeight / 2f)

    gestureControls.performClick(centerY, centerX){}
}