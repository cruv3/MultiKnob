package controler.multiknobcontroller.controls.actions.maps

import android.util.DisplayMetrics
import controler.multiknobcontroller.controls.GestureControls

fun zoomInMaps(gestureControls: GestureControls, metrics: DisplayMetrics, onComplete: () -> Unit = {}) {
    // Calculate the center of the screen
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = (screenHeight / 2f)

    // Define the pinch-to-zoom-in gesture
    val startX1 = centerX - 50
    val endX1 = centerX - 100

    val startX2 = centerX + 50
    val endX2 = centerX + 100


    gestureControls.performPinchGesture(centerY, startX1, endX1, startX2, endX2, onComplete = onComplete)
}

fun zoomOutMaps(gestureControls: GestureControls, metrics: DisplayMetrics, onComplete: () -> Unit = {}) {
    val screenWidth = metrics.widthPixels
    val screenHeight = metrics.heightPixels

    val barWidth = 80
    val centerX = (screenWidth - barWidth) / 2f + barWidth
    val centerY = (screenHeight / 2f)

    // Define the pinch-to-zoom-out gesture
    val startX1 = centerX - 100
    val endX1 = centerX - 50

    val startX2 = centerX + 100
    val endX2 = centerX + 50

    gestureControls.performPinchGesture(centerY, startX1, endX1, startX2, endX2, onComplete = onComplete)
}
