package controler.multiknobcontroller.prototype.controls.actions.maps

import android.util.DisplayMetrics
import controler.multiknobcontroller.prototype.controls.GestureControls

fun moveMapLeft(gestureControls: GestureControls, metrics: DisplayMetrics, onComplete: () -> Unit = {}) {
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = screenHeight / 2f

    val startX = centerX
    val endX = centerX + 25 // Adjust distance for desired movement
    val startY = centerY
    val endY = centerY

    gestureControls.performSwipeGesture(startX, startY, endX, endY, onComplete)
}

fun moveMapRight(gestureControls: GestureControls, metrics: DisplayMetrics, onComplete: () -> Unit = {}) {
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = screenHeight / 2f

    val startX = centerX
    val endX = centerX - 25 // Adjust distance for desired movement
    val startY = centerY
    val endY = centerY

    gestureControls.performSwipeGesture(startX, startY, endX, endY, onComplete)
}

fun moveMapUp(gestureControls: GestureControls, metrics: DisplayMetrics, onComplete: () -> Unit = {}) {
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = screenHeight / 2f

    val startX = centerX
    val startY = centerY
    val endX = centerX
    val endY = centerY + 25 // Adjust distance for desired movement

    gestureControls.performSwipeGesture(startX, startY, endX, endY, onComplete)
}

fun moveMapDown(gestureControls: GestureControls, metrics: DisplayMetrics, onComplete: () -> Unit = {}) {
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = screenHeight / 2f

    val startX = centerX
    val startY = centerY
    val endX = centerX
    val endY = centerY - 25 // Adjust distance for desired movement

    gestureControls.performSwipeGesture(startX, startY, endX, endY, onComplete)
}
