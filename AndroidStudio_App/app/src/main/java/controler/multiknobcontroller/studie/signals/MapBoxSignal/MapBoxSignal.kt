package controler.multiknobcontroller.studie.signals.MapBoxSignal

import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.studie.signals.InteractionHandler
import controler.multiknobcontroller.studie.signals.SignalProcessor

class MapBoxSignal : InteractionHandler() {
    companion object {
        private const val TAG = "MapSignal"
        private const val TRIPLE_TAP_TIMEOUT = 300L
    }

    private var mapInteractionActive = true
    private var mapInteractionInit = true

    private val buttonPressGoogleMaps = ButtonPress(TAG)
    private val rotateLeft = RotateLeft(TAG)
    private val rotateRight = RotateRight(TAG)

    private val handler = Handler(Looper.getMainLooper())

    private var lastButtonPressTime: Long = 0
    private var lastButtonReleaseTime: Long = 0
    private var tapCount: Int = 0
    private var isButtonPressed: Boolean = false

    override fun handleInteraction(
        multiKnob: MultiKnob,
        callback: SignalProcessor.SignalCallback
    ) {
        rotateLeft.rotateLeftInteraction(multiKnob, callback)
        rotateRight.rotateRightInteraction(multiKnob, callback)
        buttonPressGoogleMaps.buttonInteraction(multiKnob, callback)
    }

    private fun checkForDoubleTap(multiKnob: MultiKnob): Boolean {
        val currentTime = System.currentTimeMillis()

        if (multiKnob.buttonPress == 1 && !isButtonPressed) {
            isButtonPressed = true
            lastButtonPressTime = currentTime

            if (currentTime - lastButtonReleaseTime < TRIPLE_TAP_TIMEOUT) {
                tapCount++
            } else {
                tapCount = 1
            }

            if (tapCount == 2) {
                handler.removeCallbacksAndMessages(null)
                tapCount = 0 // Reset tapCount on triple tap detection
                Log.d(TAG, "Double Tap")
                return true
            }
        } else if (multiKnob.buttonPress == 0 && isButtonPressed) {
            isButtonPressed = false
            lastButtonReleaseTime = currentTime
        }
        return false
    }
}
