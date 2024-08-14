package controler.multiknobcontroller.controls

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log

class GestureControls(private val service: AccessibilityService) {
    companion object {
        private const val TAG = "GestureControls"
        private const val DELAY_AFTER_SWIPE = 50L
        private const val DELAY_AFTER_CLICK = 2000L
    }

    fun performSwipeGesture(startX: Float, startY: Float, endX: Float, endY: Float, onComplete: () -> Unit) {
        val swipePath = Path().apply {
            moveTo(startX, startY)
            lineTo(endX, endY)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(swipePath, 0, 50))
            .build()

        service.dispatchGesture(gesture, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d(TAG, "Swipe gesture completed.")
                Handler(Looper.getMainLooper()).postDelayed({
                    onComplete()
                }, DELAY_AFTER_SWIPE)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d(TAG, "Swipe gesture cancelled.")
            }
        }, null)
    }

    fun performPinchGesture(
        centerY: Float,
        startX1: Float, endX1: Float,
        startX2: Float, endX2: Float,
        duration: Long = 300L,
        onComplete: () -> Unit
    ) {
        val path1 = Path().apply {
            moveTo(startX1, centerY)
            lineTo(endX1, centerY)
        }

        val path2 = Path().apply {
            moveTo(startX2, centerY)
            lineTo(endX2, centerY)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path1, 0, duration))
            .addStroke(GestureDescription.StrokeDescription(path2, 0, duration))
            .build()

        service.dispatchGesture(gesture, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d(TAG, "Pinch gesture completed.")
                Handler(Looper.getMainLooper()).postDelayed({
                    onComplete()
                }, DELAY_AFTER_SWIPE)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d(TAG, "Pinch gesture cancelled.")
            }
        }, null)
    }

    fun performClick(y: Float, x: Float, onComplete: () -> Unit){
        val clickPath = Path().apply {
            moveTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(clickPath, 0, 100))
            .build()

        service.dispatchGesture(gesture, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d(TAG, "Click gesture completed.")
                Log.d(TAG, "Y: $y, X: $x")
                Handler(Looper.getMainLooper()).postDelayed({
                    onComplete()
                }, DELAY_AFTER_CLICK)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d(TAG, "Click gesture cancelled.")
            }
        }, null)
    }
}
