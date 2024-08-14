package controler.multiknobcontroller.signals.googlemaps

import android.util.Log
import controler.multiknobcontroller.entities.MultiKnob
import controler.multiknobcontroller.entities.Signal
import controler.multiknobcontroller.signals.SignalProcessor


// Global
// OpenGoogleMaps
class RotateRightGoogleMaps(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateRightGoogleMaps"
    }

    private val TAG = tag + TAG_PRESET

    fun rotateRightInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 5){
            Log.d(TAG, "Zoom In")
            callback.onSignalReceived(Signal.GoogleMaps.ZOOM_IN)
        }

        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 4){
            Log.d(TAG, "Move Right")
            callback.onSignalReceived(Signal.GoogleMaps.RIGHT)
        }

        if (multiKnob.rotation >= 25 && multiKnob.fingerCount == 3){
            Log.d(TAG, "Move Up")
            callback.onSignalReceived(Signal.GoogleMaps.UP)
        }
    }
}