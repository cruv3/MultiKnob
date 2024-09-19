package controler.multiknobcontroller.prototype.signals.googlemaps

import android.util.Log
import controler.multiknobcontroller.utils.entities.MultiKnob
import controler.multiknobcontroller.utils.entities.Signal
import controler.multiknobcontroller.prototype.signals.SignalProcessor
import controler.multiknobcontroller.utils.entities.GlobalState


// Global
// OpenGoogleMaps
class RotateRightGoogleMaps(private val tag: String) {
    companion object{
        private const val TAG_PRESET = " RotateRightGoogleMaps"
    }

    private val TAG = tag + TAG_PRESET

    fun rotateRightInteraction(multiKnob: MultiKnob, callback: SignalProcessor.SignalCallback){
        if(multiKnob.snapPoint != 1) return

        if (multiKnob.fingerCount == 4){
            Log.d(TAG, "Zoom In")
            callback.onSignalReceived(Signal.GoogleMaps.ZOOM_IN)
        }

        if (multiKnob.fingerCount == 3){
            Log.d(TAG, "Move Right")
            callback.onSignalReceived(Signal.GoogleMaps.RIGHT)
        }

        if (multiKnob.fingerCount == 2){
            Log.d(TAG, "Move Up")
            callback.onSignalReceived(Signal.GoogleMaps.UP)
        }
    }
}