package controler.multiknobcontroller.signals.googlemaps

import android.os.Handler
import android.os.Looper
import android.util.Log
import controler.multiknobcontroller.entities.AppPackage
import controler.multiknobcontroller.entities.MultiKnob
import controler.multiknobcontroller.entities.Signal
import controler.multiknobcontroller.signals.InteractionHandler
import controler.multiknobcontroller.signals.SignalBlocker
import controler.multiknobcontroller.signals.SignalProcessor

class GoogleMapsSignal : InteractionHandler() {
    companion object {
        private const val TAG = "GoogleMapsSignal"
        private const val TRIPLE_TAP_TIMEOUT = 300L
    }

    private var mapInteractionActive = true
    private var mapInteractionInit = true

    private val signalsToBlock = listOf(
        // Add more signals to block as needed
//        Signal.GlobalSignal.LEFT,
//        Signal.GlobalSignal.RIGHT,
        Signal.GlobalSignal.ButtonSignal.SHORT_TAP,
        Signal.GlobalSignal.ButtonSignal.DOUBLE_TAP,
        Signal.GlobalSignal.ButtonSignal.LONG_PRESS
    )

    private val buttonPressGoogleMaps = ButtonPressGoogleMaps(TAG)
    private val rotateLeftGoogleMaps = RotateLeftGoogleMaps(TAG)
    private val rotateRightGoogleMaps = RotateRightGoogleMaps(TAG)

    private val handler = Handler(Looper.getMainLooper())
    private var lastButtonPressTime: Long = 0
    private var lastButtonReleaseTime: Long = 0
    private var tapCount: Int = 0
    private var isButtonPressed: Boolean = false

    override fun handleInteraction(
        packageName: AppPackage?,
        multiKnob: MultiKnob,
        callback: SignalProcessor.SignalCallback
    ) {

        if (packageName != AppPackage.GOOGLE_MAPS) return
        handleMapInteraction(multiKnob)
        if (mapInteractionInit) {
            blockSignals()
            mapInteractionInit = false
        }
        if (!mapInteractionActive) return
        rotateLeftGoogleMaps.rotateLeftInteraction(multiKnob, callback)
        rotateRightGoogleMaps.rotateRightInteraction(multiKnob, callback)
        buttonPressGoogleMaps.googleMapsButtonInteraction(multiKnob, callback)
    }

    private fun handleMapInteraction(multiKnob: MultiKnob) {
        if (checkForDoubleTap(multiKnob)) {
            // Turn off and on the interaction with the map
            mapInteractionActive = !mapInteractionActive
            if (mapInteractionActive) {
                blockSignals()
            } else {
                enableSignals()
            }
        }
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

    private fun enableSignals() {
        for (signal in signalsToBlock) {
            SignalBlocker.unblockSignal(signal)
        }
        Log.d(TAG, "Signals unblocked: $signalsToBlock")
    }

    private fun blockSignals() {
        for (signal in signalsToBlock) {
            SignalBlocker.blockSignal(signal)
        }
        Log.d(TAG, "Signals blocked: $signalsToBlock")
    }
}
