package controler.multiknobcontroller.prototype.signals.googlemaps

import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.prototype.signals.global.ButtonPressGlobal
import controler.multiknobcontroller.prototype.signals.global.ButtonPressGlobal.Companion

// Global
// LongPress
// DoubleTap

class ButtonPressGoogleMaps(private val tag: String){
    companion object {
        private const val TAG_PRESET = " ButtonPressGoogleMaps"
        private const val LONG_PRESS_THRESHOLD = 1000L
        private const val DOUBLE_TAP_TIMEOUT = 300L
    }

    private val TAG = tag + TAG_PRESET

    private var lastButtonPressTime: Long = 0
    private var lastButtonReleaseTime: Long = 0
    private var tapCount: Int = 0
    private var isButtonPressed: Boolean = false
    private var longPressTriggered: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    fun googleMapsButtonInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        val currentTime = System.currentTimeMillis()

        if (multiKnob.buttonPress == 1 && !isButtonPressed) {
            isButtonPressed = true
            longPressTriggered = false
            lastButtonPressTime = currentTime

            if (currentTime - lastButtonReleaseTime < DOUBLE_TAP_TIMEOUT) {
                tapCount++
            } else {
                tapCount = 1
            }
        } else if (multiKnob.buttonPress == 0 && isButtonPressed) {
            isButtonPressed = false
            lastButtonReleaseTime = currentTime

            handler.postDelayed({
                if (tapCount == 1 && !longPressTriggered) {
                    Log.d(TAG, "Short Tap")
                    callback.onSignalReceived(Signal.GoogleMaps.ButtonSignal.SHORT_TAP)
                    tapCount = 0
                }
            }, DOUBLE_TAP_TIMEOUT)
        }
    }
}
